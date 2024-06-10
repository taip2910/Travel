package com.team4studio.travelnow.view.screens.admin.report

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team4studio.travelnow.model.remote.entity.Order
import com.team4studio.travelnow.model.remote.entity.Product
import com.team4studio.travelnow.model.remote.entity.TotalProductInfo
import com.team4studio.travelnow.view.components.AdminBottomBar
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.navigation.Screen
import com.team4studio.travelnow.view.theme.*
import com.team4studio.travelnow.viewmodel.OrderViewModel
import com.team4studio.travelnow.viewmodel.admin.DashboardVM
import com.team4studio.travelnow.viewmodel.admin.ReportVM
import kotlin.math.round

@Composable
fun AdminReportsInit(navController: NavHostController) {

    val adminDashboardVM = viewModel<DashboardVM>(LocalContext.current as ComponentActivity)
    val orderVM = viewModel<OrderViewModel>(LocalContext.current as ComponentActivity)
    val products = remember { adminDashboardVM.getAllProducts() }
    val reportVM = viewModel<ReportVM>(LocalContext.current as ComponentActivity)
    var orders = emptyList<Order>()
    reportVM.totalProductsByStatus = emptyList<TotalProductInfo>().toMutableList()

    if (reportVM.isFiltered && reportVM.filteredOrders.isEmpty()) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No orders within this range. Cannot generate report.")
            TextButton(onClick = {
                reportVM.isFiltered =
                    false; navController.navigate(Screen.AdminReport.route); reportVM.finalOrderStatusValue.value =
                "All"
            }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
                Text(text = "Generate default report", style = mediumTitle)
            }
            if (!reportVM.isFiltered) {
                orders = remember { orderVM.getAllOrders() }
                for (order in orders) {
                    reportVM.getProductCountByStatus(order)
                }
            }

        }

    } else {

        if (reportVM.isFiltered) { // not empty
            orders = reportVM.filteredOrders
            for (order in reportVM.filteredOrders) {
                reportVM.getProductCountByStatus(order)
            }
            when (reportVM.finalOrderStatusValue.value.lowercase()) {
                "Đang Xử Lý" -> {
                    reportVM.totalProductsCount.value = reportVM.totalProductsByStatus[0].count
                    reportVM.totalSales = reportVM.totalProductsByStatus[0].amount
                }
                "Đang Xác Nhận" -> {
                    reportVM.totalProductsCount.value = reportVM.totalProductsByStatus[1].count
                    reportVM.totalSales = reportVM.totalProductsByStatus[1].amount
                }
                "Đã Xác Nhận" -> {
                    reportVM.totalProductsCount.value = reportVM.totalProductsByStatus[2].count
                    reportVM.totalSales = reportVM.totalProductsByStatus[2].amount
                }
            }
        } else {
            orders = remember { orderVM.getAllOrders() }
            for (order in orders) {
                reportVM.getProductCountByStatus(order)
            }
        }

        AdminReports(navController = navController, products, orders)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReports(
    navController: NavHostController, allProducts: List<Product>, orders: List<Order>,
    reportVM: ReportVM = viewModel(LocalContext.current as ComponentActivity),
) {
    Scaffold(
        topBar = {
            TopBar("Báo Cáo", actions = {
                IconButton(onClick = {
                    navController.navigate(Screen.FilterReport.route)
                }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Localized description"
                    )
                }
            })
        },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {

                var isCollapsed by remember { mutableStateOf(true) }

                Text(

                    text = "${reportVM.totalProductsCount.value} Vé",
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    style = header
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row() {
                    Text(
                        text = "Ngày bắt đầu: ${reportVM.startDate.value}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Ngày kết thúc: ${reportVM.endDate.value}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row() {
                    Text(
                        text = "Tổng cộng $${round(reportVM.totalSales)}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Trạng thái: ${reportVM.finalOrderStatusValue.value}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle
                    )
                }
                if (!isCollapsed) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Đang xử lý", style = mediumTitle)
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${reportVM.totalProductsByStatus[0].count} vé",
                            style = smallTitle
                        )
                        Text(
                            text = "Số lượng $${round(reportVM.totalProductsByStatus[0].amount)}",
                            style = smallTitle
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Đang xác nhận", style = mediumTitle)
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${reportVM.totalProductsByStatus[1].count} vé",
                            style = smallTitle
                        )
                        Text(
                            text = "Số lượng $${round(reportVM.totalProductsByStatus[1].amount)}",
                            style = smallTitle
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Đã xác nhận", style = mediumTitle)
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${reportVM.totalProductsByStatus[2].count} vé",
                            style = smallTitle
                        )
                        Text(
                            text = "Số lượng $${round(reportVM.totalProductsByStatus[2].amount)}",
                            style = smallTitle
                        )
                    }

                }

                Spacer(modifier = Modifier.height(4.dp))

                if (reportVM.finalOrderStatusValue.value.lowercase() == "all") TextButton(onClick = {
                    isCollapsed = !isCollapsed
                }) {
                    Text(text = if (!isCollapsed) "Thu gọn" else "Bấm để xem chi tiết.")
                }

                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    contentPadding = PaddingValues(all = 4.dp)
                ) {
                    items(items = orders) { order ->
                        AdminReportCardInit(order = order, allProducts)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
                Divider()
            }
        },
        bottomBar = { AdminBottomBar(navController = navController) } //navController = navController
    )
}