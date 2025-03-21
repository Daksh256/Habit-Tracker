package com.example.habittrackerfinal.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.habittracker.presentation.screens.AddHabitScreen

import com.example.habittrackerfinal.presentation.screens.HabitListScreen
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel

sealed class Screen(val route: String) {
    object HabitList : Screen("habit_list")
    object AddHabit : Screen("add_habit")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: HabitViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HabitList.route,
        modifier = modifier // Apply modifier here
    ) {
        composable(Screen.HabitList.route) {
            HabitListScreen(viewModel = viewModel, onNavigateToAddHabit = {
                navController.navigate(Screen.AddHabit.route)
            })
        }
        composable(Screen.AddHabit.route) {
            AddHabitScreen(viewModel = viewModel, onNavigateBack = {
                navController.popBackStack()
            })
        }
    }
}

