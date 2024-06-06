package com.team4studio.travelnow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.team4studio.travelnow.view.navigation.NavigationGraph
import com.team4studio.travelnow.view.theme.EmerceTheme
import com.team4studio.travelnow.viewmodel.AppViewModel

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EmerceTheme {
                val context = LocalContext.current
                val appViewModel = viewModel<AppViewModel>(
                    viewModelStoreOwner = context as ComponentActivity
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavigationGraph(navController = rememberNavController())
                }
            }

        }
    }
}
