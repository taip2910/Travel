package com.team4studio.travelnow.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String = "",
    val icon: ImageVector = Icons.Default.Info
) {
    object Login : Screen(
        route = "login"
    )

    object Register : Screen(
        route = "register"
    )

    object Forgot : Screen(
        route = "forgotPassword"
    )

    object VerifyPassword : Screen(
        route = "verifyPassword"
    )

    object ResetPassword : Screen(
        route = "resetPassword"
    )

    object Home : Screen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

        object Search : Screen(
        route = "search",
        title = "Search",
        icon = Icons.Default.Home
    )

    object Cart : Screen(
        route = "cart",
        title = "Cart",
        icon = Icons.Default.ShoppingCart
    )

    object Profile : Screen(
        route = "profile",
        title = "Profile",
        icon = Icons.Default.Person
    )

    object OrderSummary : Screen(
        route = "orderSummary"
    )

    object Orders : Screen(
        route = "orders"
    )

    object OrderDetails : Screen(
        route = "orderDetails/{id}"
    )

    object MyAddresses : Screen(
        route = "myAddresses"
    )

    object ManageAddress : Screen(
        route = "manageAddress"
    )

    object AdminProductList : Screen(
        route = "adminProductList/{title}/{type}/{cid}",
    )

    object AdminReport : Screen(
        route = "adminReports",
        icon = Icons.Default.Analytics
    )

    object Dashboard : Screen(
        route = "dashboard",
        icon = Icons.Default.Dashboard
    )

    object FilterReport : Screen(
        route = "filterReports"
    )


    object ManageProduct : Screen(
        route = "manageProduct/{actionType}"
    )

    object ManageReview : Screen(
        route = "manageReview/{id}"
    )

    object Payment : Screen(
        route = "selectPayment"
    )

    object SelectAddress : Screen(
        route = "selectAddress"
    )

    object Product : Screen(
        route = "product/{id}"
    )

    object ProductList : Screen(
        route = "productList"
    )

    object ProductListSearch : Screen(
        route = "productListSearch/{param}"
    )

    object ProductListFilter : Screen(
        route = "FilterProducts",
        title = "Filter Products"
    )

    object DashboardDetailsPage : Screen(
        route = "detailsPage"
    )




}

