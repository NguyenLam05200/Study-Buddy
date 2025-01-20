package com.example.studybuddy

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.studybuddy.databinding.NavigationBarBinding
import com.example.studybuddy.utilities.CONF
import com.example.studybuddy.utilities.PreferencesManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: NavigationBarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Kiểm tra quyền thông báo
        requestNotificationPermission()
//        requestStoragePermission()

        /* SharedPreferences */
        PreferencesManager.initialize(this) // this will need to be initialized once, no need to do again later
        val isDarkMode = PreferencesManager.getBoolean(CONF.SWITCH_BUTTON_KEY, false)
        val language = PreferencesManager.getString(CONF.LANGUAGE_KEY, "English")
        val language_code = getLanguageCode(language!!)
        //Log.d("LANGUAGE", "Language: ${language}, code: ${language_code}")

        updateUILanguage(language_code)

        /* View */
        binding = NavigationBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        /* XML Layout */
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val headerView = navView.getHeaderView(0) // Lấy header layout
        val switchButton = headerView.findViewById<Switch>(R.id.switchButton)

        // Khởi tạo trạng thái Switch dựa trên SharedPreferences
        switchButton.isChecked = isDarkMode
        updateUIMode(isDarkMode)

        // Lắng nghe sự kiện thay đổi trạng thái của Switch
        switchButton.setOnCheckedChangeListener { _, isChecked ->
            PreferencesManager.putBoolean(CONF.SWITCH_BUTTON_KEY, isChecked)
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
                R.id.home_frag,
                R.id.course_frag,
                R.id.settings_frag,
                R.id.todolist_frag,
                R.id.register_frag,
                R.id.login_frag,
                R.id.file_frag
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.screen_topbar_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // only works for supported languages
    private fun getLanguageCode(language: String): String {
        val pos = CONF.languages.indexOf(language)

        return CONF.language_codes[pos]
    }

    private fun updateUILanguage(language_code: String) {
        val locale = java.util.Locale(language_code)
        java.util.Locale.setDefault(locale)

        val config = android.content.res.Configuration(resources.configuration)
        config.setLocale(locale)

        // Update the configuration
        this.resources.updateConfiguration(
            config, this.resources.displayMetrics
        )
    }

    private fun updateUIMode(isDarkMode: Boolean) {
        val currentMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        val newMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO

        if ((currentMode == android.content.res.Configuration.UI_MODE_NIGHT_YES && !isDarkMode) ||
            (currentMode == android.content.res.Configuration.UI_MODE_NIGHT_NO && isDarkMode)
        ) {
            AppCompatDelegate.setDefaultNightMode(newMode)
        }
    }

    private fun requestStoragePermission() {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                CONF.REQUEST_STORAGE_PERMISSION
            )
        }
    }

//    private fun requestStoragePermissionSDK30() {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
//            if (Environment.isExternalStorageManager()) {
//
//            } else {
//                // Request the permission to manage external storage
//                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//                startActivityForResult(intent, CONF.STORAGE_REQUEST_CODE)
//            }
//        }
//    }

    private fun requestNotificationPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("NotificationPermission", "Notification Permission granted!")
            } else {
                Log.d("NotificationPermission", "Notification Permission denied!")
                Snackbar.make(
                    findViewById(R.id.drawer_layout),
                    "Notification permission denied!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        if (requestCode == CONF.REQUEST_STORAGE_PERMISSION)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("StoragePermission", "Storage Permission granted!")
            } else {
                Log.d("StoragePermission", "Storage Permission granted!")

                Snackbar.make(
                    findViewById(R.id.drawer_layout),
                    "Storage permission denied!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

//        if (requestCode == CONF.STORAGE_REQUEST_CODE)
//        {
//            if (Environment.isExternalStorageManager()) {
//                // Permission granted, proceed with file reading and copying
//            } else {
//                Log.d("StoragePermission", "Permission denied!")
//                Snackbar.make(
//                    findViewById(R.id.drawer_layout),
//                    "Storage permission denied!",
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }
//        }
    }
}

