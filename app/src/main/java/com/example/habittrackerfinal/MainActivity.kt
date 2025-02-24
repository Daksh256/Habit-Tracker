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
import com.example.habittrackerfinal.ui.theme.HabitTrackerFinalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val habitViewModel = ViewModelProvider(this)[HabitViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            HabitTrackerFinalTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HabitListScreen(
                        modifier = Modifier.padding(innerPadding),
                        habitViewModel
                    )
                }
            }
        }
    }
}

