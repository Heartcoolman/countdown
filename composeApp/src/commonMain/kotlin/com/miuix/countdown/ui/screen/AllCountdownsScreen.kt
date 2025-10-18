package com.miuix.countdown.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import top.yukonga.miuix.kmp.extra.SuperDialog
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun AllCountdownsScreen(
    viewModel: CountdownViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToAddEdit: () -> Unit
) {
    val allCountdowns by viewModel.allCountdowns.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }
    var countdownToDelete by remember { mutableStateOf<CountdownItem?>(null) }
    
    val now = Clock.System.now()
    val activeCountdowns = allCountdowns.filter { it.isActive() && !TimeCalculator.isExpired(it, now) }
    val completedCountdowns = allCountdowns.filter { it.isCompleted || TimeCalculator.isExpired(it, now) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = "所有倒计时",
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (activeCountdowns.isNotEmpty()) {
                item {
                    Text(
                        text = "进行中 (${activeCountdowns.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(activeCountdowns, key = { it.id }) { countdown ->
                    CountdownListItem(
                        countdown = countdown,
                        onClick = { onNavigateToDetail(countdown.id) },
                        onDelete = {
                            countdownToDelete = countdown
                            showDeleteDialog = true
                        }
                    )
                }
            }
            
            if (completedCountdowns.isNotEmpty()) {
                item {
                    Text(
                        text = "已完成 (${completedCountdowns.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                
                items(completedCountdowns, key = { it.id }) { countdown ->
                    CountdownListItem(
                        countdown = countdown,
                        onClick = { onNavigateToDetail(countdown.id) },
                        onDelete = {
                            countdownToDelete = countdown
                            showDeleteDialog = true
                        }
                    )
                }
            }
            
            if (allCountdowns.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyState()
                    }
                }
            }
        }
    }
    
    if (showDeleteDialog && countdownToDelete != null) {
        SuperDialog(
            title = "删除倒计时",
            summary = "确定要删除「${countdownToDelete?.title}」吗？",
            show = remember { mutableStateOf(showDeleteDialog) },
            onDismissRequest = {
                showDeleteDialog = false
                countdownToDelete = null
            }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        countdownToDelete = null
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("取消")
                }
                Button(
                    onClick = {
                        countdownToDelete?.let { viewModel.deleteCountdown(it.id) }
                        showDeleteDialog = false
                        countdownToDelete = null
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("删除")
                }
            }
        }
    }
}

@Composable
fun CountdownListItem(
    countdown: CountdownItem,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var currentTime by remember { mutableStateOf(Clock.System.now()) }
    
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Clock.System.now()
            delay(1000)
        }
    }
    
    val remaining = TimeCalculator.calculateRemaining(countdown, currentTime)
    val isExpired = TimeCalculator.isExpired(countdown, currentTime)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = when (countdown.type) {
                            CountdownType.DATE -> "📅"
                            CountdownType.TIMER -> "⏱️"
                        },
                        fontSize = 20.sp
                    )
                    
                    Text(
                        text = countdown.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MiuixTheme.colorScheme.onSurface
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (isExpired || countdown.isCompleted) {
                    Text(
                        text = "已完成",
                        fontSize = 14.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                    )
                } else if (countdown.isPaused) {
                    Text(
                        text = "已暂停",
                        fontSize = 14.sp,
                        color = MiuixTheme.colorScheme.onSurfaceVariantSummary
                    )
                } else {
                    Text(
                        text = remaining.formatShort(),
                        fontSize = 14.sp,
                        color = MiuixTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            IconButton(
                onClick = onDelete
            ) {
                Text("🗑️", fontSize = 20.sp)
            }
        }
    }
}

