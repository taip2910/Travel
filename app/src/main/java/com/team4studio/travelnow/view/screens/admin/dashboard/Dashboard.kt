package com.team4studio.travelnow.view.screens.admin.dashboard

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team4studio.travelnow.model.remote.entity.Order
import com.team4studio.travelnow.model.remote.entity.Product
import com.team4studio.travelnow.view.components.AdminBottomBar
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.navigation.Screen
import com.team4studio.travelnow.view.theme.largeTitle
import com.team4studio.travelnow.viewmodel.OrderViewModel
import com.team4studio.travelnow.viewmodel.admin.DashboardVM
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardInit(navController: NavHostController) {
    val adminDashboardVM = viewModel<DashboardVM>(LocalContext.current as ComponentActivity)
    val orderVM = viewModel<OrderViewModel>(LocalContext.current as ComponentActivity)


    val allOrders = orderVM.getAllOrders()
    val totalOrders = allOrders.size


    val totalSales = adminDashboardVM.getTotalSales()
    val allProducts = remember { adminDashboardVM.getAllProducts() }
    adminDashboardVM.products
    adminDashboardVM.allOrders = allOrders.toMutableList()
    val latestOrders = adminDashboardVM.getLatestOrders()
    val latestOrdersSubList = if(latestOrders.size < 10) latestOrders else latestOrders.subList(allOrders.size - 10, allOrders.size - 1)

    Dashboard(navController, latestOrdersSubList, totalOrders, totalSales, allProducts)
}

@ExperimentalMaterial3Api
@Composable
fun Dashboard(
    navController: NavHostController,
    orderData: List<Order>,
    totalOrders: Int,
    totalSales: Double,
    products: List<Product>
) {
    Scaffold(topBar = { TopBar("Dashboard") }, content = { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatsCard(Icons.Filled.ListAlt, "Total Orders", "$totalOrders") {
                    navController.navigate(
                        "detailsPage/orders/total orders"
                    )
                }
                StatsCard(Icons.Filled.Payments, "Total Sales", "$${round(totalSales)}") {
                    navController.navigate(
                        "detailsPage/sales/Total sales"
                    )
                }
                StatsCard(Icons.Filled.Store, "Total Products", "${products.size}") {
                    navController.navigate(
                        Screen.AdminProductList.route
                    )
                }
            }
            Divider()

            Text(
                text = "Latest Orders",
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .offset(x = 12.dp),
                textAlign = TextAlign.Left,
                style = largeTitle
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                SegmentedButton()
            }
            Divider()

            LazyColumn(
                contentPadding = PaddingValues(all = 10.dp)
            ) {
                items(items = orderData) { order ->
                    LatestOrderCard(navController, order = order)
                }
            }
        }
    },

        bottomBar = { AdminBottomBar(navController = navController) } //navController = navController
    )
}
