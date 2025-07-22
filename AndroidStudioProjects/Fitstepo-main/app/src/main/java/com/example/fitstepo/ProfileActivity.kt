package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    private lateinit var tvName: TextView
    private lateinit var imgAvatar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        tvName = findViewById(R.id.tvName)
        imgAvatar = findViewById(R.id.profileImage)

        // Отримання параметрів з Intent
        val email = intent.getStringExtra(Constants.EXTRA_EMAIL)
        val fullNameFromIntent = intent.getStringExtra(Constants.EXTRA_FULL_NAME)
        val avatarFromIntent = intent.getStringExtra(Constants.EXTRA_AVATAR)

        val dbHelper = DBHelper(this)
        val user = email?.let { dbHelper.getUserByEmail(it) }

        // Встановлення аватара
        if (!avatarFromIntent.isNullOrEmpty()) {
            AvatarUtils.setAvatarFromUri(this, imgAvatar, avatarFromIntent)
        } else if (user != null && !user.avatar.isNullOrEmpty()) {
            AvatarUtils.setAvatarFromUri(this, imgAvatar, user.avatar)
        } else {
            imgAvatar.setImageResource(R.drawable.profile_placeholder)
        }

        // Встановлення імені
        tvName.text = fullNameFromIntent ?: user?.fullName ?: "User"

        // Кнопка закриття (назад)
        val btnClose = findViewById<ImageButton>(R.id.btnClose)
        btnClose.setOnClickListener {
            val intent = Intent(this, TrainingStatisticsActivity::class.java)
            intent.putExtra(Constants.EXTRA_EMAIL, email)
            intent.putExtra(Constants.EXTRA_FULL_NAME, user?.fullName ?: fullNameFromIntent)
            intent.putExtra(Constants.EXTRA_AVATAR, user?.avatar ?: avatarFromIntent)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Кнопка налаштувань
        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra(Constants.EXTRA_EMAIL, email)
            intent.putExtra(Constants.EXTRA_FULL_NAME, user?.fullName ?: fullNameFromIntent)
            intent.putExtra(Constants.EXTRA_AVATAR, user?.avatar ?: avatarFromIntent)
            startActivity(intent)
        }

        // Кнопки секцій
        btnTimeline = findViewById(R.id.btnTimeline)
        btnStats = findViewById(R.id.btnStats)
        btnDuels = findViewById(R.id.btnDuels)

        layoutTimeline = findViewById(R.id.layoutTimeline)
        layoutStats = findViewById(R.id.layoutStats)
        layoutDuels = findViewById(R.id.layoutDuels)

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
                DuelItem(
                    title = "You vs Alex",
                    subtitle = "3 workouts",
                    score1 = "2246",
                    score2 = "6468",
                    iconResId = R.drawable.ic_cycle,
                    userEmail = email ?: "", // ← Передаємо email
                    avatar2 = R.drawable.avatar2
                )
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

        // За замовчуванням — Stats
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
                it.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black))
                it.setTextColor(ContextCompat.getColor(this, android.R.color.white))
            } else {
                it.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                it.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            }
        }
    }
}
