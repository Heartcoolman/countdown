package com.miuix.countdown.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miuix.countdown.data.model.CountdownItem
import com.miuix.countdown.data.model.CountdownType
import com.miuix.countdown.utils.TimeCalculator
import com.miuix.countdown.viewmodel.CountdownViewModel
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun HomeScreen(
    viewModel: CountdownViewModel,
    onNavigateToAllCountdowns: () -> Unit,
    onNavigateToAddEdit: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToStopwatch: () -> Unit,
    onNavigateToTimer: () -> Unit
) {
    val activeCountdowns by viewModel.activeCountdowns.collectAsState()
    var selectedBottomNav by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = "倒计时",
                actions = {
                    TextButton(
                        text = "查看全部",
                        onClick = onNavigateToAllCountdowns
                    )
                }
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.padding(16.dp)
            ) {
                Button(
                    onClick = onNavigateToAddEdit,
                    modifier = Modifier.size(56.dp)
                ) {
                    Text("＋", fontSize = 24.sp)
                }
            }
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
                    onClick = { },
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
                    onClick = onNavigateToTimer,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("定时器")
                }
            }
            
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                if (activeCountdowns.isEmpty()) {
                    EmptyState(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(activeCountdowns, key = { it.id }) { countdown ->
                            CountdownCard(
                                countdown = countdown,
                                onClick = { onNavigateToDetail(countdown.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CountdownCard(
    countdown: CountdownItem,
    onClick: () -> Unit
) {
    var currentTime by remember { mutableStateOf(Clock.System.now()) }
    
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Clock.System.now()
            delay(1000)
        }
    }
    
    val remaining = TimeCalculator.calculateRemaining(countdown, currentTime)
    val progress = TimeCalculator.calculateProgress(countdown, currentTime)
    
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = countdown.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MiuixTheme.colorScheme.onSurface
                    )
                    
                    if (countdown.description.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = countdown.description,
                            fontSize = 14.sp,
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                        )
                    }
                }
                
                Text(
                    text = when (countdown.type) {
                        CountdownType.DATE -> "📅"
                        CountdownType.TIMER -> "⏱️"
                    },
                    fontSize = 24.sp
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = remaining.formatDigital(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MiuixTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "${(progress * 100).toInt()}% 已完成",
                fontSize = 12.sp,
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary
            )
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📭",
            fontSize = 64.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "暂无进行中的倒计时",
            fontSize = 16.sp,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "点击右下角 ＋ 创建新的倒计时",
            fontSize = 14.sp,
            color = MiuixTheme.colorScheme.onSurfaceVariantSummary
        )
    }
}

