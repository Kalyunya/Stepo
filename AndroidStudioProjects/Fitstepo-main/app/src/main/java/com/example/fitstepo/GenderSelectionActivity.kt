package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class GenderSelectionActivity : AppCompatActivity() {

    private var selectedGender: String? = null
    private var email: String? = null
    private var fullName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gender_selection)

        email = intent.getStringExtra(Constants.EXTRA_EMAIL)
        fullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME)

        // 🟢 ТЕПЕР LinearLayout — не ImageView!
        val maleCard = findViewById<LinearLayout>(R.id.cardMale)
        val femaleCard = findViewById<LinearLayout>(R.id.cardFemale)

        val checkMale = findViewById<ImageView>(R.id.checkMale)
        val checkFemale = findViewById<ImageView>(R.id.checkFemale)

        val btnNext = findViewById<Button>(R.id.btnNext)

        maleCard.setOnClickListener {
            selectedGender = "Male"
            checkMale.visibility = View.VISIBLE
            checkFemale.visibility = View.GONE
            maleCard.alpha = 1.0f
            femaleCard.alpha = 0.5f
        }

        femaleCard.setOnClickListener {
            selectedGender = "Female"
            checkFemale.visibility = View.VISIBLE
            checkMale.visibility = View.GONE
            femaleCard.alpha = 1.0f
            maleCard.alpha = 0.5f
        }

        btnNext.setOnClickListener {
            if (selectedGender != null && email != null && fullName != null) {
                val intent = Intent(this, Frame4Activity::class.java).apply {
                    putExtra(Constants.EXTRA_EMAIL, email)
                    putExtra(Constants.EXTRA_FULL_NAME, fullName)
                    putExtra(Constants.EXTRA_GENDER, selectedGender)
                }
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Будь ласка, виберіть стать", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
