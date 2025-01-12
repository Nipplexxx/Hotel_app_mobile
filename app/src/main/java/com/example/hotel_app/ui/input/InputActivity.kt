package com.example.hotel_app.ui.input

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hotel_app.MainActivity
import com.example.hotel_app.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class InputActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_input)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        findViewById<Button>(R.id.btnRegister)?.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnLogin)?.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val database = Firebase.database
            val usersRef = database.getReference("Users")

            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var userExists = false
                    var userRole: String? = null
                    var userId: String? = null

                    for (userSnapshot in snapshot.children) {
                        val userEmail = userSnapshot.child("email").getValue(String::class.java)
                        val userPassword = userSnapshot.child("password").getValue(String::class.java)
                        val role = userSnapshot.child("role").getValue(String::class.java)
                        val id = userSnapshot.key // Получаем ID пользователя из Firebase

                        if (userEmail == email && userPassword == password) {
                            userExists = true
                            userRole = role
                            userId = id
                            break
                        }
                    }

                    if (userExists) {
                        // Сохраняем данные пользователя в SharedPreferences
                        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("userEmail", email)
                        editor.putString("userId", userId)
                        editor.apply()

                        val intent = Intent(this@InputActivity, MainActivity::class.java)
                        intent.putExtra("userRole", userRole)
                        intent.putExtra("userEmail", email)
                        intent.putExtra("userId", userId) // передаем ID
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@InputActivity, "User not found. Please register first.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@InputActivity, "Error retrieving user data.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}