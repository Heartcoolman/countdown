package com.miuix.countdown

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.miuix.countdown.ui.navigation.Screen
import com.miuix.countdown.ui.screen.*
import com.miuix.countdown.viewmodel.CountdownViewModel
import com.miuix.countdown.viewmodel.StopwatchViewModel
import com.miuix.countdown.viewmodel.TimerViewModel
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun App(
    countdownViewModel: CountdownViewModel,
    stopwatchViewModel: StopwatchViewModel = viewModel { StopwatchViewModel() },
    timerViewModel: TimerViewModel = viewModel { TimerViewModel() }
) {
    MiuixTheme {
        val navController = rememberNavController()
        
        NavHost(
            navController = navController,
            startDestination = Screen.Home
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    viewModel = countdownViewModel,
                    onNavigateToAllCountdowns = {
                        navController.navigate(Screen.AllCountdowns)
                    },
                    onNavigateToAddEdit = {
                        navController.navigate(Screen.AddEditCountdown)
                    },
                    onNavigateToDetail = { id ->
                        navController.navigate(Screen.CountdownDetail(id))
                    },
                    onNavigateToStopwatch = {
                        navController.navigate(Screen.Stopwatch)
                    },
                    onNavigateToTimer = {
                        navController.navigate(Screen.Timer)
                    }
                )
            }
            
            composable<Screen.AllCountdowns> {
                AllCountdownsScreen(
                    viewModel = countdownViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToDetail = { id ->
                        navController.navigate(Screen.CountdownDetail(id))
                    },
                    onNavigateToAddEdit = {
                        navController.navigate(Screen.AddEditCountdown)
                    }
                )
            }
            
            composable<Screen.AddEditCountdown> {
                AddEditCountdownScreen(
                    viewModel = countdownViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable<Screen.CountdownDetail> { backStackEntry ->
                val args = backStackEntry.toRoute<Screen.CountdownDetail>()
                CountdownDetailScreen(
                    countdownId = args.countdownId,
                    viewModel = countdownViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToEdit = {
                        navController.navigate(Screen.AddEditCountdown)
                    }
                )
            }
            
            composable<Screen.Stopwatch> {
                StopwatchScreen(
                    viewModel = stopwatchViewModel,
                    onNavigateToHome = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Home) { inclusive = true }
                        }
                    },
                    onNavigateToTimer = {
                        navController.navigate(Screen.Timer)
                    }
                )
            }
            
            composable<Screen.Timer> {
                TimerScreen(
                    viewModel = timerViewModel,
                    onNavigateToHome = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Home) { inclusive = true }
                        }
                    },
                    onNavigateToStopwatch = {
                        navController.navigate(Screen.Stopwatch)
                    }
                )
            }
        }
    }
}

