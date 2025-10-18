package com.miuix.countdown.utils

import com.miuix.countdown.data.model.CountdownItem
import com.miuix.countdown.data.model.CountdownType
import com.miuix.countdown.data.model.TimeRemaining
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

object TimeCalculator {
    /**
     * 计算倒计时剩余时间
     */
    fun calculateRemaining(item: CountdownItem, now: Instant = Clock.System.now()): TimeRemaining {
        return when (item.type) {
            CountdownType.DATE -> {
                val targetDate = item.targetDate ?: return TimeRemaining()
                val millis = (targetDate.toEpochMilliseconds() - now.toEpochMilliseconds())
                TimeRemaining.fromMillis(millis.coerceAtLeast(0))
            }
            CountdownType.TIMER -> {
                val remaining = item.remainingMillis ?: item.durationMillis ?: 0
                TimeRemaining.fromMillis(remaining.coerceAtLeast(0))
            }
        }
    }
    
    /**
     * 计算进度百分比（0.0 - 1.0）
     */
    fun calculateProgress(item: CountdownItem, now: Instant = Clock.System.now()): Float {
        return when (item.type) {
            CountdownType.DATE -> {
                val targetDate = item.targetDate ?: return 0f
                val totalMillis = targetDate.toEpochMilliseconds() - item.createdAt.toEpochMilliseconds()
                val elapsedMillis = now.toEpochMilliseconds() - item.createdAt.toEpochMilliseconds()
                if (totalMillis <= 0) return 1f
                (elapsedMillis.toFloat() / totalMillis.toFloat()).coerceIn(0f, 1f)
            }
            CountdownType.TIMER -> {
                val total = item.durationMillis ?: return 0f
                val remaining = item.remainingMillis ?: total
                if (total <= 0) return 1f
                ((total - remaining).toFloat() / total.toFloat()).coerceIn(0f, 1f)
            }
        }
    }
    
    /**
     * 判断倒计时是否已经结束
     */
    fun isExpired(item: CountdownItem, now: Instant = Clock.System.now()): Boolean {
        return when (item.type) {
            CountdownType.DATE -> {
                val targetDate = item.targetDate ?: return true
                now >= targetDate
            }
            CountdownType.TIMER -> {
                val remaining = item.remainingMillis ?: 0
                remaining <= 0
            }
        }
    }
}

