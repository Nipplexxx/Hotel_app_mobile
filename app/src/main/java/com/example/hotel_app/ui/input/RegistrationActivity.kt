package com.example.hotel_app.ui.input

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hotel_app.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var etPassportData: EditText
    private lateinit var etContactPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)

        // Проверяем существование элемента с ID main
        val mainView = findViewById<View>(R.id.main)
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        } else {
            Log.e("RegistrationActivity", "View with ID 'main' not found!")
        }

        etName = findViewById(R.id.etName)
        etSurname = findViewById(R.id.etSurname)
        etBirthDate = findViewById(R.id.etBirthDate)
        etPassportData = findViewById(R.id.etPassportData)
        etContactPhone = findViewById(R.id.etContactPhone)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnBack = findViewById(R.id.btnBack)

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val surname = etSurname.text.toString()
            val birthDate = etBirthDate.text.toString()
            val passportData = etPassportData.text.toString()
            val contactPhone = etContactPhone.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            val database = Firebase.database
            val usersRef = database.getReference("Users")
            val user = hashMapOf(
                "name" to name,
                "surname" to surname,
                "birthDate" to birthDate,
                "passportData" to passportData,
                "contactPhone" to contactPhone,
                "email" to email,
                "password" to password,
                "role" to "user"
            )
            usersRef.push().setValue(user)

            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}