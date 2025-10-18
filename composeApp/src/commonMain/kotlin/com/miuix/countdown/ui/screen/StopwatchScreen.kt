package com.miuix.countdown.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miuix.countdown.data.model.TimeRemaining
import com.miuix.countdown.viewmodel.StopwatchViewModel
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun StopwatchScreen(
    viewModel: StopwatchViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToTimer: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedBottomNav by remember { mutableStateOf(1) }
    val listState = rememberLazyListState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = "秒表"
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
                    onClick = { },
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("秒表")
                }
                Button(
                    onClick = onNavigateToTimer,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("定时器")
                }
            }
            // 时间显示区域
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val timeRemaining = TimeRemaining.fromMillis(state.elapsedMillis)
                    
                    Text(
                        text = String.format(
                            "%02d:%02d:%02d.%03d",
                            timeRemaining.hours,
                            timeRemaining.minutes,
                            timeRemaining.seconds,
                            timeRemaining.milliseconds
                        ),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = if (state.isRunning) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.onSurface,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
            
            // 控制按钮
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.reset() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    enabled = !state.isRunning && state.elapsedMillis > 0
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
                
                Button(
                    onClick = { viewModel.lap() },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    enabled = state.isRunning
                ) {
                    Text("记圈")
                }
            }
            
            // 记圈列表
            if (state.laps.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        reverseLayout = true
                    ) {
                        items(state.laps.reversed(), key = { it.lapNumber }) { lap ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "圈 ${lap.lapNumber}",
                                    fontSize = 16.sp,
                                    color = MiuixTheme.colorScheme.onSurface
                                )
                                
                                Column(
                                    horizontalAlignment = Alignment.End
                                ) {
                                    val lapTime = TimeRemaining.fromMillis(lap.lapTime)
                                    Text(
                                        text = String.format(
                                            "%02d:%02d.%03d",
                                            lapTime.minutes + lapTime.hours * 60,
                                            lapTime.seconds,
                                            lapTime.milliseconds
                                        ),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = MiuixTheme.colorScheme.primary
                                    )
                                    
                                    val totalTime = TimeRemaining.fromMillis(lap.totalTime)
                                    Text(
                                        text = String.format(
                                            "总计 %02d:%02d.%03d",
                                            totalTime.minutes + totalTime.hours * 60,
                                            totalTime.seconds,
                                            totalTime.milliseconds
                                        ),
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}



