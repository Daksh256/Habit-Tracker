package com.example.habittrackerfinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.habittrackerfinal.data.repository.HabitRepository
import com.example.habittrackerfinal.domain.usecase.AddHabitUseCase
import com.example.habittrackerfinal.presentation.navigation.NavGraph
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModelFactory
import com.example.habittrackerfinal.ui.theme.HabitTrackerFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create repository and use-case instance from your database instance
        val repository = HabitRepository(MainApplication.habitDatabase.habitDao())
        val addHabitUseCase = AddHabitUseCase(repository)

        // Create the ViewModel using the custom factory
        val viewModelFactory = HabitViewModelFactory(repository, addHabitUseCase)
        val habitViewModel = ViewModelProvider(this, viewModelFactory)[HabitViewModel::class.java]

        setContent {
            HabitTrackerFinalTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        viewModel = habitViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
