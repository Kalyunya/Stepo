package com.example.fitstepo

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.fitstepo.EspSender


class HeightPickerActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HeightAdapter
    private lateinit var selectedHeightText: TextView

    private var heightList = (100..220).toList()
    private var selectedHeight = 165

    private var email: String? = null
    private var fullName: String? = null
    private var gender: String? = null
    private var fitnessRoutine: String? = null
    private var selectedTime: String? = null
    private var selectedGoals: ArrayList<String>? = null
    private var age: Int = 0
    private var weight: Int = 0

    private lateinit var database: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_height_picker)

        // Отримання даних з попередніх екранів
        email = intent.getStringExtra(Constants.EXTRA_EMAIL)
        fullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME)
        gender = intent.getStringExtra(Constants.EXTRA_GENDER)
        fitnessRoutine = intent.getStringExtra(Constants.EXTRA_FITNESS_ROUTINE)
        selectedTime = intent.getStringExtra(Constants.EXTRA_SELECTED_TIME)
        selectedGoals = intent.getStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS)
        age = intent.getIntExtra(Constants.EXTRA_AGE, 0)
        weight = intent.getIntExtra(Constants.EXTRA_WEIGHT, 0)

        // Ініціалізація бази
        database = DBHelper(this).writableDatabase

        recyclerView = findViewById(R.id.heightPicker)
        selectedHeightText = findViewById(R.id.selectedHeight)

        adapter = HeightAdapter(heightList) { height ->
            selectedHeight = height
            selectedHeightText.text = "$height cm"
        }

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = adapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        recyclerView.scrollToPosition(heightList.indexOf(selectedHeight))

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val view = snapHelper.findSnapView(recyclerView.layoutManager)
                    val position = recyclerView.getChildAdapterPosition(view!!)
                    selectedHeight = heightList[position]
                    selectedHeightText.text = "$selectedHeight cm"
                }
            }
        })

        findViewById<Button>(R.id.btnFinish).setOnClickListener {
            if (email != null && fullName != null) {
                val values = ContentValues().apply {
                    put("fullName", fullName)
                    put("email", email)
                    put("gender", gender)
                    put("age", age)
                    put("weight", weight)
                    put("height", selectedHeight)
                    put("goals", selectedGoals?.joinToString(", "))
                    put("time", selectedTime)
                    put("avatar", "default_avatar")
                }

                try {
                    val rows = database.update(
                        "Users",
                        values,
                        "email = ?",
                        arrayOf(email)
                    )

                    if (rows > 0) {
                        Toast.makeText(this, "Дані збережено", Toast.LENGTH_SHORT).show()

                        // 🔽 Додано: Надсилаємо ім’я користувача на ESP32
                        EspSender.sendFullNameToEsp(fullName!!)

                        val intent = Intent(this, TrainingStatisticsActivity::class.java)
                        intent.putExtra(Constants.EXTRA_EMAIL, email)
                        intent.putExtra(Constants.EXTRA_FULL_NAME, fullName)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Користувача не знайдено", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Помилка БД: ${e.message}", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(this, "Невірні або порожні дані (email/fullName)", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, WeightPickerActivity::class.java).apply {
                putExtra(Constants.EXTRA_EMAIL, email)
                putExtra(Constants.EXTRA_FULL_NAME, fullName)
                putExtra(Constants.EXTRA_GENDER, gender)
                putExtra(Constants.EXTRA_FITNESS_ROUTINE, fitnessRoutine)
                putExtra(Constants.EXTRA_SELECTED_TIME, selectedTime)
                putStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS, selectedGoals)
                putExtra(Constants.EXTRA_AGE, age)
                putExtra(Constants.EXTRA_WEIGHT, weight)
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        database.close()
    }
}
