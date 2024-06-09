package com.team4studio.travelnow.view.screens.productFilter

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.team4studio.travelnow.model.remote.entity.Category
import com.team4studio.travelnow.view.screens.home.CategoryFilterChips
import com.team4studio.travelnow.viewmodel.FilterViewModel

@Composable
fun CategoryFilterComponent(filterVM: FilterViewModel = viewModel(
    LocalContext.current as ComponentActivity
), categoryList: List<Category>
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Category",
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            softWrap = true,
            overflow = TextOverflow.Clip,
        )
        TextButton(onClick = {
            filterVM.setCurrentIndex("-1")
        }) { Text("Reset") }
    }

    FlowRow(
        mainAxisSpacing = 5.dp,
        mainAxisAlignment = FlowMainAxisAlignment.Start,
    ) {
        for (c in categoryList) {
            CategoryFilterChips(category = c, { })
        }
    }

}