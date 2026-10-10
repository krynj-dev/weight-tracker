package au.com.krnyj.weighttracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import au.com.krnyj.weighttracker.Database
import au.com.krnyj.weighttracker.R
import au.com.krnyj.weighttracker.db.entity.LogEntry
import kotlinx.coroutines.runBlocking
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import kotlin.collections.listOf


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(modifier: Modifier = Modifier) {
    var openAddLogDialog by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        floatingActionButton =
            {
                ExtendedFloatingActionButton(
                    text = { Text("Log weight") },
                    onClick = { openAddLogDialog = true },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.secondary,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.add_box_24dp_e3e3e3_fill0_wght500_grad0_opsz24),
                            contentDescription = "Help"
                        )
                    }
                )
            }
    ) { innerPadding ->
        val dao = Database.getInstance().logEntryDao()
        val (logList, setLogList) = remember { mutableStateOf(runBlocking { dao.getAll() }) }
        LazyColumn() {
            items(logList) { log ->
                Surface(
                    modifier = modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                ) {
                    Text(log.measurement.setScale(2, RoundingMode.HALF_EVEN).toString())
                }
            }
        }
        when {
            openAddLogDialog -> {
                BasicAlertDialog(
                    onDismissRequest = { openAddLogDialog = false },
                    modifier = modifier,
                    content = {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        ) {
                            var showDatePicker by remember { mutableStateOf(false) }
                            val dateNow = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
                            val datePickerState = rememberDatePickerState(dateNow)
                            var weightError by remember { mutableStateOf<String?>(null) }
                            val selectedDate = datePickerState.selectedDateMillis?.let { it ->
                                convertMillisToDate(it)
                            } ?: SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(dateNow)
                            val weightState = rememberTextFieldState()
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Add log",
                                    textAlign = TextAlign.Left, modifier = Modifier.fillMaxWidth(),
                                )
                                OutlinedTextField(
                                    value = selectedDate,
                                    onValueChange = {},
                                    label = { Text("Weigh-in date") },
                                    readOnly = true,
                                    maxLines = 1,
                                    trailingIcon = {
                                        IconButton(onClick = { showDatePicker = !showDatePicker }) {
                                            Icon(
                                                painter = painterResource(R.drawable.event_24dp_e3e3e3_fill0_wght500_grad0_opsz24),
                                                contentDescription = "weigh date"
                                            )
                                        }
                                    }
                                )
                                OutlinedTextField(
                                    state = weightState,
                                    label = { Text("Weight") },
                                    lineLimits = TextFieldLineLimits.SingleLine,
                                    keyboardOptions = KeyboardOptions(
                                        autoCorrectEnabled = false,
                                        keyboardType = KeyboardType.Number,
                                        showKeyboardOnFocus = true,
                                        hintLocales = null
                                    ), isError = weightError != null,
                                    supportingText = {
                                        weightError?.let { Text(it) }
                                    }
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    TextButton(
                                        onClick = { openAddLogDialog = false }
                                    ) {
                                        Text("Cancel", color = MaterialTheme.colorScheme.error)
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    TextButton(
                                        onClick = {
                                            if (weightState.text.isEmpty()) {
                                                weightError = "Enter a value"
                                            } else {
                                                try {
                                                    val weight =
                                                        BigDecimal(weightState.text.toString())
                                                    weightError = null
                                                    openAddLogDialog = false
                                                    runBlocking {
                                                        dao.insertAll(
                                                            LogEntry(
                                                                0,
                                                                Instant.ofEpochMilli(datePickerState.selectedDateMillis!!),
                                                                weight
                                                            )
                                                        )
                                                        setLogList(dao.getAll())
                                                    }
                                                } catch (e: NumberFormatException) {
                                                    weightError = "Invalid number"
                                                }
                                            }

                                        }
                                    ) {
                                        Text("Add")
                                    }
                                }
                                when {
                                    showDatePicker -> {
                                        Popup(
                                            alignment = Alignment.TopStart,
                                            onDismissRequest = { showDatePicker = false }) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .offset(y = 64.dp)
                                                    .shadow(elevation = 4.dp)
                                                    .background(
                                                        MaterialTheme.colorScheme.surface
                                                    )
                                            ) {
                                                DatePicker(
                                                    state = datePickerState,
                                                    showModeToggle = false
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    })
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
