package com.example.hotel_app

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.hotel_app.databinding.ActivityMainBinding
import com.example.hotel_app.ui.bookings.BookingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Получаем данные пользователя из Intent
        val userEmail = intent.getStringExtra("userEmail") ?: "Гость"
        val userId = intent.getStringExtra("userId") ?: "Неизвестно"

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Обновляем TextView в заголовке меню
        val headerView = navView.getHeaderView(0)
        val tvUserEmail = headerView.findViewById<TextView>(R.id.tvUserEmail)
        val tvUserId = headerView.findViewById<TextView>(R.id.tvUserId)

        tvUserEmail.text = "Email: $userEmail" // Устанавливаем почту
        tvUserId.text = "ID: $userId" // Устанавливаем ID пользователя

        // Настройка верхнего уровня навигации
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}