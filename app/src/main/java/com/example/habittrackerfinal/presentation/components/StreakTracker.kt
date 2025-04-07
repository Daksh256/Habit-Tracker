package com.example.habittrackerfinal.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


@Composable
fun StreakTracker(
    viewModel: HabitViewModel,
    habitId: Int,
    habitColor: Color // The color chosen for the habit
) {
    // Observe the LiveData from the ViewModel
    val streakData by viewModel.getWeeklyStreakLiveData(habitId).observeAsState(emptyList())

    Row(
        // Adjust arrangement as needed, SpaceEvenly might work well here
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp) // Keep padding if desired
    ) {
        if (streakData.isEmpty()) {
            // Optional: Show placeholder or loading state
            Text("Loading streak...")
        } else {
            streakData.forEach { (day, completed) ->
                // Reintroduce Column for Day Text + Box
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp) // Add space between Text and Box
                ) {
                    // Day Text (keep it simple)
                    Text(
                        text = day,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant // Or a specific color
                    )
                    // Box for completion indicator
                    Box(
                        modifier = Modifier
                            .size(20.dp) // Adjust size as needed
                            .background(
                                // Use habitColor if completed, otherwise LightGray or another neutral color
                                color = if (completed) habitColor else Color.LightGray,
                                // Optional: Add shape if you want rounded boxes
                                // shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }
    }
}

/*
@Composable
fun StreakTracker(
    viewModel: HabitViewModel,
    habitId: Int,
    habitColor: Color // New parameter: the chosen color of the habit
) {
    val scope = rememberCoroutineScope()
    var streak by remember { mutableStateOf<List<Pair<String, Boolean>>>(emptyList()) }
    val streakData by viewModel.getWeeklyStreakLiveData(habitId).observeAsState(emptyList())

    LaunchedEffect(habitId) {
        scope.launch(Dispatchers.IO) {
            streak = viewModel.getHabitStreakForWeek(habitId)
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp) // Added some padding
    ) {
        // Use the observed streakData directly
        streakData.forEach { (day, completed) ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = day)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        // Use habitColor if completed, otherwise LightGray
                        .background(if (completed) habitColor else Color.LightGray)
                )
            }
        }
    }

}
*/
