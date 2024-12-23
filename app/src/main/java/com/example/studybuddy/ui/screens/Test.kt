package com.example.studybuddy.ui.screens

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studybuddy.ui.icons.Light_mode

@Preview(showBackground = true)
@Composable
fun LightModeIconExample() {
    Icon(
        imageVector = Light_mode,
        contentDescription = "Light Mode Icon",
        modifier = Modifier.size(24.dp) // Optionally set size
    )
}