package com.example.fitstepo

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService

class LoginActivity : AppCompatActivity() {

    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        database = DBHelper(this).readableDatabase

        val etEmail = findViewById<EditText>(R.id.etLoginEmail)
        val etPassword = findViewById<EditText>(R.id.etLoginPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)

        btnSignIn.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast("Заповніть усі поля")
                return@setOnClickListener
            }

            val cursor: Cursor = database.rawQuery(
                "SELECT * FROM Users WHERE email = ? AND password = ?",
                arrayOf(email, password)
            )

            if (cursor.moveToFirst()) {
                val fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName"))
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra(Constants.EXTRA_EMAIL, email)
                intent.putExtra(Constants.EXTRA_FULL_NAME, fullName)
                startActivity(intent)
                finish()
            } else {
                showToast("Невірний email або пароль")
            }
            cursor.close()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService<InputMethodManager>()
            imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            currentFocus?.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        super.onDestroy()
        database.close()
    }
}
