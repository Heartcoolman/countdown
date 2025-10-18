package com.miuix.countdown.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.miuix.countdown.CountdownApplication
import com.miuix.countdown.MainActivity
import com.miuix.countdown.R

class AlarmReceiver : BroadcastReceiver() {
    
    companion object {
        const val EXTRA_COUNTDOWN_ID = "countdown_id"
        const val EXTRA_COUNTDOWN_TITLE = "countdown_title"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        val countdownId = intent.getStringExtra(EXTRA_COUNTDOWN_ID) ?: return
        val countdownTitle = intent.getStringExtra(EXTRA_COUNTDOWN_TITLE) ?: "倒计时"
        
        showNotification(context, countdownId, countdownTitle)
    }
    
    private fun showNotification(context: Context, countdownId: String, title: String) {
        val notificationManager = context.getSystemService<NotificationManager>() ?: return
        
        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            countdownId.hashCode(),
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        
        val notification = NotificationCompat.Builder(context, CountdownApplication.CHANNEL_ID_COUNTDOWN)
            .setContentTitle("⏰ 倒计时结束")
            .setContentText(title)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .build()
        
        notificationManager.notify(countdownId.hashCode(), notification)
    }
}

