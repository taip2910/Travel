package com.team4studio.travelnow.view.screens.admin.dashboard

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team4studio.travelnow.viewmodel.admin.DashboardVM

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SegmentedButton() {
    val adminDashboardVM = viewModel<DashboardVM>(LocalContext.current as ComponentActivity)
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

        FilterChip(selected = true,
            onClick = { if(adminDashboardVM.selectedSortChip != 1) adminDashboardVM.selectedSortChip = 1 else adminDashboardVM.selectedSortChip = 0  },
            label = { Text("Status A-Z") },
            leadingIcon = if (adminDashboardVM.selectedSortChip == 1) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Localized Description",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                {
                    Icon(
                        imageVector = Icons.Filled.SortByAlpha,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            })
        Spacer(modifier = Modifier.width(10.dp))
        FilterChip(selected = true,
            onClick = { if(adminDashboardVM.selectedSortChip != 2) adminDashboardVM.selectedSortChip = 2 else adminDashboardVM.selectedSortChip = 0  },
            label = { Text("Date") },
            leadingIcon = if (adminDashboardVM.selectedSortChip == 2) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Localized Description",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            })
        Spacer(modifier = Modifier.width(10.dp))
        FilterChip(selected = true,
            onClick = { if(adminDashboardVM.selectedSortChip != 3) adminDashboardVM.selectedSortChip = 3 else adminDashboardVM.selectedSortChip = 0  },
            label = { Text("Total") },
            leadingIcon = if (adminDashboardVM.selectedSortChip == 3) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Localized Description",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                {
                    Icon(
                        imageVector = Icons.Filled.Money,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            })
    }
}