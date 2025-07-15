package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class TimePickerActivity : AppCompatActivity() {

    private lateinit var pickerHour: NumberPicker
    private lateinit var pickerMinute: NumberPicker
    private lateinit var pickerAmPm: NumberPicker
    private lateinit var radio12Hour: RadioButton
    private lateinit var radio24Hour: RadioButton

    private var email: String? = null
    private var fullName: String? = null
    private var gender: String? = null
    private var fitnessRoutine: String? = null
    private var selectedGoals: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_picker)

        email = intent.getStringExtra(Constants.EXTRA_EMAIL)
        fullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME)
        gender = intent.getStringExtra(Constants.EXTRA_GENDER)
        fitnessRoutine = intent.getStringExtra(Constants.EXTRA_FITNESS_ROUTINE)
        selectedGoals = intent.getStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS)

        pickerHour = findViewById(R.id.pickerHour)
        pickerMinute = findViewById(R.id.pickerMinute)
        pickerAmPm = findViewById(R.id.pickerAmPm)
        radio12Hour = findViewById(R.id.radio12Hour)
        radio24Hour = findViewById(R.id.radio24Hour)

        val btnNext = findViewById<Button>(R.id.btnNext)
        val btnBack = findViewById<Button>(R.id.btnBack)

        setupPickers(use12Hour = true)

        radio12Hour.setOnClickListener { setupPickers(true) }
        radio24Hour.setOnClickListener { setupPickers(false) }

        btnNext.setOnClickListener {
            val hour = pickerHour.value
            val minute = pickerMinute.value
            val amPm = if (pickerAmPm.visibility == View.VISIBLE)
                pickerAmPm.displayedValues[pickerAmPm.value]
            else ""

            val timeString = if (amPm.isNotEmpty())
                String.format("%02d:%02d %s", hour, minute, amPm)
            else
                String.format("%02d:%02d", hour, minute)

            val intent = Intent(this, OldPickerActivity::class.java).apply {
                putExtra(Constants.EXTRA_EMAIL, email)
                putExtra(Constants.EXTRA_FULL_NAME, fullName)
                putExtra(Constants.EXTRA_GENDER, gender)
                putExtra(Constants.EXTRA_FITNESS_ROUTINE, fitnessRoutine)
                putStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS, selectedGoals)
                putExtra(Constants.EXTRA_SELECTED_TIME, timeString)
            }
            startActivity(intent)
            finish()
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, Frame5Activity::class.java).apply {
                putExtra(Constants.EXTRA_EMAIL, email)
                putExtra(Constants.EXTRA_FULL_NAME, fullName)
                putExtra(Constants.EXTRA_GENDER, gender)
                putExtra(Constants.EXTRA_FITNESS_ROUTINE, fitnessRoutine)
                putStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS, selectedGoals)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun setupPickers(use12Hour: Boolean) {
        pickerMinute.minValue = 0
        pickerMinute.maxValue = 59
        pickerMinute.setFormatter { String.format("%02d", it) }

        if (use12Hour) {
            pickerHour.minValue = 1
            pickerHour.maxValue = 12
            pickerHour.setFormatter { String.format("%02d", it) }

            pickerAmPm.visibility = View.VISIBLE
            pickerAmPm.minValue = 0
            pickerAmPm.maxValue = 1
            pickerAmPm.displayedValues = arrayOf("AM", "PM")
        } else {
            pickerHour.minValue = 0
            pickerHour.maxValue = 23
            pickerHour.setFormatter { String.format("%02d", it) }

            pickerAmPm.visibility = View.GONE
        }
    }
}
