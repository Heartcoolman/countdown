package com.miuix.countdown.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miuix.countdown.data.model.CountdownType
import com.miuix.countdown.utils.TimeCalculator
import com.miuix.countdown.viewmodel.CountdownViewModel
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import top.yukonga.miuix.kmp.basic.*
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun CountdownDetailScreen(
    countdownId: String,
    viewModel: CountdownViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: () -> Unit
) {
    LaunchedEffect(countdownId) {
        viewModel.selectCountdown(countdownId)
    }
    
    val countdown by viewModel.selectedCountdown.collectAsState()
    var currentTime by remember { mutableStateOf(Clock.System.now()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Clock.System.now()
            delay(100)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = countdown?.title ?: "",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("←", fontSize = 24.sp)
                    }
                }
            )
        }
    ) { paddingValues ->
        countdown?.let { item ->
            val remaining = TimeCalculator.calculateRemaining(item, currentTime)
            val progress = TimeCalculator.calculateProgress(item, currentTime)
            val isExpired = TimeCalculator.isExpired(item, currentTime)
            
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
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (item.type) {
                                    CountdownType.DATE -> "📅"
                                    CountdownType.TIMER -> "⏱️"
                                },
                                fontSize = 48.sp
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            if (isExpired || item.isCompleted) {
                                Text(
                                    text = "已完成！",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MiuixTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                )
                            } else if (item.isPaused) {
                                Text(
                                    text = "已暂停",
                                    fontSize = 24.sp,
                                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = remaining.formatDigital(),
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MiuixTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Text(
                                    text = remaining.formatDigital(),
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MiuixTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Text(
                                    text = remaining.format(),
                                    fontSize = 16.sp,
                                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                                    textAlign = TextAlign.Center
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp),
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Text(
                                text = "${(progress * 100).toInt()}% 已完成",
                                fontSize = 14.sp,
                                color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                            )
                        }
                    }
                }
                
                if (item.description.isNotEmpty()) {
                    item {
                        Card {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "描述",
                                    fontSize = 14.sp,
                                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.description,
                                    fontSize = 16.sp,
                                    color = MiuixTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                
                item {
                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "类型",
                                    fontSize = 14.sp,
                                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                                )
                                Text(
                                    text = when (item.type) {
                                        CountdownType.DATE -> "日期倒计时"
                                        CountdownType.TIMER -> "定时器"
                                    },
                                    fontSize = 14.sp,
                                    color = MiuixTheme.colorScheme.onSurface
                                )
                            }
                            
                            if (item.type == CountdownType.DATE && item.targetDate != null) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "目标日期",
                                        fontSize = 14.sp,
                                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                                    )
                                    val localDateTime = item.targetDate.toLocalDateTime(TimeZone.currentSystemDefault())
                                    Text(
                                        text = "${localDateTime.year}年${localDateTime.monthNumber}月${localDateTime.dayOfMonth}日 ${localDateTime.hour}:${localDateTime.minute.toString().padStart(2, '0')}",
                                        fontSize = 14.sp,
                                        color = MiuixTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "创建时间",
                                    fontSize = 14.sp,
                                    color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                                )
                                val localDateTime = item.createdAt.toLocalDateTime(TimeZone.currentSystemDefault())
                                Text(
                                    text = "${localDateTime.year}年${localDateTime.monthNumber}月${localDateTime.dayOfMonth}日",
                                    fontSize = 14.sp,
                                    color = MiuixTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
                
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (!isExpired && !item.isCompleted) {
                            Button(
                                onClick = { viewModel.togglePause(item) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text(if (item.isPaused) "继续" else "暂停")
                            }
                        }
                        
                        Button(
                            onClick = { showDeleteDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("删除")
                        }
                    }
                }
            }
        }
    }
    
    if (showDeleteDialog) {
        SuperDialog(
            title = "删除倒计时",
            summary = "确定要删除此倒计时吗？",
            show = remember { mutableStateOf(showDeleteDialog) },
            onDismissRequest = { showDeleteDialog = false }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { showDeleteDialog = false },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("取消")
                }
                Button(
                    onClick = {
                        viewModel.deleteCountdown(countdownId)
                        showDeleteDialog = false
                        onNavigateBack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("删除")
                }
            }
        }
    }
}

