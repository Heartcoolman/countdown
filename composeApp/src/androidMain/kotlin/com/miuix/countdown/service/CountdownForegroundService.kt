package com.miuix.countdown.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.miuix.countdown.CountdownApplication
import com.miuix.countdown.MainActivity
import com.miuix.countdown.R

class CountdownForegroundService : Service() {
    
    companion object {
        private const val NOTIFICATION_ID = 1001
        const val ACTION_START = "com.miuix.countdown.START_FOREGROUND"
        const val ACTION_STOP = "com.miuix.countdown.STOP_FOREGROUND"
        const val EXTRA_COUNTDOWN_TITLE = "countdown_title"
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val countdownTitle = intent.getStringExtra(EXTRA_COUNTDOWN_TITLE) ?: "倒计时"
                startForegroundService(countdownTitle)
            }
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }
    
    private fun startForegroundService(title: String) {
        val notification = createNotification(title)
        startForeground(NOTIFICATION_ID, notification)
    }
    
    private fun createNotification(title: String): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        return NotificationCompat.Builder(this, CountdownApplication.CHANNEL_ID_FOREGROUND)
            .setContentTitle("倒计时运行中")
            .setContentText(title)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }
}

