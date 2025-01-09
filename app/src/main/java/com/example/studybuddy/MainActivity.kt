package com.example.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.studybuddy.data.ThemePreference
import com.example.studybuddy.ui.theme.StudyBuddyTheme
import com.example.studybuddy.ui.navigation.StudyBuddyApp

class MainActivity : AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        setContent {
            StudyBuddyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    StudyBuddyApp()
                }
            }
        }
    }
}
