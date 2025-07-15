package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {

    private lateinit var btnTimeline: Button
    private lateinit var btnStats: Button
    private lateinit var btnDuels: Button

    private lateinit var layoutTimeline: LinearLayout
    private lateinit var layoutStats: LinearLayout
    private lateinit var layoutDuels: LinearLayout

    private lateinit var timelineRecycler: RecyclerView
    private lateinit var duelsRecycler: RecyclerView

    private lateinit var tvName: TextView // ← додали змінну

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Отримання email з інтенту
        val email = intent.getStringExtra("email")

        // Отримання користувача з бази
        val dbHelper = DBHelper(this)
        val user = email?.let { dbHelper.getUserByEmail(it) }

        // Встановлення імені користувача
        tvName = findViewById(R.id.tvName)
        user?.let {
            tvName.text = it.fullName
        }

        btnTimeline = findViewById(R.id.btnTimeline)
        btnStats = findViewById(R.id.btnStats)
        btnDuels = findViewById(R.id.btnDuels)

        layoutTimeline = findViewById(R.id.layoutTimeline)
        layoutStats = findViewById(R.id.layoutStats)
        layoutDuels = findViewById(R.id.layoutDuels)

        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        timelineRecycler = findViewById(R.id.recyclerTimeline)
        timelineRecycler.layoutManager = LinearLayoutManager(this)
        timelineRecycler.adapter = TimelineAdapter(
            listOf(
                TimelineItem("Indoor Run", "24 min", "5.56 km", "348 kcal", R.drawable.ic_run),
                TimelineItem("Outdoor Cycle", "24 min", "4.22 km", "248 kcal", R.drawable.ic_cycle)
            )
        )

        duelsRecycler = findViewById(R.id.recyclerDuels)
        duelsRecycler.layoutManager = LinearLayoutManager(this)
        duelsRecycler.adapter = DuelAdapter(
            listOf(
                DuelItem("You vs Alex", "3 workouts", "2246", "6468", R.drawable.ic_cycle,
                    R.drawable.avatar1, R.drawable.avatar2)
            )
        )

        btnTimeline.setOnClickListener {
            showSection(layoutTimeline)
            highlightButton(btnTimeline)
        }

        btnStats.setOnClickListener {
            showSection(layoutStats)
            highlightButton(btnStats)
        }

        btnDuels.setOnClickListener {
            showSection(layoutDuels)
            highlightButton(btnDuels)
        }

        showSection(layoutStats)
        highlightButton(btnStats)
    }

    private fun showSection(section: LinearLayout) {
        layoutTimeline.visibility = View.GONE
        layoutStats.visibility = View.GONE
        layoutDuels.visibility = View.GONE
        section.visibility = View.VISIBLE
    }

    private fun highlightButton(activeButton: Button) {
        val buttons = listOf(btnTimeline, btnStats, btnDuels)
        buttons.forEach {
            if (it == activeButton) {
                it.setBackgroundColor(resources.getColor(android.R.color.black))
                it.setTextColor(resources.getColor(android.R.color.white))
            } else {
                it.setBackgroundColor(resources.getColor(android.R.color.transparent))
                it.setTextColor(resources.getColor(android.R.color.black))
            }
        }
    }
}
