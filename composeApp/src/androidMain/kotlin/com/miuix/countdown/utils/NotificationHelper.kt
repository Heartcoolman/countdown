package com.miuix.countdown.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.getSystemService
import com.miuix.countdown.data.model.CountdownItem
import com.miuix.countdown.data.model.CountdownType
import com.miuix.countdown.receiver.AlarmReceiver
import com.miuix.countdown.service.CountdownForegroundService

object NotificationHelper {
    
    /**
     * 为倒计时设置闹钟通知
     */
    fun scheduleCountdownAlarm(context: Context, countdown: CountdownItem) {
        val alarmManager = context.getSystemService<AlarmManager>() ?: return
        
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_COUNTDOWN_ID, countdown.id)
            putExtra(AlarmReceiver.EXTRA_COUNTDOWN_TITLE, countdown.title)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            countdown.id.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val triggerTime = when (countdown.type) {
            CountdownType.DATE -> countdown.targetDate?.toEpochMilliseconds() ?: return
            CountdownType.TIMER -> System.currentTimeMillis() + (countdown.durationMillis ?: return)
        }
        
        // 使用精确闹钟 API
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            } else {
                // 无法设置精确闹钟，使用非精确闹钟
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTime,
                pendingIntent
            )
        }
    }
    
    /**
     * 取消倒计时闹钟
     */
    fun cancelCountdownAlarm(context: Context, countdownId: String) {
        val alarmManager = context.getSystemService<AlarmManager>() ?: return
        
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            countdownId.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )
        
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }
    
    /**
     * 启动前台服务
     */
    fun startForegroundService(context: Context, title: String) {
        val intent = Intent(context, CountdownForegroundService::class.java).apply {
            action = CountdownForegroundService.ACTION_START
            putExtra(CountdownForegroundService.EXTRA_COUNTDOWN_TITLE, title)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
    
    /**
     * 停止前台服务
     */
    fun stopForegroundService(context: Context) {
        val intent = Intent(context, CountdownForegroundService::class.java).apply {
            action = CountdownForegroundService.ACTION_STOP
        }
        context.startService(intent)
    }
    
    /**
     * 检查是否是小米设备
     */
    fun isMiuiDevice(): Boolean {
        val manufacturer = Build.MANUFACTURER.lowercase()
        return manufacturer.contains("xiaomi") || manufacturer.contains("redmi")
    }
}

