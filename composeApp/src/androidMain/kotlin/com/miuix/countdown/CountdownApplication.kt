package com.miuix.countdown

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.getSystemService

class CountdownApplication : Application() {
    
    companion object {
        const val CHANNEL_ID_COUNTDOWN = "countdown_channel"
        const val CHANNEL_ID_FOREGROUND = "foreground_service_channel"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService<NotificationManager>()
            
            // 倒计时通知渠道 - 针对小米设备优化
            val countdownChannel = NotificationChannel(
                CHANNEL_ID_COUNTDOWN,
                getString(R.string.notification_channel_countdown_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.notification_channel_countdown_desc)
                enableVibration(true)
                enableLights(true)
                // 小米设备：允许通知浮现
                setShowBadge(true)
                // 小米设备：锁屏显示
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
                // 设置声音（小米设备重视）
                setSound(
                    android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION),
                    android.media.AudioAttributes.Builder()
                        .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
            }
            
            // 前台服务通知渠道 - 小米系统要求明确说明用途
            val foregroundChannel = NotificationChannel(
                CHANNEL_ID_FOREGROUND,
                getString(R.string.notification_channel_foreground_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_foreground_desc)
                setShowBadge(false)
                // 前台服务通知静默
                setSound(null, null)
                enableVibration(false)
            }
            
            notificationManager?.createNotificationChannel(countdownChannel)
            notificationManager?.createNotificationChannel(foregroundChannel)
        }
    }
}

