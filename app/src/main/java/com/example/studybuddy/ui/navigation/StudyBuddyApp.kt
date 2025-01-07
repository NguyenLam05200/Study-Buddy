package com.example.studybuddy.ui.navigation

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.data.ThemePreference
import com.example.studybuddy.ui.theme.StudyBuddyTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyBuddyApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Quan sát trạng thái từ DataStore
    val isDarkMode by ThemePreference.isDarkMode(context).collectAsState(initial = false)

    StudyBuddyTheme(darkTheme = isDarkMode) {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent(
                    navController,
                    drawerState,
                    scope,
                    isDarkMode,
                    onThemeToggle = { isDark ->
                        scope.launch {
                            ThemePreference.saveDarkMode(context, isDark) // Lưu trạng thái
                        }
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Study Buddy") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "todo",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    navItems.forEach { item ->
                        composable(item.route) {
                            item.screen(Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }
    }
}
