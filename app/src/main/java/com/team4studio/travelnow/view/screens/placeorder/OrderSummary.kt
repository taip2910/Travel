package com.team4studio.travelnow.view.screens.placeorder

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team4studio.travelnow.model.remote.entity.CartWithProduct
import com.team4studio.travelnow.model.remote.entity.Address
import com.team4studio.travelnow.view.components.AdminBottomBar
import com.team4studio.travelnow.view.components.SuccessDialog
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.components.UserBottomBar
import com.team4studio.travelnow.view.navigation.Screen
import com.team4studio.travelnow.viewmodel.AppViewModel
import com.team4studio.travelnow.viewmodel.CartViewModel
import com.team4studio.travelnow.viewmodel.PlaceOrderViewModel

@ExperimentalMaterial3Api
@Composable
fun OrderSummary(
    navController: NavHostController,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    placeOrderViewModel: PlaceOrderViewModel = viewModel(LocalContext.current as ComponentActivity),
    cartViewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
) {
    Scaffold(topBar = { TopBar("Tóm Tắt", { navController.popBackStack() }) },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
            ) {
                val userCart = cartViewModel.userCart.value
                val totalPrice = cartViewModel.grandTotal.value

                Column(
                    Modifier.weight(5f, true)
                ) {
                    SummaryPageSubtitleText("VÉ")

                    LazyColumn(
                        Modifier
                            .weight(1f)
                            .padding(bottom = 5.dp)
                    ) {
                        items(items = userCart, key = { userCart -> userCart.cart.id }) {
                            SummaryPageItemRow(it)
                        }
                    }

                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("TOTAL:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("$$totalPrice", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    SummaryPageDivider()
                }

                // Selected Address
                Column(Modifier.weight(2.5f)) {
                    SummaryPageSubtitleText("XÁC NHẬN")
                    SummaryPageAddressDetails(placeOrderViewModel.selectedAddress)
                    SummaryPageDivider()
                }

                // Selected Payment
                Column(Modifier.weight(1f)) {
                    SummaryPageSubtitleText("PHƯƠNG THỨC THANH TOÁN")
                    Text(placeOrderViewModel.selectedPayment)
                    SummaryPageDivider()
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val openDialog = remember { mutableStateOf(false) }
                    Button(onClick = {
                        placeOrderViewModel.placeOrder(userCart, totalPrice)
                        openDialog.value = true
                    }) {
                        Text("Đặt Phòng")
                    }

                    SuccessDialog(
                        openDialog, "Xác Nhận Đặt Phòng", "Cảm Ơn Bạn Đặt Phòng!", "Ẩn"
                    ) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (appViewModel.isAdmin) AdminBottomBar(
                navController = navController
            )
            else UserBottomBar(navController = navController)
        }

    )
}

@Composable
fun SummaryPageSubtitleText(text: String) {
    Text(text, Modifier.padding(bottom = 8.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun SummaryPageItemRow(cartItem: CartWithProduct) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp)
    ) {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = cartItem.product.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "$${cartItem.product.price}")

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "x ${cartItem.cart.quantity}")
                }
            }
        }
    }
}

@Composable
fun SummaryPageAddressDetails(address: Address) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(10.dp)) {
            Text(address.name, fontWeight = FontWeight.Bold)
            Text("Đơn vị ${address.unit}, Tòa nhà ${address.building}")
            Text("Đường ${address.street}, Zalo ${address.zone}")
            Text("Hộp thư: ${address.poBox}")
            Text(address.phone)
        }
    }
}

@Composable
fun SummaryPageDivider() {
    Divider(Modifier.padding(top = 10.dp, bottom = 10.dp))
}