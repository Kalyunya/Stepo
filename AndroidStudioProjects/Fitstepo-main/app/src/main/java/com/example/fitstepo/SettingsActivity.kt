package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Назад
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // More (не чіпаємо)
        val btnMore = findViewById<ImageButton>(R.id.btnMore)
        btnMore.setOnClickListener {
            Toast.makeText(this, "More options clicked", Toast.LENGTH_SHORT).show()
        }

        // Перехід до редагування профілю
        val accountSection = findViewById<View>(R.id.accountSection)
        accountSection.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        // Кнопки меню
        val locationItem = findViewById<RelativeLayout>(R.id.itemLocation)
        val notificationItem = findViewById<RelativeLayout>(R.id.itemNotification)
        val closeFriendsItem = findViewById<RelativeLayout>(R.id.itemCloseFriends)
        val soundsItem = findViewById<RelativeLayout>(R.id.itemSounds)

        // ⬇️ Додаємо переходи
        locationItem.setOnClickListener {
            val intent = Intent(this, Frame18Activity::class.java) // ← activity_frame18.xml
            startActivity(intent)
        }

        notificationItem.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        closeFriendsItem.setOnClickListener {
            val intent = Intent(this, CloseFriendsActivity::class.java)
            startActivity(intent)
        }

        soundsItem.setOnClickListener {
            val intent = Intent(this, SoundsActivity::class.java)
            startActivity(intent)
        }
    }
}
