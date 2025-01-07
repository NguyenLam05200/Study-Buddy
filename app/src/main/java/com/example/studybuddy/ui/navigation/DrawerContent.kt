package com.example.studybuddy.ui.navigation

import Dark_mode
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
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
import com.example.studybuddy.data.ThemePreference
import com.example.studybuddy.ui.components.CustomSwitch
import com.example.studybuddy.ui.icons.Light_mode
import kotlinx.coroutines.flow.map


data class NavigationItems(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String,
    val screen: @Composable (Modifier) -> Unit // Modifier là optional
)

// Export danh sách điều hướng
val navItems = listOf(
    NavigationItems(
        "Home",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        "home",
        screen = { modifier -> HomeScreen(modifier) } // Modifier được truyền
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

@Preview("Drawer contents", showBackground = true)
@Preview("Drawer contents (dark)", showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewDrawerContent() {
    StudyBuddyTheme { // Sử dụng theme của ứng dụng
        val fakeNavController = rememberNavController()
        val fakeDrawerState =
            rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
        val fakeScope = rememberCoroutineScope()
        val context = LocalContext.current
        val isDarkMode by ThemePreference.isDarkMode(context).collectAsState(initial = false)


        DrawerContent(
            navController = fakeNavController,
            drawerState = fakeDrawerState,
            scope = fakeScope,
            isDarkMode = isDarkMode,
            onThemeToggle = { isDark ->
                fakeScope.launch {
                    ThemePreference.saveDarkMode(context, isDark) // Lưu trạng thái
                }
            }
        )
    }
}


@Composable
fun DrawerContent(
    navController: NavController,
    drawerState: androidx.compose.material3.DrawerState,
    scope: CoroutineScope,
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit // Callback để xử lý thay đổi theme
) {
    val currentRoute by navController.currentBackStackEntryFlow
        .map { it?.destination?.route ?: "home" }
        .collectAsState(initial = "home")


    Log.d("DrawerContent", "currentRoute: $currentRoute")

    ModalDrawerSheet {
        DrawerHeader(isDarkMode, onThemeToggle)

        navItems.forEach { item ->
            NavigationDrawerItem(
                label = { Text(text = item.title) },
                selected = (currentRoute == item.route),
                onClick = {
                    Log.d("Clicked item", "currentRoute: $currentRoute, item.route: ${item.route}")
                    if (currentRoute != item.route) {
                        scope.launch { drawerState.close() }
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
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

@Composable
fun DrawerHeader(
    isDarkMode: Boolean,
    onThemeToggle: (Boolean) -> Unit // Callback để xử lý thay đổi theme
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star, // Hoặc Icons.Outlined.Settings
                contentDescription = "Settings Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp) // Kích thước icon (tùy chỉnh)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Study Buddy",
                style = MaterialTheme.typography.titleMedium, // Bold style for app name
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f) // Đẩy Switch sang cuối dòng
            )

            // Add switch to toggle dark/light mode
//            CustomSwitchModeV0(isDarkMode, onThemeToggle)
//            CustomSwitchModeV1(isDarkMode, onThemeToggle)
            CustomSwitchModeV3(isDarkMode, onThemeToggle)

        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
    }
}

@Composable
fun CustomSwitchModeV0(isDarkMode: Boolean, onThemeToggle: (Boolean) -> Unit) {
    Switch(
        checked = isDarkMode,
        onCheckedChange = { onThemeToggle(it) }, // Gọi callback khi Switch thay đổi
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.primary,
            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            uncheckedThumbColor = MaterialTheme.colorScheme.onBackground,
            uncheckedTrackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )
    )
}

@Composable
fun CustomSwitchModeV1(isDarkMode: Boolean, onThemeToggle: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
//                    modifier = Modifier.size(50.dp, 30.dp), // Custom kích thước
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isDarkMode) Dark_mode else Light_mode,
                contentDescription = if (isDarkMode) "Dark Mode Icon" else "Light Mode Icon",
                tint = if (isDarkMode) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
//                Spacer(modifier = Modifier.width(0.dp))
        Switch(
            checked = isDarkMode,
            onCheckedChange = { onThemeToggle(it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                uncheckedTrackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            ),
            modifier = Modifier
                .padding(start = 8.dp)
                .size(50.dp, 30.dp) // Đảm bảo switch vừa với icon
        )
    }
}


@Composable
fun CustomSwitchModeV3(isDarkMode: Boolean, onThemeToggle: (Boolean) -> Unit) {
    CustomSwitch(
        height = 30.dp,
        width = 60.dp,
        circleButtonPadding = 4.dp,
        outerBackgroundOnResource = R.drawable.switch_body_night,
        outerBackgroundOffResource = R.drawable.switch_body_day,
        circleBackgroundOnResource = R.drawable.switch_btn_moon,
        circleBackgroundOffResource = R.drawable.switch_btn_sun,
        stateOn = 1,
        stateOff = 0,
        initialValue = if (isDarkMode) 1 else 0, // Set initial state based on isDarkMode
        onCheckedChanged = onThemeToggle
    )
}


