package com.example.studybuddy

import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import android.widget.Switch
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.studybuddy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val headerView = navView.getHeaderView(0) // Lấy header layout

        // Lấy Switch từ header layout
        val switchButton = headerView.findViewById<Switch>(R.id.switchButton)

        // Sử dụng SharedPreferences
        val sharedPreferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Khởi tạo trạng thái Switch dựa trên SharedPreferences
        val isDarkMode = sharedPreferences.getBoolean(SWITCH_BUTTON_KEY, false)
        switchButton.isChecked = isDarkMode
        updateUIMode(isDarkMode)

        // Lắng nghe sự kiện thay đổi trạng thái của Switch
        switchButton.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean(SWITCH_BUTTON_KEY, isChecked).apply()
            updateUIMode(isChecked)
        }

        // Đặt layout padding theo SystemBars
        ViewCompat.setOnApplyWindowInsetsListener(headerView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val layoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = systemBars.top
            v.layoutParams = layoutParams

            insets

        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun updateUIMode(isDarkMode: Boolean) {
        val currentMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        val newMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        if ((currentMode == android.content.res.Configuration.UI_MODE_NIGHT_YES && !isDarkMode) ||
            (currentMode == android.content.res.Configuration.UI_MODE_NIGHT_NO && isDarkMode)) {
            AppCompatDelegate.setDefaultNightMode(newMode)
        }
    }


    companion object {
        const val SWITCH_BUTTON_KEY = "switch"
        const val PREF_KEY = "pref"
    }

}

