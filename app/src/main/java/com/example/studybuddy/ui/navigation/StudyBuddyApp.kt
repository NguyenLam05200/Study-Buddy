package com.example.studybuddy.ui.navigation

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.FrameLayout
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.data.ThemePreference
import com.example.studybuddy.ui.screens.CourseFragment
import com.example.studybuddy.ui.theme.StudyBuddyTheme
import kotlinx.coroutines.launch
import com.example.studybuddy.ui.components.FragmentContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyBuddyApp() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Quan sát trạng thái từ DataStore
    val isDarkMode by ThemePreference.isDarkMode(context).collectAsState(initial = false)

    Log.d("StudyBuddyApp", "isDarkMode: $isDarkMode")
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
                    StudyBuddyTopBar(
                        title = "Study Buddy",
                        onMenuClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }
                    )

//                    TopAppBar(
//                        title = { Text("Study Buddy") },
//                        navigationIcon = {
//                            IconButton(onClick = {
//                                scope.launch {
//                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
//                                }
//                            }) {
//                                Icon(Icons.Default.Menu, contentDescription = "Menu")
//                            }
//                        }
//                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "todo",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    navItems.forEach { item ->
                        if (item.route == "courses") {
                            // Handle Fragment-based screen
                            composable(item.route) {
//                                FragmentContainer(fragmentClass = CourseFragment::class.java, modifier = Modifier.padding(8.dp))
                            FragmentContainer(fragmentClass = CourseFragment::class.java)
                            }

                        } else {
                            // Handle Compose-based screens
                            composable(item.route) {
                                item.screen?.invoke(Modifier.padding(8.dp))
                            }
                        }

                    }
                }
            }
        }
    }
}
