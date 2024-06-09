package com.team4studio.travelnow.view.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.components.UserBottomBar
import com.team4studio.travelnow.viewmodel.AppViewModel
import com.team4studio.travelnow.viewmodel.ReviewViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ManageReview(
    navController: NavHostController,
    productId: String,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    reviewViewModel: ReviewViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    reviewViewModel.setUserId(appViewModel.getCurrentUserId())
    reviewViewModel.setProductId(productId)

    val product = reviewViewModel.getProduct()

    val inputFieldModifier = Modifier
        .padding(top = 5.dp)
        .fillMaxWidth()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }, topBar = {

        var type = ""
        reviewViewModel.returnReview()?.let { type = "Sứa" } ?: run { type = "Thêm" }
        TopBar(type, { navController.popBackStack() }, actions = {
            if (type == "Sửa") {
                IconButton(onClick = {
                    reviewViewModel.openDialog = true
                }) {
                    Row(Modifier.fillMaxWidth(0.5f)) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Xóa Đánh Giá",
                            tint = Color.Red
                        )
                    }
                }
            }
        })
    }, content = { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
            product?.let {
                Row(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
                    Image(
                        painter = rememberAsyncImagePainter(model = it.image),
                        contentDescription = "placeholder image",
                        Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.width(32.dp))
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = it.title,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 20.sp
                        )
                        RatingBar(reviewViewModel)
                    }
                }
            }

            Column() {
                Text(text = "Tiêu Đề Đánh Giá", fontSize = 14.sp)
                OutlinedTextField(
                    modifier = inputFieldModifier,
                    value = reviewViewModel.reviewTitle,
                    onValueChange = {
                        if (reviewViewModel.titleError) reviewViewModel.titleError = false
                        reviewViewModel.reviewTitle = it
                    },
                    placeholder = { Text("Phần mở đầu") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = reviewViewModel.titleError
                )
            }

            Column() {
                Text(text = "Phần mô tả", fontSize = 14.sp)
                OutlinedTextField(
                    modifier = inputFieldModifier.fillMaxHeight(0.5f),
                    value = reviewViewModel.reviewDescription,
                    onValueChange = {
                        if (reviewViewModel.descriptionError) reviewViewModel.descriptionError =
                            false
                        reviewViewModel.reviewDescription = it
                    },
                    placeholder = { Text("Chi tiết") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = reviewViewModel.descriptionError
                )
            }

            Button(modifier = Modifier
                .align(Alignment.End)
                .padding(top = 50.dp), onClick = {}) {
                Text(text = "Submit", fontSize = 20.sp, modifier = Modifier.clickable {
                    val allowSubmit: Boolean = reviewViewModel.validateReviewInput()
                    reviewViewModel.returnReview()?.let {
                        if (allowSubmit) {
                            reviewViewModel.updateReview()
                            Toast.makeText(context, "Đánh giá đã cập nhật", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    } ?: run {

                        if (allowSubmit) {
                            reviewViewModel.addReview()
                            Toast.makeText(context, "Đã thêm đánh giá", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                })
            }

            if (reviewViewModel.openDialog) {
                AlertDialog(onDismissRequest = { reviewViewModel.openDialog = false },
                    title = { Text("Bạn có chắc chắn muốn xóa đánh giá?") },
                    text = {
                        Column() {
                            Text(
                                "Thao tác này không thể hoàn tác.",
                                Modifier.padding(top = 10.dp)
                            )
                        }
                    },
                    confirmButton = {
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            reviewViewModel.openDialog = false
                            reviewViewModel.deleteReview()
                            Toast.makeText(context, "Đã xóa đánh giá.", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }) {
                            Text("Xác nhận")
                        }
                    },
                    dismissButton = {
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            reviewViewModel.openDialog = false

                        }) {
                            Text("Hủy")
                        }
                    })
            }
        }
    }, bottomBar = { UserBottomBar(navController) })
}

@Composable
fun RatingBar(reviewViewModel: ReviewViewModel) {
    Column() {
        Text(text = "Chọn đánh giá: ", fontSize = 12.sp)
        Row() {
            for (i in 1..5) {
                Icon(
                    imageVector = Icons.Filled.StarRate,
                    contentDescription = "sao",
                    modifier = Modifier
                        .requiredWidthIn(28.dp, 35.dp)
                        .height(32.dp)
                        .clickable {
                            reviewViewModel.ratingValue = i
                        },
                    tint = if (i <= reviewViewModel.ratingValue) Color(255, 215, 0) else Color.Gray
                )
            }
        }
    }
}


