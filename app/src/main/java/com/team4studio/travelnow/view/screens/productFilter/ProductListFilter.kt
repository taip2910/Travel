package com.team4studio.travelnow.view.screens.productFilter

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team4studio.travelnow.model.remote.entity.Category
import com.team4studio.travelnow.model.remote.entity.Product
import com.team4studio.travelnow.view.components.AdminBottomBar
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.components.UserBottomBar
import com.team4studio.travelnow.view.screens.product.productTypes
import com.team4studio.travelnow.viewmodel.AppViewModel
import com.team4studio.travelnow.viewmodel.FilterViewModel
import com.team4studio.travelnow.viewmodel.HomeViewModel

@Composable
fun ProductListFilterInit(
    navController: NavHostController,
    category_id: String? ,
    productType: String?,
) {
    val homeVM = viewModel<HomeViewModel>(LocalContext.current as ComponentActivity)
    val categoryList = remember { homeVM.getAllCategories() }
    Log.d("category", "$categoryList")

    ProductListFilter(navController, category_id, productType, categoryList, false)
}

@Composable
@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
fun ProductListFilter(
    navController: NavHostController,
    category_id: String? ,
    productType: String?,
    categoryList: List<Category>,
    isCategoryChip: Boolean,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity)
) {


    var filterVM = viewModel<FilterViewModel>(LocalContext.current as ComponentActivity)
    Log.d("category id", category_id!!)
    val scope = rememberCoroutineScope()
    val skipHalfExpanded by remember { mutableStateOf(false) }
    val state = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = skipHalfExpanded
    )
    Log.d("type", "$productType")
    Log.d("cid", category_id)
    var navRoute = remember {
        "productList/${productType}/$productType/$category_id/1"
    }

    val result: List<Product>? = productTypes(type = productType, cid = category_id)
    Log.d("type", "$result")
    result?.let {
        viewModel<HomeViewModel>(LocalContext.current as ComponentActivity).setProductList(it)
    }

    ModalBottomSheetLayout(sheetState = state, sheetContent = {
        PriceFilterBottomSheet()
    }) {

        Scaffold(topBar = {
            TopBar("Filter $productType", { navController.popBackStack() }) {
                TextButton(onClick = {
                    if (category_id == "-1" && categoryList.contains(Category(name = productType!!))) {
                        navRoute = "ProductList/All products/All products/$category_id/1"
                    }
                    else navRoute = "ProductListSearch/${filterVM.nameLocation}"

                    navController.navigate(navRoute) {
                        popUpTo("home")

                    }
                }) {

                    Text(
                        text = "APPLY",
                        Modifier.padding(end = 5.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                }
            }
        }, content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                NameFilterComponent()
                //DateFilterComponent()
                //QuantityFilterComponent()
                PriceFilterComponent(scope = scope, state = state)
                ReviewFilterComponent()
            }
        }, bottomBar = {
            if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
            else UserBottomBar(navController = navController)
        })
    }
}