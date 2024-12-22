package com.example.studybuddy.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.ui.screens.*
import com.example.studybuddy.ui.theme.BuddyTypography
import com.example.studybuddy.ui.theme.StudyBuddyTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.studybuddy.R


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

@Preview(showBackground = true)
@Composable
fun PreviewDrawerContent() {
    StudyBuddyTheme { // Sử dụng theme của ứng dụng
        val fakeNavController = rememberNavController()
        val fakeDrawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
        val fakeScope = rememberCoroutineScope()

        DrawerContent(
            navController = fakeNavController,
            drawerState = fakeDrawerState,
            scope = fakeScope
        )
    }
}


@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: androidx.compose.material3.DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet {
        DrawerHeader()

        navItems.forEach { item ->
            NavigationDrawerItem(
                label = { androidx.compose.material3.Text(text = item.title) },
                selected = navController.currentBackStackEntry?.destination?.route == item.route,
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

@Preview(showBackground = true)
@Composable
fun DrawerHeader() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Replace with your app icon resource
//                contentDescription = "App Icon",
//                tint = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.size(64.dp) // Adjust width/height of the Icon
//            )
            Icon(
                imageVector = Icons.Filled.Star, // Hoặc Icons.Outlined.Settings
                contentDescription = "Settings Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp) // Kích thước icon (tùy chỉnh)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Study Buddy",
                style = MaterialTheme.typography.titleLarge, // Bold style for app name
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline,
            thickness = 1.dp
        )







    }
}

