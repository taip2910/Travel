package com.team4studio.travelnow.view.screens.product

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team4studio.travelnow.viewmodel.FilterViewModel

@Composable
fun QuantityFilterComponent(filterVM: FilterViewModel = viewModel(LocalContext.current as ComponentActivity)) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        QuantityField(
            modifier = Modifier.weight(1f),
            text = "Khách",
            value = filterVM.numberOfGuests,
            onValueChange = {filterVM.numberOfGuests=it},
            leadingIcon = {Icon(
                imageVector = Icons.Default.People,
                contentDescription = "Guests",
            ) },
        )
        QuantityField(
            modifier = Modifier.weight(1f),
            text = "Phòng",
            value = filterVM.numberOfRooms,
            onValueChange = {filterVM.numberOfRooms=it},
            leadingIcon = {Icon(
                imageVector = Icons.Default.Hotel,
                contentDescription = "Rooms",
            ) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuantityField(
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier

){
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier) {
        Text(text = text)
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            leadingIcon = { leadingIcon() },
            textStyle = TextStyle(fontSize = 18.sp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
        )
    }
}

@Composable
@Preview(showBackground = true )
fun QuantityFilterComponentPreview(){
    QuantityFilterComponent()
}