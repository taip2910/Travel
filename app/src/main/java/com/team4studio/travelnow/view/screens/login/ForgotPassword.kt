package com.team4studio.travelnow.view.screens.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.navigation.Screen
import com.team4studio.travelnow.viewmodel.ForgotViewModel


@ExperimentalComposeUiApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Forgot(
    navController: NavHostController, viewModel: ForgotViewModel, onVerifyClick: () -> Unit
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        // Modifier for click action anywhere on the screen - Hide keyboard and reset focus
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        }, topBar = { TopBar(navBack = { navController.popBackStack() }) }, content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp),
            ) {
                Text(
                    text = "Quên Mật Khẩu", fontWeight = FontWeight.Bold, fontSize = 40.sp
                )
                Text(
                    text = "Vui lòng nhập địa chỉ email của bạn, mã xác minh sẽ được gửi đến email của bạn"
                )
                Spacer(modifier = Modifier.height(102.dp))
                Text(
                    text = "Địa chỉ Email"
                )
                OutlinedTextField(value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text(text = "Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                fun sendVerificationCode() {
                    if (viewModel.validateEmailInput()) {
                        viewModel.generateVCode()
                        navController.navigate(Screen.VerifyPassword.route)
                    } else {
                        Toast.makeText(context, "Thông tin không hợp lệ!", Toast.LENGTH_SHORT).show()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            sendVerificationCode()
                        }, modifier = Modifier.fillMaxWidth()

                    ) {
                        Text(
                            text = "Gửi mã xác minh"
                        )
                    }
                }

            }
        })
}






