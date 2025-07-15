package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Frame5Activity : AppCompatActivity() {

    private val selectedGoals = mutableSetOf<String>()
    private var email: String? = null
    private var fullName: String? = null
    private var gender: String? = null
    private var selectedAnswer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame5)

        email = intent.getStringExtra(Constants.EXTRA_EMAIL)
        fullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME)
        gender = intent.getStringExtra(Constants.EXTRA_GENDER)
        selectedAnswer = intent.getStringExtra(Constants.EXTRA_FITNESS_ROUTINE)

        val goalOptions = listOf(
            Triple(findViewById<View>(R.id.optionWeightLoss), "Weight loss", R.id.optionWeightLoss),
            Triple(findViewById<View>(R.id.optionWeightGain), "Weight gain", R.id.optionWeightGain),
            Triple(findViewById<View>(R.id.optionMuscleBuilding), "Muscle building", R.id.optionMuscleBuilding),
            Triple(findViewById<View>(R.id.optionStrengthTraining), "Strength training", R.id.optionStrengthTraining),
            Triple(findViewById<View>(R.id.optionFlexibility), "Flexibility", R.id.optionFlexibility),
            Triple(findViewById<View>(R.id.optionEndurance), "Endurance", R.id.optionEndurance),
            Triple(findViewById<View>(R.id.optionBalance), "Balance", R.id.optionBalance),
            Triple(findViewById<View>(R.id.optionPress), "Press", R.id.optionPress)
        )

        for ((layout, label, _) in goalOptions) {
            val goalText = layout.findViewById<TextView>(R.id.goalText)
            val goalCheck = layout.findViewById<ImageView>(R.id.goalCheck)

            goalText.text = label

            layout.setOnClickListener {
                val isSelected = selectedGoals.contains(label)
                if (isSelected) {
                    selectedGoals.remove(label)
                    goalCheck.visibility = View.GONE
                } else {
                    selectedGoals.add(label)
                    goalCheck.visibility = View.VISIBLE
                }
            }
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            val intent = Intent(this, Frame4Activity::class.java).apply {
                putExtra(Constants.EXTRA_EMAIL, email)
                putExtra(Constants.EXTRA_FULL_NAME, fullName)
                putExtra(Constants.EXTRA_GENDER, gender)
                putExtra(Constants.EXTRA_FITNESS_ROUTINE, selectedAnswer)
            }
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            val intent = Intent(this, TimePickerActivity::class.java).apply {
                putExtra(Constants.EXTRA_EMAIL, email)
                putExtra(Constants.EXTRA_FULL_NAME, fullName)
                putExtra(Constants.EXTRA_GENDER, gender)
                putExtra(Constants.EXTRA_FITNESS_ROUTINE, selectedAnswer)
                putStringArrayListExtra(Constants.EXTRA_SELECTED_GOALS, ArrayList(selectedGoals))
            }
            startActivity(intent)
            finish()
        }
    }
}
