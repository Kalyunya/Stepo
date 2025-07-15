package com.example.fitstepo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var imgAvatar: ImageView
    private lateinit var etDob: EditText
    private lateinit var etFullName: EditText
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Зв'язування елементів
        imgAvatar = findViewById(R.id.imgAvatar)
        etDob = findViewById(R.id.etDob)
        etFullName = findViewById(R.id.etFullName)
        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnChangeAvatar = findViewById<ImageButton>(R.id.btnChangeAvatar)

        // Назад
        btnBack.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }

        // Вибір дати
        etDob.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(this, { _, y, m, d ->
                etDob.setText("${getMonthName(m)} $d, $y")
            }, year, month, day)
            dpd.show()
        }

        // Обробка зміни аватару
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imgAvatar.setImageURI(it)
                // TODO: Зберегти URI або картинку в базу (опціонально)
            }
        }

        btnChangeAvatar.setOnClickListener {
            pickImage.launch("image/*")
        }

        // Збереження (TODO: реалізувати)
        btnSave.setOnClickListener {
            val name = etFullName.text.toString()
            val dob = etDob.text.toString()
            val height = etHeight.text.toString()
            val weight = etWeight.text.toString()

            Toast.makeText(this, "Saved: $name", Toast.LENGTH_SHORT).show()
            // TODO: Зберегти в SQLite
        }
    }

    private fun getMonthName(month: Int): String {
        return listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")[month]
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
