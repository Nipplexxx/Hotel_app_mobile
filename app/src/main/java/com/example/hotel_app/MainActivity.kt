package com.example.hotel_app

import android.os.Bundle
import android.view.Menu
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

        // Получаем почту пользователя из Intent
        val userEmail = intent.getStringExtra("userEmail")

        // Передаем почту пользователя в BookingsFragment
        val bookingsFragment = BookingsFragment()
        val bundle = Bundle()
        bundle.putString("userEmail", userEmail)
        bookingsFragment.arguments = bundle
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Извлекать пользовательские данные из Intent extras
        intent.getStringExtra("userId")
        intent.getStringExtra("login")
        intent.getStringExtra("password")
        intent.getStringExtra("role")

        // Передача каждого идентификатора меню в виде набора идентификаторов
        // меню следует рассматривать как пункты назначения высшего уровня.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Расширьте меню; это добавит элементы на панель действий, если она присутствует.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}