package com.miuix.countdown.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()
    
    @Serializable
    data object AllCountdowns : Screen()
    
    @Serializable
    data object AddEditCountdown : Screen()
    
    @Serializable
    data class CountdownDetail(val countdownId: String) : Screen()
    
    @Serializable
    data object Stopwatch : Screen()
    
    @Serializable
    data object Timer : Screen()
}

enum class BottomNavItem(
    val title: String,
    val route: Screen
) {
    HOME("首页", Screen.Home),
    STOPWATCH("秒表", Screen.Stopwatch),
    TIMER("定时器", Screen.Timer)
}

