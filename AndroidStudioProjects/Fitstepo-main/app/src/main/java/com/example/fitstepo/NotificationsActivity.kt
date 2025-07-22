package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationsActivity : AppCompatActivity() {

    private var email: String? = null // ← ДОДАНО

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        // Зчитуємо email
        email = intent.getStringExtra(Constants.EXTRA_EMAIL)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val recycler = findViewById<RecyclerView>(R.id.recyclerNotifications)

        // КНОПКА НАЗАД → до SettingsActivity
        btnBack.setOnClickListener {
//            Toast.makeText(this, "Назад натиснуто", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra(Constants.EXTRA_EMAIL, email)
            startActivity(intent)
            finish()
        }


        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = NotificationAdapter(getFakeNotifications())
    }

    private fun getFakeNotifications(): List<NotificationItem> {
        return listOf(
            NotificationItem("CaFit", "Your Duel with Alex will start today. Be well prepared to participate 💪", "02:01 PM", R.drawable.ic_cafit),
            NotificationItem("Alina Jen", "Started following you!", "10:32 AM", R.drawable.avatar_alina),
            NotificationItem("Monica Kim", "Started following you!", "10:32 AM", R.drawable.avatar_monica),
            NotificationItem("CaFit", "Your Duel with Alex will start today. Be well prepared to participate 💪", "02:01 PM", R.drawable.ic_cafit)
        )
    }
}
