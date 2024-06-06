package com.team4studio.travelnow.view.screens.admin.report

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team4studio.travelnow.model.remote.entity.Order
import com.team4studio.travelnow.model.remote.entity.Product
import com.team4studio.travelnow.view.theme.largeTitle
import com.team4studio.travelnow.view.theme.mediumTitle
import com.team4studio.travelnow.view.theme.smallCaption
import com.team4studio.travelnow.view.theme.smallTitle
import com.team4studio.travelnow.viewmodel.admin.ReportVM

@Composable
fun AdminReportCardInit(order: Order, allProducts: List<Product>) {
    AdminReportCard(order, allProducts)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportCard(order: Order, allProducts: List<Product>) {
    val reportVM = viewModel<ReportVM>(LocalContext.current as ComponentActivity)

    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        onClick = {},
        shape = CardDefaults.outlinedShape
    ) {

        Column(Modifier.padding(10.dp)) {
            Text(
                text = "Order# ${order.id}",
//                    text = "Order# ${if (order.id.isNotEmpty()) order.id.substring(0, 3) else ""}",
                style = largeTitle
            )
            Text(
                text = "Status ${order.status}",
//                    text = "Order# ${if (order.id.isNotEmpty()) order.id.substring(0, 3) else ""}",
                style = mediumTitle
            )
            Spacer(Modifier.height(5.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "${order.date}", style = smallTitle
                )
                Text(
                    text = "Total $${order.total}", style = smallTitle
                )
            }
            Divider(color=MaterialTheme.colorScheme.inversePrimary)
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Product xQuantity", style = smallCaption)
                Text(text = "Price", style = smallCaption)
            }
            Spacer(modifier = Modifier.height(10.dp))

            for (items in order.items ?: emptyList()) {
                val product = reportVM.getProductDetails(items.pid, allProducts)
                val strList = product?.title?.split(" ")
                if (strList.isNullOrEmpty()) continue
                val str =
                    if (strList.isEmpty() && strList.size!! < 3) product.title else strList.subList(
                        0, 3
                    ).joinToString(separator = " ")

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//                    Text(
//                        text = if (product.id.isNotEmpty()) product.id.substring(0, 3) else "",
//                        style = smallTitle
//                    )
                    Row() {
                        Text(text = str, style = smallTitle)
                        Text(
                            text = " x" + items.quantity, style = smallTitle
                        )
                    }
                    Text(text = "$${product.price}", style = smallTitle)
                }
            }
        }


//        Column(
//            Modifier
//                .padding(12.dp)
//                .fillMaxWidth()
//        ) {
//            Text(
//                text = "Product Name: YYYY",
//                fontWeight = FontWeight.Bold,
//                fontSize = 18.sp,
//                //modifier = Modifier.padding(horizontal = 5.dp),
//                softWrap = true,
//                overflow = TextOverflow.Clip,
//            )
//            Text(
//                text = "Price: 200$",
//                //fontWeight = FontWeight.Bold,
//                //modifier = Modifier.padding(horizontal = 5.dp),
//                softWrap = true,
//                overflow = TextOverflow.Clip,
//            )
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
//
//            ) {
//                Text(
//                    text = "Ordered Date: 12/01/2002",
//                    //fontWeight = FontWeight.Bold,
//
//                    softWrap = true,
//                    overflow = TextOverflow.Clip,
//                )
//                Spacer(Modifier.width(10.dp))
//                Text(
//                    text = "Status: Progressing",
//                    //fontWeight = FontWeight.Bold,
//
//                    softWrap = true,
//                    overflow = TextOverflow.Clip,
//                )
//            }
//        }
    }
}
