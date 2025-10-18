package com.miuix.countdown.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TimerState(
    val durationMillis: Long = 0,  // 设定的总时长
    val remainingMillis: Long = 0,  // 剩余时间
    val isRunning: Boolean = false,
    val isCompleted: Boolean = false
)

class TimerViewModel : ViewModel() {
    
    private val _state = MutableStateFlow(TimerState())
    val state: StateFlow<TimerState> = _state.asStateFlow()
    
    private var lastUpdateTime = 0L
    
    // 快捷时间选项（秒）
    val quickTimeOptions = listOf(
        60L to "1分钟",
        300L to "5分钟",
        600L to "10分钟",
        900L to "15分钟",
        1800L to "30分钟",
        3600L to "1小时"
    )
    
    fun setDuration(hours: Int, minutes: Int, seconds: Int) {
        val totalMillis = (hours * 3600L + minutes * 60L + seconds) * 1000L
        _state.value = TimerState(
            durationMillis = totalMillis,
            remainingMillis = totalMillis
        )
    }
    
    fun setDurationFromSeconds(seconds: Long) {
        val totalMillis = seconds * 1000L
        _state.value = TimerState(
            durationMillis = totalMillis,
            remainingMillis = totalMillis
        )
    }
    
    fun start() {
        if (!_state.value.isRunning && _state.value.remainingMillis > 0) {
            lastUpdateTime = System.currentTimeMillis()
            _state.value = _state.value.copy(
                isRunning = true,
                isCompleted = false
            )
            startTimer()
        }
    }
    
    fun pause() {
        if (_state.value.isRunning) {
            _state.value = _state.value.copy(isRunning = false)
        }
    }
    
    fun reset() {
        _state.value = _state.value.copy(
            remainingMillis = _state.value.durationMillis,
            isRunning = false,
            isCompleted = false
        )
    }
    
    fun stop() {
        _state.value = TimerState()
    }
    
    fun getProgress(): Float {
        if (_state.value.durationMillis == 0L) return 0f
        val elapsed = _state.value.durationMillis - _state.value.remainingMillis
        return (elapsed.toFloat() / _state.value.durationMillis.toFloat()).coerceIn(0f, 1f)
    }
    
    private fun startTimer() {
        viewModelScope.launch {
            while (_state.value.isRunning && _state.value.remainingMillis > 0) {
                val currentTime = System.currentTimeMillis()
                val elapsed = currentTime - lastUpdateTime
                lastUpdateTime = currentTime
                
                val newRemaining = (_state.value.remainingMillis - elapsed).coerceAtLeast(0)
                
                if (newRemaining <= 0) {
                    _state.value = _state.value.copy(
                        remainingMillis = 0,
                        isRunning = false,
                        isCompleted = true
                    )
                } else {
                    _state.value = _state.value.copy(remainingMillis = newRemaining)
                }
                
                delay(50) // 更新频率 50ms
            }
        }
    }
}

