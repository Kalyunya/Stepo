package com.example.fitstepo

import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import java.io.File
import java.io.FileOutputStream
import java.util.*
import com.example.fitstepo.EspSender

class EditProfileActivity : AppCompatActivity() {

    private lateinit var imgAvatar: ShapeableImageView
    private lateinit var tvProfileName: TextView
    private lateinit var etDob: EditText
    private lateinit var etFullName: EditText
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText

    private lateinit var dbHelper: DBHelper
    private lateinit var db: SQLiteDatabase
    private var avatarUri: Uri? = null
    private var email: String? = null
    private var savedAvatarPath: String? = null // шлях до збереженого аватара

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        email = intent.getStringExtra(Constants.EXTRA_EMAIL)

        dbHelper = DBHelper(this)
        db = dbHelper.writableDatabase

        imgAvatar = findViewById(R.id.imgAvatar)
        tvProfileName = findViewById(R.id.tvProfileName)
        etDob = findViewById(R.id.etDob)
        etFullName = findViewById(R.id.etFullName)
        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)

        // Завантаження аватару
        AvatarUtils.loadUserAvatar(this, email, imgAvatar)

        email?.let {
            val user = dbHelper.getUserByEmail(it)
            user?.let { u ->
                etFullName.setText(u.fullName)
                etHeight.setText(u.height.toString())
                etWeight.setText(u.weight.toString())
                tvProfileName.text = u.fullName
            }
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnChangeAvatar = findViewById<ImageButton>(R.id.btnChangeAvatar)

        btnBack.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra(Constants.EXTRA_EMAIL, email)
            startActivity(intent)
            finish()
        }

        val btnLogout = findViewById<ImageButton>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            // Надсилання на ESP32 повідомлення "Good bay"
            EspSender.sendFullNameToEsp("Good bay")

            // Перехід на WelcomeActivity
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }


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

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                avatarUri = it
                imgAvatar.setImageURI(it)

                // ↓ Спробуємо зберегти вибране зображення у внутрішнє сховище
                savedAvatarPath = saveAvatarToInternalStorage(it)
            }
        }

        btnChangeAvatar.setOnClickListener {
            pickImage.launch("image/*")
        }

//        btnSave.setOnClickListener {
//            val name = etFullName.text.toString().trim()
//            val heightStr = etHeight.text.toString().trim()
//            val weightStr = etWeight.text.toString().trim()
//
//            if (name.isBlank() || heightStr.isBlank() || weightStr.isBlank()) {
//                Toast.makeText(this, "Будь ласка, заповніть всі поля", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val height = heightStr.toFloatOrNull() ?: 0f
//            val weight = weightStr.toFloatOrNull() ?: 0f
//
//            if (email != null) {
//                val values = ContentValues().apply {
//                    put("fullName", name)
//                    put("height", height)
//                    put("weight", weight)
//                    if (!savedAvatarPath.isNullOrEmpty()) {
//                        put("avatar", savedAvatarPath)
//                    }
//                }
//
//                val rows = db.update("Users", values, "email = ?", arrayOf(email))
//                if (rows > 0) {
//                    Toast.makeText(this, "Дані оновлено", Toast.LENGTH_SHORT).show()
//                    val intent = Intent(this, SettingsActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_EMAIL, email)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//                    startActivity(intent)
//                    finish()
//                } else {
//                    Toast.makeText(this, "Користувача не знайдено", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(this, "Помилка: email не передано", Toast.LENGTH_SHORT).show()
//            }
//        }

        btnSave.setOnClickListener {
            val name = etFullName.text.toString().trim()
            val heightStr = etHeight.text.toString().trim()
            val weightStr = etWeight.text.toString().trim()

            if (name.isBlank() || heightStr.isBlank() || weightStr.isBlank()) {
                Toast.makeText(this, "Будь ласка, заповніть всі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val height = heightStr.toFloatOrNull() ?: 0f
            val weight = weightStr.toFloatOrNull() ?: 0f

            if (email != null) {
                val values = ContentValues().apply {
                    put("fullName", name)
                    put("height", height)
                    put("weight", weight)
                }

                // 👇 ЛОГІКА ВИДАЛЕННЯ СТАРОГО АВАТАРА
                val user = dbHelper.getUserByEmail(email!!)
                val oldAvatar = user?.avatar
                if (!savedAvatarPath.isNullOrEmpty()) {
                    if (!oldAvatar.isNullOrEmpty() && oldAvatar.startsWith("/data")) {
                        File(oldAvatar).delete() // ← видаляємо старий файл
                    }
                    values.put("avatar", savedAvatarPath) // ← оновлюємо новий шлях
                }

                val rows = db.update("Users", values, "email = ?", arrayOf(email))
                if (rows > 0) {
                    Toast.makeText(this, "Дані оновлено", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SettingsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_EMAIL, email)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Користувача не знайдено", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Помилка: email не передано", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getMonthName(month: Int): String {
        return listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")[month]
    }

    private fun saveAvatarToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(this.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }

            val fileName = "avatar_${System.currentTimeMillis()}.png"
            val file = File(filesDir, fileName)
            val outStream = FileOutputStream(file)
            inputStream.compress(Bitmap.CompressFormat.PNG, 100, outStream)
            outStream.flush()
            outStream.close()

            file.absolutePath // ← записуємо цей шлях у БД
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = android.graphics.Rect()
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

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}
