package com.team4studio.travelnow.view.components

import androidx.activity.ComponentActivity
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team4studio.travelnow.viewmodel.OrderViewModel
import com.team4studio.travelnow.viewmodel.admin.ManageProductVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(options: List<String>, text: String, type: String, defaultOption: String) {
    val orderVM = viewModel<OrderViewModel>(LocalContext.current as ComponentActivity)
    val manageProductVM = viewModel<ManageProductVM>(LocalContext.current as ComponentActivity)

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { (mutableStateOf(defaultOption)) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text(text) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEachIndexed() { index, selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        when (type) {
                            "category" -> manageProductVM.cid = (index + 1).toString()
                            "orders" -> {
                                orderVM.orderStatus.value = selectionOption
                                orderVM.isOrderUpdated.value = true
                            }
                        }
                        expanded = false
                        selectedOptionText = selectionOption
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}