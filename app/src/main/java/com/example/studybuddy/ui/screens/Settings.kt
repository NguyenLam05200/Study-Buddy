package com.example.studybuddy.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen() {
    Text(text = "Welcome to Settings Screen", modifier = Modifier.fillMaxSize())
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
