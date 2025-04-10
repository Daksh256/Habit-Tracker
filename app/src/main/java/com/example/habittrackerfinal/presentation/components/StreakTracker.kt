// In presentation/components/StreakTracker.kt

package com.example.habittrackerfinal.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittrackerfinal.data.model.Habit // Import Habit
import com.example.habittrackerfinal.presentation.viewmodel.HabitViewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.ui.graphics.drawscope.Fill

@Composable
fun StreakTracker(
    viewModel: HabitViewModel,
    habit: Habit, // Receive Habit object
    habitColor: Color
) {
    // Observe the LiveData for weekly counts
    // It now returns List<Pair<LocalDate, Int>>
    val weeklyCounts by viewModel.getWeeklyCompletionCounts(habit.id).observeAsState(emptyList())

    Row(
        horizontalArrangement = Arrangement.SpaceAround, // SpaceAround might look better
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        if (weeklyCounts.isEmpty()) {
            Text("Loading...") // Placeholder
        } else {
            weeklyCounts.forEach { (date, count) ->
                // Pass habit and date to DayIndicator
                DayIndicator(
                    date = date,
                    completionCount = count,
                    completionsNeeded = habit.completionsPerDay,
                    color = habitColor,
                    onClick = {
                        viewModel.handleDayClick(habit.id, date) // Use the specific date
                    }
                )
            }
        }
    }
}

@Composable
fun DayIndicator(
    date: LocalDate,
    completionCount: Int,
    completionsNeeded: Int,
    color: Color, // Habit color
    onClick: () -> Unit
) {
    val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val safeCompletionsNeeded = maxOf(1, completionsNeeded) // Ensure at least 1
    val isComplete = completionCount >= safeCompletionsNeeded
    // Calculate segment angle for the track
    val segmentAngle = if (safeCompletionsNeeded > 0) 360f / safeCompletionsNeeded else 0f

    val indicatorSize = 24.dp
    // Define the thickness of the track inside the box
    val trackStrokeWidth = 4.dp // Adjust thickness as needed

    // Define a base color for the inactive track (e.g., gray or darker shade)
    val trackBaseColor = Color.Gray.copy(alpha = 0.3f) // Example: transparent gray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp)
    ) {
        // Day Text
        Text(
            text = dayName,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isComplete) 0.7f else 1.0f),
            textAlign = TextAlign.Center
        )

        // Progress Indicator Box
        Box(
            modifier = Modifier
                .size(indicatorSize)
                // Use habit color for background when complete
                .background(
                    color = if (isComplete) color else trackBaseColor, // Use base color or habit color
                    // Make the box background slightly rounded
                    shape = MaterialTheme.shapes.extraSmall // Or RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Only draw the track details if NOT fully complete
            if (!isComplete) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasSize = size.minDimension
                    val strokeWidthPx = trackStrokeWidth.toPx()

                    // Calculate radius considering the stroke width for centering the track
                    // Use a slightly smaller radius so the track fits well within the box
                    val radius = (canvasSize / 2f) - (strokeWidthPx / 2f) - (1.dp.toPx()) // Small padding
                    val center = Offset(canvasSize / 2f, canvasSize / 2f)
                    val arcSize = Size(radius * 2, radius * 2) // Size of the arc drawing area
                    val arcTopLeft = Offset(center.x - radius, center.y - radius) // Top-left for arc

                    // 1. Draw the full background track (e.g., gray)
                    drawArc(
                        color = trackBaseColor.copy(alpha = 0.5f), // Make base track slightly more visible
                        startAngle = 0f, // Start from right
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = arcTopLeft,
                        size = arcSize,
                        style = Stroke(width = strokeWidthPx, cap = StrokeCap.Butt) // Butt cap for seamless circle
                    )

                    // 2. Draw the colored progress segments on top
                    if (completionCount > 0 && segmentAngle > 0f) {
                        for (i in 0 until completionCount) {
                            // Start at -90 (top) and draw segments clockwise
                            val start = -90f + (i * segmentAngle)
                            // Subtract a small gap angle for visual separation if needed, otherwise use full segmentAngle
                            // val sweep = segmentAngle - 2f
                            val sweep = segmentAngle // Use full segment angle
                            drawArc(
                                color = color, // Habit color for progress
                                startAngle = start,
                                sweepAngle = sweep,
                                useCenter = false,
                                topLeft = arcTopLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round) // Round cap looks good
                            )
                        }
                    }
                } // End Canvas
            } // End if (!isComplete)
        } // End Box
    } // End Column
}