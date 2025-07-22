package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OldPickerActivity : AppCompatActivity() {

    private lateinit var selectedAgeText: TextView
    private lateinit var ageRecyclerView: RecyclerView
    private lateinit var adapter: AgeAdapter
    private lateinit var btnBack: Button
    private lateinit var btnNext: Button

    private var selectedAge: Int = 28

    private var email: String? = null
    private var fullName: String? = null
    private var gender: String? = null
    private var fitnessRoutine: String? = null
    private var selectedTime: String? = null
    private var selectedGoals: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_picker)

        email = intent.getStringExtra(Constants.EXTRA_EMAIL)
        fullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME)
        gender = intent.getStringExtra(Constants.EXTRA_GENDER)
        fitnessRoutine = intent.getStringExtra(Constants.EXTRA_FITNESS_ROUTINE)
        selectedTime = intent.getStringExtra(Constants.EXTRA_SELECTED_TIME)
        selectedGoals = intent.getStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS)

        selectedAgeText = findViewById(R.id.selectedAge)
        ageRecyclerView = findViewById(R.id.ageRecyclerView)
        btnBack = findViewById(R.id.btnBack)
        btnNext = findViewById(R.id.btnNext)

        val ageList = (10..80).toList()

        adapter = AgeAdapter(ageList) { age ->
            selectedAge = age
            selectedAgeText.text = age.toString()
        }

        ageRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        ageRecyclerView.adapter = adapter

        val defaultIndex = ageList.indexOf(selectedAge)
        ageRecyclerView.scrollToPosition(defaultIndex)

        btnBack.setOnClickListener {
            val intent = Intent(this, TimePickerActivity::class.java).apply {
                putExtra(Constants.EXTRA_EMAIL, email)
                putExtra(Constants.EXTRA_FULL_NAME, fullName)
                putExtra(Constants.EXTRA_GENDER, gender)
                putExtra(Constants.EXTRA_FITNESS_ROUTINE, fitnessRoutine)
                putExtra(Constants.EXTRA_SELECTED_TIME, selectedTime)
                putStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS, selectedGoals)
            }
            startActivity(intent)
            finish()
        }

        btnNext.setOnClickListener {
            val intent = Intent(this, WeightPickerActivity::class.java).apply {
                putExtra(Constants.EXTRA_EMAIL, email)
                putExtra(Constants.EXTRA_FULL_NAME, fullName)
                putExtra(Constants.EXTRA_GENDER, gender)
                putExtra(Constants.EXTRA_FITNESS_ROUTINE, fitnessRoutine)
                putExtra(Constants.EXTRA_SELECTED_TIME, selectedTime)
                putStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS, selectedGoals)
                putExtra(Constants.EXTRA_AGE, selectedAge)
            }
            startActivity(intent)
            finish()
        }
    }
}
