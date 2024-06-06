package com.team4studio.travelnow.view.screens.cart

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.components.UserBottomBar
import com.team4studio.travelnow.view.navigation.Screen
import com.team4studio.travelnow.viewmodel.AppViewModel
import com.team4studio.travelnow.viewmodel.CartViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.team4studio.travelnow.R
import com.team4studio.travelnow.view.components.AdminBottomBar

@ExperimentalMaterial3Api
@Composable
fun ShoppingCart(
    navController: NavHostController,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    viewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    viewModel.setUser(
        viewModel<AppViewModel>(LocalContext.current as ComponentActivity).getCurrentUser(),
    )

    Scaffold(topBar = { TopBar("My Cart", { navController.popBackStack() }) },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
            ) {
                val userCart = viewModel.userCart.value

                if (userCart.isEmpty()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.empty_cart),
                            contentDescription = "Empty cart image",
                            Modifier.size(250.dp)
                        )
                        Text("Your cart is empty", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "You haven't added any items to your cart.",
                            Modifier.padding(bottom = 10.dp)
                        )
                        Button(onClick = { navController.navigate(Screen.Home.route) }) {
                            Text("Browse")
                        }
                    }
                } else {
                    LazyColumn(Modifier.weight(4f)) {
                        items(items = userCart, key = { userCart -> userCart.cart.id }) {
                            CartItemCard(
                                it, viewModel
                            ) { navController.navigate("product/${it.product.id}") }
                        }
                    }

                    Divider(Modifier.padding(top = 10.dp))

                    Column(
                        Modifier.padding(start = 10.dp, end = 10.dp)
                    ) {
                        val totalPrice = viewModel.totalPrice.value
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", fontSize = 16.sp)
                            Text("$$totalPrice", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Discount:", fontSize = 16.sp)
                            Text(
                                "${viewModel.discount}%",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(bottom = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Grand Total:", fontSize = 16.sp)
                            Text(
                                "$${viewModel.grandTotal.value}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Button(
                            onClick = { navController.navigate(Screen.SelectAddress.route) },
                            Modifier.fillMaxWidth(),
                        ) {
                            Text(text = "CHECKOUT", fontSize = 18.sp)
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
            else UserBottomBar(navController = navController)
        })
}