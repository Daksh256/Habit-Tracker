package com.example.habittrackerfinal
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.habittrackerfinal.Database.Habits

data class HabitCategory(
    val name: String,
    val icon: ImageVector,
    val iconName: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    modifier: Modifier = Modifier,
    viewModel: HabitViewModel
) {
    val habits by viewModel.habitsList.observeAsState(emptyList())

    val categories = listOf(
        HabitCategory("Finance", Icons.Filled.AccountBox, "💰"),
        HabitCategory("Fitness", Icons.Filled.Face, "💪"),
        HabitCategory("Study", Icons.Filled.Email, "📚")
    )

    val colorOptions = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta)

    var habitName by remember { mutableStateOf("") }
    var habitDescription by remember { mutableStateOf("") }
    var habitCompletionsPerDay by remember { mutableStateOf("1") } // Default value
    var selectedCategory by remember { mutableStateOf<HabitCategory?>(null) }
    var showError by remember { mutableStateOf(false) }
    var selectedColor by remember{mutableStateOf(Color.Blue)}

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (showError) {
            Text(
                text = "Please fill all required fields",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                TextField(
                    value = habitName,
                    onValueChange = {
                        habitName = it
                        showError = false
                    },
                    label = { Text("Habit Name *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = habitDescription,
                    onValueChange = { habitDescription = it },
                    label = { Text("Habit Description (Optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = habitCompletionsPerDay,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(Regex("^\\d+$"))) {
                            habitCompletionsPerDay = it
                        }
                    },
                    label = { Text("Completions Per Day *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    categories.forEach { category ->
                        CategoryButton(
                            category = category,
                            isSelected = selectedCategory == category,
                            onClick = {
                                selectedCategory = category
                                showError = false
                            }
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    colorOptions.forEach { color ->
                        ColorBox(color, isSelected = color == selectedColor) {
                            selectedColor = color
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (habitName.isNotBlank() && selectedCategory != null && habitCompletionsPerDay.isNotBlank()) {
                            try {
                                val completions = habitCompletionsPerDay.toIntOrNull() ?: 1
                                viewModel.addHabit(
                                    title = habitName.trim(),
                                    description = habitDescription.ifBlank { null },
                                    completionsPerDay = completions,
                                    category = selectedCategory!!.name,
                                    emoji = selectedCategory!!.iconName,
                                    color = selectedColor.toArgb()
                                )
                                // Reset form
                                habitName = ""
                                habitDescription = ""
                                habitCompletionsPerDay = "1"
                                selectedCategory = null
                                showError = false
                            } catch (e: Exception) {
                                showError = true
                            }
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add Habit")
                }
            }
        }

        LazyColumn {
            items(habits) { habit ->
                HabitItem(
                    habit = habit,
                    onDelete = { viewModel.deleteHabit(habit.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitItem(
    habit: Habits,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${habit.selectedIcon} ${habit.name}",
                    style = MaterialTheme.typography.titleMedium
                )
                habit.description?.let { description ->
                    if (description.isNotBlank()) {
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Text(
                    text = "Category: ${habit.selectedCategory}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Daily Goal: ${habit.completionsPerDay}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Box(
                modifier = Modifier.background(Color(habit.selectedColor))
            ) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete habit"
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryButton(
    category: HabitCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(category.name)
        }
    }
}

@Composable
fun ColorBox(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color)
            .border(2.dp, if (isSelected) Color.Black else Color.Transparent)
            .clickable { onClick() }
    )
}