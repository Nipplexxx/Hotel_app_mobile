package com.example.hotel_app

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.hotel_app.databinding.ActivityMainBinding
import com.example.hotel_app.ui.bookings.BookingsFragment
import com.example.hotel_app.ui.viewing_rooms.ViewingRoomsFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("userEmail", "Гость") ?: "Гость"
        val userId = sharedPreferences.getString("userId", "Неизвестно") ?: "Неизвестно"

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        val headerView = navView.getHeaderView(0)
        val tvUserEmail = headerView.findViewById<TextView>(R.id.tvUserEmail)
        val tvUserId = headerView.findViewById<TextView>(R.id.tvUserId)

        tvUserEmail.text = "Email: $userEmail"
        tvUserId.text = "ID: $userId"

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (savedInstanceState == null) {
            val viewingRoomsFragment = ViewingRoomsFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, viewingRoomsFragment)
                .commit()
        }

        val bundle = Bundle().apply {
            putString("userId", userId)
        }
        val bookingsFragment = BookingsFragment()
        bookingsFragment.arguments = bundle

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragment_container) {
                bookingsFragment.arguments = bundle
            }
        }
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