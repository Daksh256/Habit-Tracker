package com.example.habittrackerfinal.presentation.components


import com.example.habittrackerfinal.data.model.Habit
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel

@Composable
fun HabitItem(
    habit: Habit,
    onDelete: () -> Unit,
    onComplete: () -> Unit,
    viewModel: HabitViewModel
) {
    Card {
        Column {
            Text(text = "${habit.icon} ${habit.name}")
            Text(text = "Category: ${habit.category}")
            // Display other habit details

            Row {
                Button(onClick = onComplete) {
                    Text("Complete")
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
