package com.miuix.countdown.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.miuix.countdown.data.model.CountdownItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "countdown_data")

class CountdownRepositoryImpl(
    private val context: Context
) : CountdownRepository {
    
    private val json = Json { 
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    companion object {
        private val COUNTDOWNS_KEY = stringPreferencesKey("countdowns")
    }
    
    override fun getAllCountdowns(): Flow<List<CountdownItem>> {
        return context.dataStore.data.map { preferences ->
            val jsonString = preferences[COUNTDOWNS_KEY] ?: "[]"
            try {
                json.decodeFromString<List<CountdownItem>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
    
    override fun getActiveCountdowns(): Flow<List<CountdownItem>> {
        return getAllCountdowns().map { items ->
            items.filter { it.isActive() }
        }
    }
    
    override suspend fun getCountdownById(id: String): CountdownItem? {
        return getAllCountdowns().first().find { it.id == id }
    }
    
    override suspend fun addCountdown(item: CountdownItem) {
        context.dataStore.edit { preferences ->
            val currentList = getAllCountdowns().first().toMutableList()
            currentList.add(item)
            preferences[COUNTDOWNS_KEY] = json.encodeToString(currentList)
        }
    }
    
    override suspend fun updateCountdown(item: CountdownItem) {
        context.dataStore.edit { preferences ->
            val currentList = getAllCountdowns().first().toMutableList()
            val index = currentList.indexOfFirst { it.id == item.id }
            if (index != -1) {
                currentList[index] = item
                preferences[COUNTDOWNS_KEY] = json.encodeToString(currentList)
            }
        }
    }
    
    override suspend fun deleteCountdown(id: String) {
        context.dataStore.edit { preferences ->
            val currentList = getAllCountdowns().first().toMutableList()
            currentList.removeAll { it.id == id }
            preferences[COUNTDOWNS_KEY] = json.encodeToString(currentList)
        }
    }
    
    override suspend fun deleteAllCountdowns() {
        context.dataStore.edit { preferences ->
            preferences[COUNTDOWNS_KEY] = "[]"
        }
    }
}

