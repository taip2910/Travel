package com.team4studio.travelnow.view.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.team4studio.travelnow.R
import com.team4studio.travelnow.model.remote.entity.User
import com.team4studio.travelnow.view.components.AdminBottomBar
import com.team4studio.travelnow.view.components.TopBar
import com.team4studio.travelnow.view.components.UserBottomBar
import com.team4studio.travelnow.view.navigation.Screen
import com.team4studio.travelnow.viewmodel.AppViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun Profile(
    navController: NavHostController,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val profileMenuItemFontSize = 20.sp
    val currentUser: User? = appViewModel.getCurrentUser()

    Scaffold(topBar = { TopBar("My Profile") }, content = { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile Picture",
                    Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(15.dp))
                )
                Spacer(modifier = Modifier.padding(10.dp))
                Column() {
                    Text(
                        text = "${currentUser?.firstName} ${currentUser?.lastName}",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row() {
                        Text("Type: ")
                        Text("${currentUser?.type}", fontWeight = FontWeight.Bold)
                    }
                }

            }
            Divider()
            Text(
                "My Orders",
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(route = Screen.Orders.route)
                    }, fontSize = profileMenuItemFontSize
            )
            Text(
                "My Addresses",
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(route = Screen.MyAddresses.route)
                    }, fontSize = profileMenuItemFontSize
            )
            Box(
                Modifier.weight(1f), contentAlignment = BottomEnd
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Text(
                        "Logout",
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                appViewModel.setCurrentUserId("")
                                navController.navigate(route = Screen.Login.route) {
                                    popUpTo(0)
                                }
                            }, fontSize = profileMenuItemFontSize
                    )
                }
            }
        }
    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(
            navController = navController
        )
        else UserBottomBar(navController = navController)
    })
}