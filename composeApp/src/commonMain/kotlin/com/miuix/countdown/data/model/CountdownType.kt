package com.miuix.countdown.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class CountdownType {
    DATE,      // 日期倒计时
    TIMER      // 定时器倒计时
}

