package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Frame4Activity : AppCompatActivity() {

    private var selectedAnswer: String? = null
    private var email: String? = null
    private var fullName: String? = null
    private var gender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame4)

        email = intent.getStringExtra(Constants.EXTRA_EMAIL)
        fullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME)
        gender = intent.getStringExtra(Constants.EXTRA_GENDER)

        val optionYes = findViewById<LinearLayout>(R.id.optionYes)
        val optionNo = findViewById<LinearLayout>(R.id.optionNo)

        val checkYes = findViewById<ImageView>(R.id.checkYes)
        val checkNo = findViewById<ImageView>(R.id.checkNo)

        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnNext = findViewById<Button>(R.id.btnNext)

        optionYes.setOnClickListener {
            selectedAnswer = "Yes"
            checkYes.visibility = View.VISIBLE
            checkNo.visibility = View.GONE
            optionYes.alpha = 1.0f
            optionNo.alpha = 0.5f
        }

        optionNo.setOnClickListener {
            selectedAnswer = "No"
            checkNo.visibility = View.VISIBLE
            checkYes.visibility = View.GONE
            optionNo.alpha = 1.0f
            optionYes.alpha = 0.5f
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnNext.setOnClickListener {
            if (selectedAnswer != null && email != null && fullName != null && gender != null) {
                val intent = Intent(this, Frame5Activity::class.java).apply {
                    putExtra(Constants.EXTRA_EMAIL, email)
                    putExtra(Constants.EXTRA_FULL_NAME, fullName)
                    putExtra(Constants.EXTRA_GENDER, gender)
                    putExtra(Constants.EXTRA_FITNESS_ROUTINE, selectedAnswer)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Будь ласка, виберіть варіант", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
