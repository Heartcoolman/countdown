package com.miuix.countdown.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LapTime(
    val lapNumber: Int,
    val lapTime: Long,  // 本圈时间
    val totalTime: Long  // 总时间
)

data class StopwatchState(
    val elapsedMillis: Long = 0,
    val isRunning: Boolean = false,
    val laps: List<LapTime> = emptyList()
)

class StopwatchViewModel : ViewModel() {
    
    private val _state = MutableStateFlow(StopwatchState())
    val state: StateFlow<StopwatchState> = _state.asStateFlow()
    
    private var startTime = 0L
    private var pausedTime = 0L
    
    fun start() {
        if (!_state.value.isRunning) {
            startTime = System.currentTimeMillis() - _state.value.elapsedMillis
            _state.value = _state.value.copy(isRunning = true)
            startTimer()
        }
    }
    
    fun pause() {
        if (_state.value.isRunning) {
            pausedTime = _state.value.elapsedMillis
            _state.value = _state.value.copy(isRunning = false)
        }
    }
    
    fun reset() {
        _state.value = StopwatchState()
        startTime = 0L
        pausedTime = 0L
    }
    
    fun lap() {
        if (_state.value.isRunning) {
            val currentTime = _state.value.elapsedMillis
            val lastLapTotalTime = _state.value.laps.lastOrNull()?.totalTime ?: 0L
            val lapTime = currentTime - lastLapTotalTime
            
            val newLap = LapTime(
                lapNumber = _state.value.laps.size + 1,
                lapTime = lapTime,
                totalTime = currentTime
            )
            
            _state.value = _state.value.copy(
                laps = _state.value.laps + newLap
            )
        }
    }
    
    private fun startTimer() {
        viewModelScope.launch {
            while (_state.value.isRunning) {
                val elapsed = System.currentTimeMillis() - startTime
                _state.value = _state.value.copy(elapsedMillis = elapsed)
                delay(10) // 更新频率 10ms
            }
        }
    }
}

