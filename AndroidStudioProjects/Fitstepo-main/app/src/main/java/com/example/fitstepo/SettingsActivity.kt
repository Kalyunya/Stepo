package com.example.fitstepo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var imgAvatar: ImageView
    private lateinit var tvFullName: TextView
    private lateinit var tvEmail: TextView

    private lateinit var dbHelper: DBHelper

    private var email: String? = null
    private var fullName: String? = null
    private var avatar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        imgAvatar = findViewById(R.id.imgAvatar)
        tvFullName = findViewById(R.id.tvFullName)
        tvEmail = findViewById(R.id.tvEmail)

        dbHelper = DBHelper(this)

        // Отримуємо email з інтенту
        email = intent.getStringExtra(Constants.EXTRA_EMAIL)

        if (email != null) {
            val user = dbHelper.getUserByEmail(email!!)
            if (user != null) {
                fullName = user.fullName
                avatar = user.avatar

                tvFullName.text = fullName
                tvEmail.text = email

//                if (!avatar.isNullOrEmpty() && avatar!!.startsWith("content://")) {
//                    imgAvatar.setImageURI(Uri.parse(avatar))
//                } else {
//                    imgAvatar.setImageResource(R.drawable.default_avatar)
//                }

                AvatarUtils.loadUserAvatar(this, email, imgAvatar)

            } else {
                Toast.makeText(this, "Користувача не знайдено", Toast.LENGTH_SHORT).show()
            }
        }

//        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        // Назад
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_EMAIL, email) // ← ОБОВ’ЯЗКОВО
            startActivity(intent)
            finish()
        }




        // Кнопка More
        val btnMore = findViewById<ImageButton>(R.id.btnMore)
        btnMore.setOnClickListener {
            Toast.makeText(this, "More options clicked", Toast.LENGTH_SHORT).show()
        }

        // Перехід до редагування профілю
        val accountSection = findViewById<View>(R.id.accountSection)
        accountSection.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_EMAIL, email)
            startActivity(intent)
        }

        // Меню переходів
        val locationItem = findViewById<RelativeLayout>(R.id.itemLocation)
        val notificationItem = findViewById<RelativeLayout>(R.id.itemNotification)
        val closeFriendsItem = findViewById<RelativeLayout>(R.id.itemCloseFriends)
        val soundsItem = findViewById<RelativeLayout>(R.id.itemSounds)
        val espItem = findViewById<RelativeLayout>(R.id.itemESP)

        locationItem.setOnClickListener {
            val intent = Intent(this, Frame18Activity::class.java)
            passUserData(intent)
            startActivity(intent)
        }

        notificationItem.setOnClickListener {
            val intent = Intent(this, NotificationsActivity::class.java)
            passUserData(intent)
            startActivity(intent)
        }

        closeFriendsItem.setOnClickListener {
            val intent = Intent(this, CloseFriendsActivity::class.java)
            passUserData(intent)
            startActivity(intent)
        }

        soundsItem.setOnClickListener {
            val intent = Intent(this, SoundsActivity::class.java)
            passUserData(intent)
            startActivity(intent)
        }
        espItem.setOnClickListener {
            val intent = Intent(this, EspDataActivity::class.java)
            passUserData(intent) // якщо потрібно передати email, fullName, avatar
            startActivity(intent)
        }
    }

    private fun passUserData(intent: Intent) {
        intent.putExtra(Constants.EXTRA_EMAIL, email)
        intent.putExtra(Constants.EXTRA_FULL_NAME, fullName)
        intent.putExtra(Constants.EXTRA_AVATAR, avatar)
    }



}
