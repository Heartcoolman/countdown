package com.miuix.countdown.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miuix.countdown.data.model.TimeRemaining
import com.miuix.countdown.viewmodel.TimerViewModel
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun TimerScreen(
    viewModel: TimerViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToStopwatch: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedBottomNav by remember { mutableStateOf(2) }
    var showTimePicker by remember { mutableStateOf(state.durationMillis == 0L) }
    
    var hours by remember { mutableStateOf("0") }
    var minutes by remember { mutableStateOf("5") }
    var seconds by remember { mutableStateOf("0") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = "定时器"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 底部导航栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onNavigateToHome,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("首页")
                }
                Button(
                    onClick = onNavigateToStopwatch,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("秒表")
                }
                Button(
                    onClick = { },
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("定时器")
                }
            }
            if (showTimePicker) {
                // 时间选择器
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "设置倒计时时长",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MiuixTheme.colorScheme.onSurface
                            )
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextField(
                                    value = hours,
                                    onValueChange = { hours = it.filter { c -> c.isDigit() }.take(2) },
                                    label = "时",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                TextField(
                                    value = minutes,
                                    onValueChange = { minutes = it.filter { c -> c.isDigit() }.take(2) },
                                    label = "分",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                                TextField(
                                    value = seconds,
                                    onValueChange = { seconds = it.filter { c -> c.isDigit() }.take(2) },
                                    label = "秒",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                    
                    Text(
                        text = "快捷选择",
                        fontSize = 16.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                    )
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(viewModel.quickTimeOptions) { (timeSeconds, label) ->
                            Button(
                                onClick = {
                                    viewModel.setDurationFromSeconds(timeSeconds)
                                    showTimePicker = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                            ) {
                                Text(label)
                            }
                        }
                    }
                    
                    Button(
                        onClick = {
                            val h = hours.toLongOrNull() ?: 0L
                            val m = minutes.toLongOrNull() ?: 0L
                            val s = seconds.toLongOrNull() ?: 0L
                            if (h > 0 || m > 0 || s > 0) {
                                viewModel.setDuration(h.toInt(), m.toInt(), s.toInt())
                                showTimePicker = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("开始")
                    }
                }
            } else {
                // 定时器运行界面
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                progress = { viewModel.getProgress() },
                                modifier = Modifier.size(200.dp),
                                strokeWidth = 8.dp,
                            )
                            
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val timeRemaining = TimeRemaining.fromMillis(state.remainingMillis)
                                
                                if (state.isCompleted) {
                                    Text(
                                        text = "时间到！",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MiuixTheme.colorScheme.primary
                                    )
                                } else {
                                Text(
                                    text = String.format(
                                        "%02d:%02d:%02d",
                                        timeRemaining.hours,
                                        timeRemaining.minutes,
                                        timeRemaining.seconds
                                    ),
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (state.isRunning) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.onSurface
                                )
                                }
                            }
                        }
                    }
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (!state.isCompleted) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.reset() },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.dp),
                                    enabled = !state.isRunning
                                ) {
                                    Text("重置")
                                }
                                
                                Button(
                                    onClick = {
                                        if (state.isRunning) {
                                            viewModel.pause()
                                        } else {
                                            viewModel.start()
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(56.dp)
                                ) {
                                    Text(if (state.isRunning) "暂停" else "开始")
                                }
                            }
                        }
                        
                        Button(
                            onClick = {
                                viewModel.stop()
                                showTimePicker = true
                                hours = "0"
                                minutes = "5"
                                seconds = "0"
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(if (state.isCompleted) "再来一次" else "取消")
                        }
                    }
                }
            }
        }
    }
}

