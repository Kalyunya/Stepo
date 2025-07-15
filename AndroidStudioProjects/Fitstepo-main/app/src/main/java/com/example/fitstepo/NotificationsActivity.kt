package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val recycler = findViewById<RecyclerView>(R.id.recyclerNotifications)

        // Повернення на SettingsActivity
        btnBack.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
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
