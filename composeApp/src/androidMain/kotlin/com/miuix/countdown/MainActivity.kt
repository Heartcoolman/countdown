package com.miuix.countdown

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miuix.countdown.data.repository.CountdownRepositoryImpl
import com.miuix.countdown.ui.components.MiuiPermissionGuideDialog
import com.miuix.countdown.ui.components.PermissionGuideItem
import com.miuix.countdown.utils.MiuiPermissionHelper
import com.miuix.countdown.viewmodel.CountdownViewModel

class MainActivity : ComponentActivity() {
    
    companion object {
        private const val PREFS_NAME = "miui_guide_prefs"
        private const val KEY_GUIDE_SHOWN = "guide_shown"
    }
    
    private lateinit var prefs: SharedPreferences
    
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // 权限已授予
        } else {
            // 权限被拒绝，可能需要引导用户手动开启
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        // 请求通知权限（Android 13+）
        requestNotificationPermission()
        
        setContent {
            val repository = CountdownRepositoryImpl(applicationContext)
            val viewModel: CountdownViewModel = viewModel {
                CountdownViewModel(repository)
            }
            
            // 小米设备权限引导
            val showMiuiGuide = remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                if (MiuiPermissionHelper.isMiuiDevice() && !prefs.getBoolean(KEY_GUIDE_SHOWN, false)) {
                    showMiuiGuide.value = true
                }
            }
            
            if (showMiuiGuide.value) {
                MiuiPermissionGuideDialog(
                    show = showMiuiGuide,
                    items = buildMiuiGuideItems(),
                    onDismiss = {
                        showMiuiGuide.value = false
                        prefs.edit().putBoolean(KEY_GUIDE_SHOWN, true).apply()
                    }
                )
            }
            
            App(countdownViewModel = viewModel)
        }
    }
    
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    
    private fun buildMiuiGuideItems(): List<PermissionGuideItem> {
        val items = mutableListOf<PermissionGuideItem>()
        
        // 1. 自启动权限
        items.add(
            PermissionGuideItem(
                title = "1. 开启自启动",
                description = "允许应用在后台自动启动，确保倒计时提醒不被错过",
                actionText = "去设置",
                onAction = { MiuiPermissionHelper.openMiuiAutoStartSettings(this) }
            )
        )
        
        // 2. 电池优化
        if (!MiuiPermissionHelper.isIgnoringBatteryOptimizations(this)) {
            items.add(
                PermissionGuideItem(
                    title = "2. 关闭电池优化",
                    description = "防止系统在省电时杀掉应用，保证倒计时准确性",
                    actionText = "去设置",
                    onAction = { MiuiPermissionHelper.requestBatteryOptimizationExemption(this) }
                )
            )
        }
        
        // 3. 精确闹钟权限（Android 12+）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && 
            !MiuiPermissionHelper.canScheduleExactAlarms(this)) {
            items.add(
                PermissionGuideItem(
                    title = "3. 精确闹钟权限",
                    description = "允许应用在准确的时间发送提醒",
                    actionText = "去设置",
                    onAction = { MiuiPermissionHelper.openAlarmPermissionSettings(this) }
                )
            )
        }
        
        // 4. 通知权限
        items.add(
            PermissionGuideItem(
                title = "4. 允许通知",
                description = "接收倒计时结束通知和提醒",
                actionText = "去设置",
                onAction = { MiuiPermissionHelper.openNotificationSettings(this) }
            )
        )
        
        return items
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // Preview implementation
}

