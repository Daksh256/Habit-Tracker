package com.example.habittracker.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.habittrackerfinal.data.model.Habit
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.habittrackerfinal.presentation.components.ColorBox

@Composable
fun AddHabitScreen(viewModel: HabitViewModel, onNavigateBack: () -> Unit) {
    // State for input fields
    val habitName = remember { mutableStateOf("") }
    val habitDescription = remember { mutableStateOf("") }
    val habitCategory = remember { mutableStateOf("") }
    val habitCompletionsPerDay = remember { mutableStateOf("1") }
    var selectedColor by remember { mutableStateOf(Color.Blue) }

    //Color Option for Habit
    val colorOptions = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta)


    // For this example, we use defaults for emoji and color.
    val defaultEmoji = "⭐"
    //val defaultColor = Color.Blue

    // Show error message if validation fails.
    val showError = remember { mutableStateOf(false) }

    Column {
        TextField(
            value = habitName.value,
            onValueChange = {
                habitName.value = it
                showError.value = false
            },
            label = { Text("Habit Name") }
        )

        TextField(
            value = habitDescription.value,
            onValueChange = { habitDescription.value = it },
            label = { Text("Description") }
        )

        TextField(
            value = habitCategory.value,
            onValueChange = { habitCategory.value = it },
            label = { Text("Category") }
        )

        TextField(
            value = habitCompletionsPerDay.value,
            onValueChange = { habitCompletionsPerDay.value = it },
            label = { Text("Completions Per Day") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Color Selection Row
        Text(text = "Select Color", style = MaterialTheme.typography.bodyLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colorOptions.forEach { color ->
                ColorBox(color = color, isSelected = color == selectedColor) {
                    selectedColor = color
                }
            }
        }

        if (showError.value) {
            Text(
                text = "Please fill in the required fields",
                color = Color.Red
            )
        }

        Button(onClick = {
            // Check that required fields are not blank
            if (habitName.value.isNotBlank() && habitCategory.value.isNotBlank()) {
                val habit = Habit(
                    id = 0,  // id can be auto-generated in your DB
                    name = habitName.value.trim(),
                    description = habitDescription.value.ifBlank { null },
                    completionsPerDay = habitCompletionsPerDay.value.toIntOrNull() ?: 1,
                    category = habitCategory.value,
                    icon = defaultEmoji,
                    color = selectedColor.toArgb(),
                    createdDate = System.currentTimeMillis() // Using timestamp for createdDate
                )
                viewModel.addHabit(habit)
                onNavigateBack()
            } else {
                showError.value = true
            }
        }) {
            Text("Submit")
        }
    }
}
