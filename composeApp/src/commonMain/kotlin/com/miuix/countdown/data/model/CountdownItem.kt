package com.miuix.countdown.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CountdownItem(
    val id: String,
    val title: String,
    val description: String = "",
    val type: CountdownType,
    val targetDate: Instant? = null,  // 用于日期倒计时
    val durationMillis: Long? = null, // 用于定时器（毫秒）
    val createdAt: Instant,
    val isCompleted: Boolean = false,
    val isPaused: Boolean = false,
    val remainingMillis: Long? = null // 定时器剩余时间
) {
    /**
     * 判断倒计时是否正在进行中
     */
    fun isActive(): Boolean {
        return !isCompleted && !isPaused
    }
    
    /**
     * 判断是否是未来的倒计时（还未开始）
     */
    fun isFuture(now: Instant): Boolean {
        return when (type) {
            CountdownType.DATE -> {
                targetDate?.let { it > now } ?: false
            }
            CountdownType.TIMER -> {
                remainingMillis?.let { it > 0 } ?: false
            }
        }
    }
}

