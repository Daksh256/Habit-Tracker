package com.example.habittracker.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.habittrackerfinal.data.model.Habit
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.habittrackerfinal.presentation.components.ColorBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitScreen(viewModel: HabitViewModel, onNavigateBack: () -> Unit) {

    val habitName = remember { mutableStateOf("") }
    val habitDescription = remember { mutableStateOf("") }
    val habitCategory = remember { mutableStateOf("") }
    val habitCompletionsPerDay = remember { mutableStateOf("1") }
    var selectedColor by remember { mutableStateOf(Color.Blue) }

    val colorOptions = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta)

    val defaultEmoji = "⭐"

    var showError by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth().padding(2.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Go to home page"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Add a Habit"
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Name",
            modifier = Modifier.align(Alignment.Start)
        )
        OutlinedTextField(
            value = habitName.value,
            onValueChange = {
                habitName.value = it
                showError = false
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Description"
        )
        OutlinedTextField(
            value = habitDescription.value,
            onValueChange = { habitDescription.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Category"
        )
        OutlinedTextField(
            value = habitCategory.value,
            onValueChange = { habitCategory.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = "Completions Per Day"
        )
        OutlinedTextField(
            value = habitCompletionsPerDay.value,
            onValueChange = { habitCompletionsPerDay.value = it },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = Color.Black
            ),
            modifier = Modifier.fillMaxWidth()
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

        if (showError) {
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
                    completionsPerDay = habitCompletionsPerDay.value.toIntOrNull() ?: 3,
                    category = habitCategory.value,
                    icon = defaultEmoji,
                    color = selectedColor.toArgb(),
                    createdDate = System.currentTimeMillis() // Using timestamp for createdDate
                )
                viewModel.addHabit(habit)
                onNavigateBack()
            } else {
                showError = true
            }
        }) {
            Text("Submit")
        }
    }
}

/*
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
                    completionsPerDay = habitCompletionsPerDay.value.toIntOrNull() ?: 3,
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
*/