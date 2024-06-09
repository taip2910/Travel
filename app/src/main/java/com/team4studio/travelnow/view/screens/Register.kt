package com.team4studio.travelnow.view.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team4studio.travelnow.view.components.DatePickerDialog
import com.team4studio.travelnow.view.components.SuccessDialog
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.viewmodel.RegisterViewModel
import java.util.*

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun Register(
    navToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val inputFieldModifier = Modifier
        .padding(top = 10.dp)
        .fillMaxWidth()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        // Modifier for click action anywhere on the screen - Hide keyboard and reset focus
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        },

        topBar = { TopBar(navBack = { navToLogin() }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
        ) {
            val context = LocalContext.current

            Text("Tạo Tài Khoản", fontWeight = FontWeight.Bold, fontSize = 40.sp)
            Text("Hãy tham gia cùng chúng tôi và bắt đầu đặt phòng ngay bây giờ!", fontSize = 15.sp)

            OutlinedTextField(modifier = inputFieldModifier,
                value = viewModel.firstName,
                onValueChange = {
                    if (viewModel.fNameError) viewModel.fNameError = false
                    viewModel.firstName = it
                },
                label = { Text("Tên") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                isError = viewModel.fNameError)

            OutlinedTextField(value = viewModel.lastName,
                modifier = inputFieldModifier,
                onValueChange = {
                    if (viewModel.lNameError) viewModel.lNameError = false
                    viewModel.lastName = it
                },
                label = { Text("Họ") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                isError = viewModel.lNameError)

            OutlinedTextField(value = viewModel.email,
                modifier = inputFieldModifier,
                onValueChange = {
                    if (viewModel.emailError) viewModel.emailError = false
                    viewModel.email = it
                },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                isError = viewModel.emailError)


            OutlinedTextField(value = viewModel.password,
                modifier = inputFieldModifier,
                onValueChange = {
                    if (viewModel.passwordError) viewModel.passwordError = false
                    viewModel.password = it
                },
                label = { Text("Mật Khẩu") },
                singleLine = true,
                visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                isError = viewModel.passwordError,
                trailingIcon = {
                    val image =
                        if (viewModel.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description =
                        if (viewModel.passwordVisible) "Ẩn Mật Khẩu" else "Hiện Mật Khẩu"
                    IconButton(onClick = {
                        viewModel.passwordVisible = !viewModel.passwordVisible
                    }) {
                        Icon(imageVector = image, description)
                    }
                })

            //Date Picker
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.YEAR, -18)

            val dateDialog = DatePickerDialog(
                viewModel.birthDate, context, calendar, calendar.timeInMillis
            )

            OutlinedTextField(value = viewModel.birthDate.value,
                modifier = inputFieldModifier.clickable(indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                    dateDialog.show()
                },
                onValueChange = {},
                label = { Text("Ngày Sinh") },
                singleLine = true,
                enabled = false,
                isError = viewModel.dobError
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TermsCheck(viewModel.termsIsChecked)

                val openDialog = remember { mutableStateOf(false) }
                Button(modifier = Modifier.padding(top = 20.dp),
                    enabled = viewModel.termsIsChecked.value,
                    onClick = {
                        if (!viewModel.validateRegisterInput()) {
                            Toast.makeText(context, "Không Hợp Lệ!", Toast.LENGTH_SHORT).show()
                        } else if (!viewModel.validateExistingAccount()) {
                            Toast.makeText(context, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            viewModel.addUser()
                            openDialog.value = true
                        }
                    }) {
                    Text("ĐĂNG KÝ", fontSize = 20.sp)
                }

                SuccessDialog(
                    openDialog, "Tạo Tài Khoản Thành Công", "Tận Hưởng Vui Vẻ!", "Đăng Nhập"
                ) { navToLogin() }
            }
        }
    }
}

@Composable
fun TermsCheck(termsIsChecked: MutableState<Boolean>) {
    Row(verticalAlignment = Alignment.Top) {
        Checkbox(
            checked = termsIsChecked.value,
            onCheckedChange = { termsIsChecked.value = it },

            modifier = Modifier
                .padding(5.dp)
                .size(3.dp),
        )
        Spacer(modifier = Modifier.padding(5.dp))
        Text("Tôi đồng ý với Điều khoản dịch vụ và Chính sách quyền riêng tư.",
            Modifier.clickable(indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                termsIsChecked.value = !termsIsChecked.value
            },
            fontSize = 12.sp
        )
    }
}