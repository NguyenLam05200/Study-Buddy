package com.example.studybuddy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp

@Composable
fun DrawerContent(onDestinationClicked: (String) -> Unit) {
    Column {
        Text(text = "Navigation Drawer", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
        Divider()
        DrawerItem(icon = Icons.Default.Home, label = "Home") {
            onDestinationClicked("home")
        }
        DrawerItem(icon = Icons.Default.Settings, label = "Settings") {
            onDestinationClicked("settings")
        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    ListItem(
        leadingContent = {
            Icon(imageVector = icon, contentDescription = null)
        },
        headlineContent = {
            Text(text = label)
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    )
}
