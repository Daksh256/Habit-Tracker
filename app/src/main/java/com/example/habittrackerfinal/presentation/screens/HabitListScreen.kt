package com.example.habittrackerfinal.presentation.screens


import androidx.compose.foundation.layout.Arrangement
import com.example.habittrackerfinal.presentation.components.HabitItem
import com.example.habittrackerfinal.presentation.components.StreakTracker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.habittrackerfinal.R
import com.example.habittrackerfinal.data.model.Habit
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel



@Composable
fun HabitListScreen(viewModel: HabitViewModel, onNavigateToAddHabit: () -> Unit) {
    val habits by viewModel.habitsList.observeAsState(emptyList())

    //Test
    // State for managing the delete confirmation dialog
    var showDeleteDialog by remember { mutableStateOf(false) }
    var habitToDelete by remember { mutableStateOf<Habit?>(null) }

    Column { // Keep your existing outer Column or structure
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onNavigateToAddHabit
            ){
                Icon(
                   imageVector = Icons.Default.AddCircle,
                    contentDescription = "Add Habit"
                )
            }
            Spacer(modifier = Modifier.padding(end = 3.dp))
            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_bar_chart_24),
                    contentDescription = "Bar Chart"
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(habits) { habit ->
                HabitItem(
                    habit = habit,
                    onComplete = { viewModel.completeHabit(habit.id) },
                    // Pass the lambda for long click
                    onLongClick = {
                        habitToDelete = habit // Store the habit to delete
                        showDeleteDialog = true // Show the dialog
                    },
                    viewModel = viewModel
                )
            }
        }
    } // End of outer Column

    // --- Add the AlertDialog ---
    if (showDeleteDialog && habitToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when clicking outside or pressing back
                showDeleteDialog = false
                habitToDelete = null
            },
            title = { Text("Delete Habit") },
            text = { Text("Are you sure you want to delete '${habitToDelete?.name}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteHabit(habitToDelete!!.id) // Call ViewModel delete function
                        showDeleteDialog = false // Close dialog
                        habitToDelete = null // Reset selected habit
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false // Close dialog
                        habitToDelete = null // Reset selected habit
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    // --- End of AlertDialog ---
}

