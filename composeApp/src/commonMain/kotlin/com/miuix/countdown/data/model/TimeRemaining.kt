package com.miuix.countdown.data.model

/**
 * 剩余时间数据类
 */
data class TimeRemaining(
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0,
    val milliseconds: Long = 0,
    val totalMillis: Long = 0
) {
    companion object {
        fun fromMillis(millis: Long): TimeRemaining {
            if (millis <= 0) {
                return TimeRemaining()
            }
            
            val totalSeconds = millis / 1000
            val days = totalSeconds / 86400
            val hours = (totalSeconds % 86400) / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60
            val milliseconds = millis % 1000
            
            return TimeRemaining(
                days = days,
                hours = hours,
                minutes = minutes,
                seconds = seconds,
                milliseconds = milliseconds,
                totalMillis = millis
            )
        }
    }
    
    /**
     * 格式化为字符串
     */
    fun format(): String {
        return when {
            days > 0 -> "${days}天 ${hours}时 ${minutes}分 ${seconds}秒"
            hours > 0 -> "${hours}时 ${minutes}分 ${seconds}秒"
            minutes > 0 -> "${minutes}分 ${seconds}秒"
            else -> "${seconds}秒"
        }
    }
    
    /**
     * 格式化为简短字符串
     */
    fun formatShort(): String {
        return when {
            days > 0 -> "${days}天${hours}时"
            hours > 0 -> "${hours}时${minutes}分"
            minutes > 0 -> "${minutes}分${seconds}秒"
            else -> "${seconds}秒"
        }
    }
    
    /**
     * 格式化为数字显示（HH:MM:SS）
     */
    fun formatDigital(): String {
        return when {
            days > 0 -> String.format("%d:%02d:%02d:%02d", days, hours, minutes, seconds)
            hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
            else -> String.format("%02d:%02d", minutes, seconds)
        }
    }
}

