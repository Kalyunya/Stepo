package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper

class WeightPickerActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeightAdapter
    private lateinit var selectedWeightText: TextView

    private lateinit var btnKg: Button
    private lateinit var btnLb: Button

    private var weightList = (10..300).toList()
    private var selectedWeight = 75

    private var isKg = true

    private var email: String? = null
    private var fullName: String? = null
    private var gender: String? = null
    private var fitnessRoutine: String? = null
    private var selectedTime: String? = null
    private var selectedGoals: ArrayList<String>? = null
    private var age: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_picker)

        email = intent.getStringExtra(Constants.EXTRA_EMAIL)
        fullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME)
        gender = intent.getStringExtra(Constants.EXTRA_GENDER)
        fitnessRoutine = intent.getStringExtra(Constants.EXTRA_FITNESS_ROUTINE)
        selectedTime = intent.getStringExtra(Constants.EXTRA_SELECTED_TIME)
        selectedGoals = intent.getStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS)
        age = intent.getIntExtra(Constants.EXTRA_AGE, 28)

        recyclerView = findViewById(R.id.weightPicker)
        selectedWeightText = findViewById(R.id.selectedWeight)
        btnKg = findViewById(R.id.btnKg)
        btnLb = findViewById(R.id.btnLb)

        adapter = WeightAdapter(weightList) { weight ->
            selectedWeight = weight
            updateDisplayedWeight(weight)
        }

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = adapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.scrollToPosition(weightList.indexOf(selectedWeight))

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val view = snapHelper.findSnapView(recyclerView.layoutManager)
                    val position = recyclerView.getChildAdapterPosition(view!!)
                    selectedWeight = weightList[position]
                    updateDisplayedWeight(selectedWeight)
                }
            }
        })

        btnKg.setOnClickListener {
            isKg = true
            updateUnitButtons()
            updateDisplayedWeight(selectedWeight)
        }

        btnLb.setOnClickListener {
            isKg = false
            updateUnitButtons()
            updateDisplayedWeight(selectedWeight)
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            val intent = Intent(this, HeightPickerActivity::class.java).apply {
                putExtra(Constants.EXTRA_EMAIL, email)
                putExtra(Constants.EXTRA_FULL_NAME, fullName)
                putExtra(Constants.EXTRA_GENDER, gender)
                putExtra(Constants.EXTRA_FITNESS_ROUTINE, fitnessRoutine)
                putExtra(Constants.EXTRA_SELECTED_TIME, selectedTime)
                putStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS, selectedGoals)
                putExtra(Constants.EXTRA_AGE, age)
                putExtra(Constants.EXTRA_WEIGHT, selectedWeight)
            }
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, OldPickerActivity::class.java).apply {
                putExtra(Constants.EXTRA_EMAIL, email)
                putExtra(Constants.EXTRA_FULL_NAME, fullName)
                putExtra(Constants.EXTRA_GENDER, gender)
                putExtra(Constants.EXTRA_FITNESS_ROUTINE, fitnessRoutine)
                putExtra(Constants.EXTRA_SELECTED_TIME, selectedTime)
                putStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS, selectedGoals)
                putExtra(Constants.EXTRA_AGE, age)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun updateDisplayedWeight(weight: Int) {
        val text = if (isKg) {
            "$weight kg"
        } else {
            "${(weight * 2.20462).toInt()} lb"
        }
        selectedWeightText.text = text
    }

    private fun updateUnitButtons() {
        if (isKg) {
            btnKg.setBackgroundColor(resources.getColor(android.R.color.white))
            btnKg.setTextColor(resources.getColor(android.R.color.black))

            btnLb.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
            btnLb.setTextColor(resources.getColor(android.R.color.white))
        } else {
            btnLb.setBackgroundColor(resources.getColor(android.R.color.white))
            btnLb.setTextColor(resources.getColor(android.R.color.black))

            btnKg.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
            btnKg.setTextColor(resources.getColor(android.R.color.white))
        }
    }
}
