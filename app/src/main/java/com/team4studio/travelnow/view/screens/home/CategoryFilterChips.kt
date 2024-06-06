package com.team4studio.travelnow.view.screens.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team4studio.travelnow.model.remote.entity.Category
import com.team4studio.travelnow.viewmodel.FilterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilterChips(
    category: Category,
    navToProductList: () -> Unit,
    // navController: NavController,
    //onClick: () -> Unit, //filterViewModel: FilterViewModel
    filterVM: FilterViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    //  productVM: ProductViewModel = viewModel(  //filterViewModel: FilterViewModel
    //        LocalContext.current as ComponentActivity


    Row(
        modifier = Modifier
            .height(45.dp), horizontalArrangement = Arrangement.Center
    ) {
        FilterChip(
            modifier = Modifier.height(40.dp),
            selected = filterVM.isIndexMatching(category.cid.toString()),
            colors = FilterChipDefaults.filterChipColors(
                labelColor = Color.Black,
                selectedContainerColor = Color.Black,
                selectedLabelColor = Color.White,
                disabledSelectedContainerColor = Color.White

            ),
            onClick = { filterVM.setCurrentIndex(category.cid.toString()); navToProductList() }, //filterViewModel.setCurrentIndex(category.id); //{ onClick; navToProductList();  }
            label = { Text(category.name); }
        )
    }
}

