package com.example.studybuddy.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material3.ModalDrawerSheet
import com.example.studybuddy.ui.screens.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class NavigationItems(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String,
    val screen: @Composable () -> Unit
)

// Export danh sách điều hướng
val navItems = listOf(
    NavigationItems(
        "Home",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        "home",
        screen = { HomeScreen() }
    ),
    NavigationItems(
        "To-Do List",
        Icons.Filled.Edit,
        Icons.Outlined.Edit,
        "todo",
        screen = { TodoListScreen() }
    ),
    NavigationItems(
        "Settings",
        Icons.Filled.Settings,
        Icons.Outlined.Settings,
        "settings",
        screen = { SettingsScreen() }
    )
)

@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: androidx.compose.material3.DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet {
        navItems.forEach { item ->
            NavigationDrawerItem(
                label = { androidx.compose.material3.Text(text = item.title) },
                selected = false, // Bạn có thể thêm logic để làm nổi bật item được chọn
                onClick = {
                    scope.launch { drawerState.close() }
                    navController.navigate(item.route)
                },
                icon = {
                    androidx.compose.material3.Icon(
                        imageVector = item.selectedIcon,
                        contentDescription = item.title
                    )
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}
