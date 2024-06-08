package com.team4studio.travelnow.view.screens.productFilter

import android.app.DatePickerDialog
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team4studio.travelnow.viewmodel.FilterViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale



@Composable
fun DateFilterComponent(filterVM: FilterViewModel = viewModel(LocalContext.current as ComponentActivity)) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        DatePickerDialog(
            text = "Nhận phòng",
            value = filterVM.selectedDateCheckIn,
            onValueChange = { filterVM.selectedDateCheckIn = it },
            contentDescription = "Check In",
            modifier = Modifier.clickable {
                coroutineScope.launch {
                    filterVM.showDatePickerDialog(context, true) { selectedDate ->
                        filterVM.selectedDateCheckIn = selectedDate
                        filterVM.showDateCheckIn = false
                    }
                }
            }
        )
        DatePickerDialog(
            text = "Trả Phòng",
            value = filterVM.selectedDateCheckOut,
            onValueChange = {filterVM.selectedDateCheckOut =it},
            contentDescription = "Check Out",
            modifier = Modifier.clickable {
                coroutineScope.launch {
                    filterVM.showDatePickerDialog(context, true) { selectedDate ->
                        filterVM.selectedDateCheckOut = selectedDate
                        filterVM.showDateCheckOut = false
                    }
                }
            }
        )
    }
}


//fun showDatePickerDialog(context: Context, showDialog: Boolean, onDateSelected: (String) -> Unit) {
//    val format = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("en", "VN"))
//
//    if (showDialog) {
//        val calendar = Calendar.getInstance()
//        DatePickerDialog(
//            context,
//            { _, year, month, dayOfMonth ->
//                val selectedCalendar = Calendar.getInstance().apply {
//                    set(Calendar.YEAR, year)
//                    set(Calendar.MONTH, month)
//                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
//                }
//                onDateSelected(format.format(selectedCalendar.time))
//            },
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH)
//        ).show()
//    }
//}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    contentDescription: String,
    modifier: Modifier
    ){
    Text(text = text)
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(
            Icons.Default.DateRange,
            contentDescription = contentDescription,
            modifier = modifier
        ) },
        textStyle = TextStyle(fontSize = 18.sp),
    )
}

@Composable
@Preview(showBackground = true)
fun DateFilterComponentPreview() {
    DateFilterComponent()
}



