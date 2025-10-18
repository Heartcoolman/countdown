package com.miuix.countdown.utils

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.getSystemService

/**
 * 小米/澎湃OS 权限管理辅助类
 * 参考: https://dev.mi.com/xiaomihyperos/app-develop
 */
object MiuiPermissionHelper {
    
    /**
     * 检查是否是小米或红米设备
     */
    fun isMiuiDevice(): Boolean {
        val manufacturer = Build.MANUFACTURER.lowercase()
        val brand = Build.BRAND.lowercase()
        return manufacturer.contains("xiaomi") || 
               manufacturer.contains("redmi") || 
               brand.contains("xiaomi") || 
               brand.contains("redmi") ||
               brand.contains("mi")
    }
    
    /**
     * 检查是否已关闭电池优化
     */
    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = context.getSystemService<PowerManager>()
            return powerManager?.isIgnoringBatteryOptimizations(context.packageName) ?: false
        }
        return true
    }
    
    /**
     * 引导用户关闭电池优化
     */
    fun requestBatteryOptimizationExemption(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isIgnoringBatteryOptimizations(activity)) {
                try {
                    val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                        data = Uri.parse("package:${activity.packageName}")
                    }
                    activity.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                    // 如果失败，跳转到电池设置页面
                    openBatterySettings(activity)
                }
            }
        }
    }
    
    /**
     * 打开电池设置页面
     */
    private fun openBatterySettings(activity: Activity) {
        try {
            val intent = Intent(Settings.ACTION_SETTINGS)
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * 引导用户开启自启动权限（小米设备）
     */
    fun openMiuiAutoStartSettings(activity: Activity) {
        if (!isMiuiDevice()) return
        
        try {
            // 尝试直接打开自启动设置页面
            val intent = Intent().apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                
                // MIUI 自启动设置页面的多个可能路径
                when {
                    // HyperOS / MIUI 14+
                    tryComponent(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    ) -> {}
                    // MIUI 12/13
                    tryComponent(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity"
                    ) -> {}
                    // 旧版 MIUI
                    tryComponent(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartDetailManagementActivity"
                    ) -> {
                        putExtra("packageName", activity.packageName)
                    }
                    // 如果都不行，打开应用详情页
                    else -> {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", activity.packageName, null)
                    }
                }
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            // 最后的fallback：打开应用详情页
            openAppDetailsSettings(activity)
        }
    }
    
    private fun Intent.tryComponent(pkg: String, cls: String): Boolean {
        return try {
            component = ComponentName(pkg, cls)
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 打开应用详情设置页面
     */
    fun openAppDetailsSettings(activity: Activity) {
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", activity.packageName, null)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * 打开通知设置页面
     */
    fun openNotificationSettings(activity: Activity) {
        try {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
                }
            } else {
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
            }
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * 检查精确闹钟权限（Android 12+）
     */
    fun canScheduleExactAlarms(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService<android.app.AlarmManager>()
            return alarmManager?.canScheduleExactAlarms() ?: false
        }
        return true
    }
    
    /**
     * 引导用户开启精确闹钟权限
     */
    fun openAlarmPermissionSettings(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
                activity.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * 获取设备型号信息（用于日志和调试）
     */
    fun getDeviceInfo(): String {
        return buildString {
            append("设备: ${Build.MANUFACTURER} ${Build.BRAND} ${Build.MODEL}\n")
            append("系统: Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})\n")
            if (isMiuiDevice()) {
                append("MIUI/HyperOS: ${getMiuiVersion()}\n")
            }
        }
    }
    
    /**
     * 获取 MIUI/HyperOS 版本
     */
    private fun getMiuiVersion(): String {
        return try {
            val properties = Class.forName("android.os.SystemProperties")
            val get = properties.getMethod("get", String::class.java)
            get.invoke(properties, "ro.miui.ui.version.name") as? String ?: "未知"
        } catch (e: Exception) {
            "未知"
        }
    }
}

