package com.team4studio.travelnow.view.screens.product

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team4studio.travelnow.model.remote.entity.OrderItem
import com.team4studio.travelnow.model.remote.entity.Product
import com.team4studio.travelnow.viewmodel.FilterViewModel
import com.team4studio.travelnow.viewmodel.OrderViewModel
import com.team4studio.travelnow.viewmodel.admin.DashboardVM


@Composable
fun productTypes(type: String?, cid: String?): List<Product>? {
    val filterVM = viewModel<FilterViewModel>(LocalContext.current as ComponentActivity)
    val orderVM = viewModel<OrderViewModel>(LocalContext.current as ComponentActivity)
    val adminDashboardVM = viewModel<DashboardVM>(LocalContext.current as ComponentActivity)
    filterVM.reviewRange = 0f..5f; filterVM.steps = 4;
    Log.d("cid inside types", cid.toString())
    return if (cid == "-1") {
        when (type) {
            "Mới" -> newArrivals(filterVM)

            "Xếp Hạng Cao" -> {
                filterVM.reviewRange = 1f..5f; filterVM.steps = 3;
                topRanked(filterVM)
            }

            "Nổi Bật" -> {
                filterVM.reviewRange = 3f..5f; filterVM.steps = 1;
                trending(filterVM,  orderVM.getAllOrderItems())
            }
            //"Normal" -> allProducts(filterVM)
            else -> {
                val allProducts = adminDashboardVM.getAllProducts()
                searchedProducts(filterVM, products = allProducts, type.toString())
            }
        }

    } else {
        normalProducts(filterVM)
    }
}

//fun allProducts(fVM: FilterViewModel): List<Product>? {
//
//    return fVM.getNormalFilteredProducts(
//    )
//}

fun newArrivals(fVM: FilterViewModel): List<Product> {
    return fVM.getNewArrivalProducts(
    ) ?: emptyList()
}

fun normalProducts(fVM: FilterViewModel): List<Product>? {
    return fVM.getCategoryProducts(
    )
}

fun searchedProducts(fVM: FilterViewModel, products: List<Product>, title: String): List<Product> {

    return fVM.getSearchedProducts(title, products
    ) ?: emptyList()
}

fun trending(fVM: FilterViewModel,  orderItems: List<OrderItem>): List<Product> {
    return fVM.getTrendingProducts(
        orderItems
    ) ?: emptyList()
}

fun topRanked(fVM: FilterViewModel): List<Product> {
    return fVM.getTopRankedProducts(

    ) ?: emptyList()
}
