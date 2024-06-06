package com.team4studio.travelnow.view.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team4studio.travelnow.R
import com.team4studio.travelnow.model.remote.entity.Address
import com.team4studio.travelnow.view.components.AdminBottomBar
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.components.UserBottomBar
import com.team4studio.travelnow.view.navigation.Screen
import com.team4studio.travelnow.viewmodel.AddressViewModel
import com.team4studio.travelnow.viewmodel.AppViewModel
import com.team4studio.travelnow.viewmodel.PlaceOrderViewModel

@ExperimentalMaterial3Api
@Composable
fun Address(
    navController: NavHostController,
    screenMode: String,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    placeOrderViewModel: PlaceOrderViewModel = viewModel(LocalContext.current as ComponentActivity),
    addressViewModel: AddressViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    addressViewModel.setUser(appViewModel.getCurrentUserId())

    Scaffold(topBar = { TopBar("$screenMode Address", { navController.popBackStack() }) },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
            ) {
                val addresses = addressViewModel.addresses.value

                LazyColumn(
                    Modifier
                        .padding(10.dp)
                        .weight(6f, false),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    items(items = addresses, key = { address -> address.id }) { address ->
                        AddressCard(address = address,
                            mode = screenMode,
                            navController = navController,
                            setSelectedAddress = { placeOrderViewModel.selectedAddress = address })
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f), horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        navController.navigate("${Screen.ManageAddress.route}/$screenMode/-1")
                    }) {
                        Text("Add Address")
                    }
                }
            }
        },
        bottomBar = {
            if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
            else UserBottomBar(navController = navController)
        })
}

@ExperimentalMaterial3Api
@Composable
fun AddressCard(
    address: Address,
    mode: String,
    navController: NavHostController,
    setSelectedAddress: () -> Unit = {},
    addressViewModel: AddressViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    Card(
        onClick = {
            if (mode == "Select") {
                setSelectedAddress()
                navController.navigate(route = Screen.Payment.route)
            }
        },
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(20.dp)
    ) {

        Row(Modifier.padding(8.dp)) {
            val openRemoveAddressDialog = remember { mutableStateOf(false) }

            Image(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "pin",
                Modifier.size(35.dp)
            )

            Column(Modifier.padding(5.dp)) {
                Text(address.name)
                Text("Unit ${address.unit}, Building ${address.building}")
                Text("Street ${address.street}, Zone ${address.zone}")
                Text("PO Box: ${address.poBox}")
                Text(address.phone)
            }

            Row(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
            ) {
                Image(painter = painterResource(id = R.drawable.edit),
                    contentDescription = "Edit Address",
                    Modifier
                        .size(25.dp)
                        .padding(end = 5.dp)
                        .clickable {
                            navController.navigate("${Screen.ManageAddress.route}/$mode/${address.id}")
                        })
                Image(painter = painterResource(id = R.drawable.trash),
                    contentDescription = "Delete Address",
                    Modifier
                        .size(25.dp)
                        .padding(end = 5.dp)
                        .clickable { openRemoveAddressDialog.value = true })
            }

            if (openRemoveAddressDialog.value) {
                AlertDialog(onDismissRequest = { openRemoveAddressDialog.value = false }, title = {
                    Text(text = "Remove address")
                }, text = {
                    Text("Would you like to remove this address from your account?")
                }, confirmButton = {
                    Button(onClick = {
                        openRemoveAddressDialog.value = false
                        addressViewModel.deleteAddress(address)
                    }) {
                        Text("Yes")
                    }
                }, dismissButton = {
                    Button(onClick = {
                        openRemoveAddressDialog.value = false
                    }) {
                        Text("Cancel")
                    }
                })
            }
        }
    }
}
