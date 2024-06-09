package com.team4studio.travelnow.view.screens.product

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.team4studio.travelnow.R
import com.team4studio.travelnow.model.remote.entity.Review
import com.team4studio.travelnow.view.components.AdminBottomBar
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.components.UserBottomBar
import com.team4studio.travelnow.view.navigation.Screen
import com.team4studio.travelnow.viewmodel.AppViewModel
import com.team4studio.travelnow.viewmodel.ProductViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ProductDetails(
    navController: NavHostController,
    productId: String,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    viewModel: ProductViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val currentUserId = appViewModel.getCurrentUserId()
    viewModel.setCurrentProduct(productId)

    val product = viewModel.product

    val reviews = product.value.reviews
    val avgRating = viewModel.getProductAvgRating(reviews)
    val reviewCount = reviews.size

    val userBought = viewModel.userBoughtItem(currentUserId, productId)

    Scaffold(topBar = { TopBar(navBack = { navController.popBackStack() }) }, content = { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxHeight()
        ) {
            var titleFontSize = 25.sp
            if (product.value.title.length > 55) titleFontSize = 22.sp
            Text(product.value.title, fontWeight = FontWeight.Bold, fontSize = titleFontSize)

            if (reviewCount != 0) {
                Row(Modifier.padding(bottom = 10.dp)) {
                    Text("⭐$avgRating", fontWeight = FontWeight.Bold)
                    Text(" ($reviewCount ${if (reviewCount == 1) "Đánh Giá" else "Reviews"})")
                }
            }

            LazyColumn(
                Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item() {
                    Image(
                        painter = rememberAsyncImagePainter(model = product.value.image),
                        contentDescription = "Product image",
                        Modifier.size(300.dp)
                    )
                }

                item() { Text(product.value.description) }

                item() { Divider() }

                item() {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Reviews", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        if (userBought) {
                            IconButton(onClick = {
                                navController.navigate("manageReview/${productId}")
                            }) {
                                Row() {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = "Thêm Đánh Giá",
                                        tint = Color.Blue
                                    )
                                }
                            }
                        }
                    }
                }

                if (reviewCount != 0) items(items = reviews) { ReviewCard(it) }
                else item() { Text("Sản phẩm này không có đánh giá nào.") }
            }

            val openDialog = remember { mutableStateOf(false) }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$${product.value.price}", fontWeight = FontWeight.Bold, fontSize = 25.sp)

                Button(
                    onClick = {
                        viewModel.addToCart(currentUserId, productId)
                        openDialog.value = true
                    }, Modifier.padding(top = 10.dp), enabled = product.value.stock != 0
                ) {
                    Text("THÊM VÀO VÉ", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (openDialog.value) {
                AlertDialog(onDismissRequest = { openDialog.value = false },
                    title = { Text("Đã Thêm ") },
                    text = {
                        Column() {
                            Text(
                                "Đã Thêm Vào Vé.", Modifier.padding(top = 10.dp)
                            )
                        }
                    },
                    confirmButton = {
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            openDialog.value = false
                            navController.navigate(Screen.Cart.route)
                        }) {
                            Text("Đi Đến Vé Của Tôi")
                        }
                    },
                    dismissButton = {
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            openDialog.value = false
                            navController.navigate(Screen.Home.route)
                        }) {
                            Text("Tiếp Tục Đặt Phòng")
                        }
                    })
            }
        }
    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
        else UserBottomBar(navController = navController)
    }
    )
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ReviewCard(review: Review) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Xem lại ảnh hồ sơ người dùng",
                        Modifier
                            .size(25.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )

                    Text(
                        text = review.date,
                        Modifier.padding(start = 5.dp, end = 5.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text("⭐".repeat(review.stars))
            }
            Text(review.title, fontWeight = FontWeight.Bold)
            Text(review.details)
        }
    }
}
