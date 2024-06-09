package com.team4studio.travelnow.view.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.navigation.Screen
import com.team4studio.travelnow.viewmodel.AppViewModel
import com.team4studio.travelnow.viewmodel.ManageAddressViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ManageAddress(
    navController: NavHostController,
    initScreen: String?,
    addressId: String,
    viewModel: ManageAddressViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    viewModel.setUser(
        viewModel<AppViewModel>(LocalContext.current as ComponentActivity).getCurrentUserId(),
    )

    val inputFieldModifier = Modifier
        .padding(top = 10.dp)
        .fillMaxWidth()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var manageType = ""
    if (addressId == "-1") {
        manageType = "Thêm"
        viewModel.resetFields()
    } else {
        manageType = "Sửa"
        viewModel.setCurrentAddress(addressId)
    }

    Scaffold(
        // Modifier for click action anywhere on the screen - Hide keyboard and reset focus
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            keyboardController?.hide()
            focusManager.clearFocus()
        },

        topBar = { TopBar("$manageType Thông Tin", { navController.popBackStack() }) },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
            ) {

                val context = LocalContext.current
                OutlinedTextField(
                    modifier = inputFieldModifier,
                    value = viewModel.contactName,
                    onValueChange = {
                        if (viewModel.nameError) viewModel.nameError = false
                        viewModel.contactName = it
                    },
                    label = { Text("Tên") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    isError = viewModel.nameError
                )

                OutlinedTextField(
                    value = viewModel.unit,
                    modifier = inputFieldModifier,
                    onValueChange = {
                        if (viewModel.unitError) viewModel.unitError = false
                        viewModel.unit = it
                    },
                    label = { Text("CCCD ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = viewModel.unitError
                )

                OutlinedTextField(
                    value = viewModel.building,
                    modifier = inputFieldModifier,
                    onValueChange = {
                        if (viewModel.buildingError) viewModel.buildingError = false
                        viewModel.building = it
                    },
                    label = { Text("Tòa Nhà ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = viewModel.buildingError
                )

                OutlinedTextField(
                    value = viewModel.street,
                    modifier = inputFieldModifier,
                    onValueChange = {
                        if (viewModel.streetError) viewModel.streetError = false
                        viewModel.street = it
                    },
                    label = { Text("Đường ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = viewModel.streetError
                )

                OutlinedTextField(
                    value = viewModel.zone,
                    modifier = inputFieldModifier,
                    onValueChange = {
                        if (viewModel.zoneError) viewModel.zoneError = false
                        viewModel.zone = it
                    },
                    label = { Text("Zalo ") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = viewModel.zoneError
                )

                OutlinedTextField(
                    value = viewModel.poBox,
                    modifier = inputFieldModifier,
                    onValueChange = {
                        if (viewModel.poBoxError) viewModel.poBoxError = false
                        viewModel.poBox = it
                    },
                    label = { Text("Hộp Thư") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = viewModel.poBoxError
                )

                OutlinedTextField(
                    value = viewModel.contactNumber,
                    modifier = inputFieldModifier,
                    onValueChange = {
                        if (viewModel.numberError) viewModel.numberError = false
                        viewModel.contactNumber = it
                    },
                    label = { Text("Phone") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = viewModel.numberError
                )

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {
                    Button(
                        onClick = {
                            if (!viewModel.validateAddingAddress()) {
                                Toast.makeText(context, "Invalid input!", Toast.LENGTH_SHORT).show()
                            } else {
                                if (manageType == "Add") viewModel.addAddress()
                                else if (manageType == "Edit") viewModel.updateAddress(addressId)

                                if (initScreen == "Select") {
                                    navController.navigate(Screen.SelectAddress.route) {
                                        popUpTo(Screen.SelectAddress.route) { inclusive = true }
                                    }
                                } else if (initScreen == "Manage") {
                                    navController.navigate(Screen.MyAddresses.route) {
                                        popUpTo(Screen.MyAddresses.route) { inclusive = true }
                                    }
                                }
                            }
                        }, shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = manageType)
                    }
                }
            }
        })
}

