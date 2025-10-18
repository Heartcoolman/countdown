package com.miuix.countdown.data.repository

import com.miuix.countdown.data.model.CountdownItem
import kotlinx.coroutines.flow.Flow

interface CountdownRepository {
    /**
     * 获取所有倒计时项目
     */
    fun getAllCountdowns(): Flow<List<CountdownItem>>
    
    /**
     * 获取正在进行的倒计时项目
     */
    fun getActiveCountdowns(): Flow<List<CountdownItem>>
    
    /**
     * 根据 ID 获取倒计时项目
     */
    suspend fun getCountdownById(id: String): CountdownItem?
    
    /**
     * 添加倒计时项目
     */
    suspend fun addCountdown(item: CountdownItem)
    
    /**
     * 更新倒计时项目
     */
    suspend fun updateCountdown(item: CountdownItem)
    
    /**
     * 删除倒计时项目
     */
    suspend fun deleteCountdown(id: String)
    
    /**
     * 删除所有倒计时项目
     */
    suspend fun deleteAllCountdowns()
}

