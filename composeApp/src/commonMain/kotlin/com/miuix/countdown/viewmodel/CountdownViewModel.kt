package com.miuix.countdown.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miuix.countdown.data.model.CountdownItem
import com.miuix.countdown.data.model.CountdownType
import com.miuix.countdown.data.repository.CountdownRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID

class CountdownViewModel(
    private val repository: CountdownRepository
) : ViewModel() {
    
    val allCountdowns: StateFlow<List<CountdownItem>> = repository.getAllCountdowns()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val activeCountdowns: StateFlow<List<CountdownItem>> = repository.getActiveCountdowns()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    private val _selectedCountdown = MutableStateFlow<CountdownItem?>(null)
    val selectedCountdown: StateFlow<CountdownItem?> = _selectedCountdown.asStateFlow()
    
    fun selectCountdown(id: String) {
        viewModelScope.launch {
            _selectedCountdown.value = repository.getCountdownById(id)
        }
    }
    
    fun clearSelection() {
        _selectedCountdown.value = null
    }
    
    fun addCountdown(
        title: String,
        description: String,
        type: CountdownType,
        targetDate: Instant? = null,
        durationMillis: Long? = null
    ) {
        viewModelScope.launch {
            val item = CountdownItem(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                type = type,
                targetDate = targetDate,
                durationMillis = durationMillis,
                createdAt = Clock.System.now(),
                remainingMillis = durationMillis
            )
            repository.addCountdown(item)
        }
    }
    
    fun updateCountdown(item: CountdownItem) {
        viewModelScope.launch {
            repository.updateCountdown(item)
        }
    }
    
    fun deleteCountdown(id: String) {
        viewModelScope.launch {
            repository.deleteCountdown(id)
        }
    }
    
    fun togglePause(item: CountdownItem) {
        viewModelScope.launch {
            repository.updateCountdown(item.copy(isPaused = !item.isPaused))
        }
    }
    
    fun markAsCompleted(id: String) {
        viewModelScope.launch {
            repository.getCountdownById(id)?.let { item ->
                repository.updateCountdown(item.copy(isCompleted = true))
            }
        }
    }
}

