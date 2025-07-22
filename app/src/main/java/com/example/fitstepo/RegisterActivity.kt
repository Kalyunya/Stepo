package com.example.fitstepo

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Rect
import android.os.Bundle
import android.util.Patterns
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        database = DBHelper(this).writableDatabase

        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val checkTerms = findViewById<CheckBox>(R.id.checkTerms)
        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)

        btnCreateAccount.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            when {
                firstName.isEmpty() -> showToast("Enter your first name")
                lastName.isEmpty() -> showToast("Enter your last name")
                email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> showToast("Enter a valid email")
                password.length < 8 -> showToast("Password must be at least 8 characters")
                !checkTerms.isChecked -> showToast("You must agree to the terms")
                else -> {
                    val fullName = "$firstName $lastName"
                    val values = ContentValues().apply {
                        put("fullName", fullName)
                        put("email", email)
                        put("password", password)
                    }

                    try {
                        database.insertOrThrow("Users", null, values)
                        showToast("Account created successfully")
                        val intent = Intent(this, GenderSelectionActivity::class.java).apply {
                            putExtra(Constants.EXTRA_EMAIL, email)
                            putExtra(Constants.EXTRA_FULL_NAME, fullName)
                        }
                        startActivity(intent)
                        finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        if (e.message?.contains("UNIQUE", ignoreCase = true) == true) {
                            showToast("Користувач із таким Email вже існує")
                        } else {
                            showToast("Error: ${e.message}")
                        }
                    }
                }
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null && ev.action == MotionEvent.ACTION_DOWN) {
            val focusedView = currentFocus
            if (focusedView is EditText) {
                val outRect = Rect()
                focusedView.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    focusedView.clearFocus()
                    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(focusedView.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onDestroy() {
        super.onDestroy()
        database.close()
    }
}
