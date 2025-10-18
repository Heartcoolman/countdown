package com.miuix.countdown.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miuix.countdown.data.model.CountdownType
import com.miuix.countdown.viewmodel.CountdownViewModel
import kotlinx.datetime.*
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.extra.SuperDropdown
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun AddEditCountdownScreen(
    viewModel: CountdownViewModel,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var countdownType by remember { mutableStateOf(CountdownType.DATE) }
    var showTypeDropdown by remember { mutableStateOf(false) }
    
    // 日期倒计时
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    var yearText by remember { mutableStateOf(now.year.toString()) }
    var monthText by remember { mutableStateOf(now.monthNumber.toString()) }
    var dayText by remember { mutableStateOf(now.dayOfMonth.toString()) }
    var hourText by remember { mutableStateOf("0") }
    var minuteText by remember { mutableStateOf("0") }
    
    // 定时器
    var timerHours by remember { mutableStateOf("0") }
    var timerMinutes by remember { mutableStateOf("5") }
    var timerSeconds by remember { mutableStateOf("0") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = "创建倒计时",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←", fontSize = 24.sp)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextField(
                            value = title,
                            onValueChange = { title = it },
                            label = "标题",
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        TextField(
                            value = description,
                            onValueChange = { description = it },
                            label = "描述（可选）",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            
            item {
                Card(
                    onClick = { showTypeDropdown = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "倒计时类型",
                            fontSize = 16.sp,
                            color = MiuixTheme.colorScheme.onSurface
                        )
                        Text(
                            text = when (countdownType) {
                                CountdownType.DATE -> "日期倒计时"
                                CountdownType.TIMER -> "定时器"
                            },
                            fontSize = 16.sp,
                            color = MiuixTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            if (countdownType == CountdownType.DATE) {
                item {
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "目标日期",
                                fontSize = 16.sp,
                                color = MiuixTheme.colorScheme.onSurface
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextField(
                                    value = yearText,
                                    onValueChange = { 
                                        if (it.isEmpty() || it.all { c -> c.isDigit() }) {
                                            yearText = it
                                        }
                                    },
                                    label = "年",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                TextField(
                                    value = monthText,
                                    onValueChange = { 
                                        if (it.isEmpty() || it.all { c -> c.isDigit() }) {
                                            monthText = it
                                        }
                                    },
                                    label = "月",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                TextField(
                                    value = dayText,
                                    onValueChange = { 
                                        if (it.isEmpty() || it.all { c -> c.isDigit() }) {
                                            dayText = it
                                        }
                                    },
                                    label = "日",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextField(
                                    value = hourText,
                                    onValueChange = { 
                                        if (it.isEmpty() || it.all { c -> c.isDigit() }) {
                                            hourText = it
                                        }
                                    },
                                    label = "时",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                TextField(
                                    value = minuteText,
                                    onValueChange = { 
                                        if (it.isEmpty() || it.all { c -> c.isDigit() }) {
                                            minuteText = it
                                        }
                                    },
                                    label = "分",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "倒计时时长",
                                fontSize = 16.sp,
                                color = MiuixTheme.colorScheme.onSurface
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextField(
                                    value = timerHours,
                                    onValueChange = { timerHours = it.filter { c -> c.isDigit() } },
                                    label = "时",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                TextField(
                                    value = timerMinutes,
                                    onValueChange = { timerMinutes = it.filter { c -> c.isDigit() } },
                                    label = "分",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                TextField(
                                    value = timerSeconds,
                                    onValueChange = { timerSeconds = it.filter { c -> c.isDigit() } },
                                    label = "秒",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
            
            item {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            when (countdownType) {
                                CountdownType.DATE -> {
                                    try {
                                        val year = yearText.toIntOrNull() ?: return@Button
                                        val month = monthText.toIntOrNull() ?: return@Button
                                        val day = dayText.toIntOrNull() ?: return@Button
                                        val hour = hourText.toIntOrNull() ?: 0
                                        val minute = minuteText.toIntOrNull() ?: 0
                                        
                                        val targetDateTime = LocalDateTime(
                                            year,
                                            month,
                                            day,
                                            hour,
                                            minute
                                        )
                                        val targetInstant = targetDateTime.toInstant(TimeZone.currentSystemDefault())
                                        
                                        viewModel.addCountdown(
                                            title = title,
                                            description = description,
                                            type = CountdownType.DATE,
                                            targetDate = targetInstant
                                        )
                                        onNavigateBack()
                                    } catch (e: Exception) {
                                        // 日期无效，不做任何操作
                                    }
                                }
                                CountdownType.TIMER -> {
                                    val hours = timerHours.toLongOrNull() ?: 0L
                                    val minutes = timerMinutes.toLongOrNull() ?: 0L
                                    val seconds = timerSeconds.toLongOrNull() ?: 0L
                                    val totalMillis = (hours * 3600 + minutes * 60 + seconds) * 1000
                                    
                                    if (totalMillis > 0) {
                                        viewModel.addCountdown(
                                            title = title,
                                            description = description,
                                            type = CountdownType.TIMER,
                                            durationMillis = totalMillis
                                        )
                                        onNavigateBack()
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("保存")
                }
            }
        }
    }
    
    if (showTypeDropdown) {
        SuperDropdown(
            selectedIndex = if (countdownType == CountdownType.DATE) 0 else 1,
            title = "选择类型",
            items = listOf("日期倒计时", "定时器"),
            onSelectedIndexChange = { index ->
                countdownType = if (index == 0) CountdownType.DATE else CountdownType.TIMER
                showTypeDropdown = false
            }
        )
    }
}

