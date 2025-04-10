package com.example.habittrackerfinal.presentation.components


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import com.example.habittrackerfinal.data.model.Habit
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitItem(
    habit: Habit,
    //onDelete: () -> Unit,
    onLongClick: () -> Unit,
    onComplete: () -> Unit,
    viewModel: HabitViewModel
) {
    // Use ElevatedCard for slight shadow or Card for flat
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .combinedClickable(
                onClick = { },
        onLongClick = onLongClick // Call the lambda passed in on long press
        ),
        shape = RoundedCornerShape(16.dp), // Rounded corners
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Light greyish color
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Padding inside the card
            verticalAlignment = Alignment.CenterVertically // Align items vertically
        ) {
            // Column for Text and StreakTracker
            Column(
                modifier = Modifier.weight(1f) // Takes available space
            ) {
                // Habit Name (Title)
                Text(
                    text = habit.name, // Removed icon prefix, add back if needed
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Habit Description (Optional)
                habit.description?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f) // Slightly faded
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Space before streak
                }

                // Integrate StreakTracker directly here
                StreakTracker(
                    viewModel = viewModel,
                    habit = habit, // Pass the whole habit object
                    habitColor = Color(habit.color)
                )
            }

            // Spacer between text column and button
            Spacer(modifier = Modifier.width(16.dp))

            // Complete Button (Checkmark)
            IconButton(
                // Update onClick to call the new ViewModel function
                onClick = { viewModel.handleDayClick(habit.id, LocalDate.now()) },
                //onClick = { viewModel.completeAllForToday(habit.id) },
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    // Update content description
                    contentDescription = "Complete all for today",
                    modifier = Modifier.size(24.dp)
                )
            }

            // Removed old Row with Text Button and Delete Icon
            // Consider where to put the delete action if needed (e.g., long press, swipe)
        }
    }
}

/*
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
*/