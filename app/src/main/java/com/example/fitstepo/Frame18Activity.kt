package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior

class Frame18Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<android.view.View>
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame18)

        // 🔹 Знаходимо кнопку Назад
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        // 🔹 Отримання даних з Intent
        val fullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME) ?: "User"
        val email = intent.getStringExtra(Constants.EXTRA_EMAIL)
        val avatar = intent.getStringExtra(Constants.EXTRA_AVATAR)

        // 🔹 Кнопка Назад
        btnBack.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(Constants.EXTRA_EMAIL, email)
            resultIntent.putExtra(Constants.EXTRA_FULL_NAME, fullName)
            resultIntent.putExtra(Constants.EXTRA_AVATAR, avatar)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        // ⬇️ Встановлення імені
        val nameTextView = findViewById<TextView>(R.id.helloText)
        nameTextView.text = "Hello $fullName!"

        // ⬇️ Встановлення аватара
        val avatarImageView = findViewById<ImageView>(R.id.profileImage)
        if (email != null) {
            AvatarUtils.loadUserAvatar(this, email, avatarImageView)
        } else {
            avatarImageView.setImageResource(R.drawable.default_avatar)
        }

        // ⬇️ BottomSheet логіка
        val bottomSheet: android.view.View = findViewById(R.id.bottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.peekHeight = 200
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.isHideable = false

        val screenHeight = resources.displayMetrics.heightPixels
        val maxHeight = (screenHeight * 0.75).toInt()
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: android.view.View, slideOffset: Float) {
                if (bottomSheet.height > maxHeight) {
                    bottomSheet.layoutParams.height = maxHeight
                    bottomSheet.requestLayout()
                }
            }

            override fun onStateChanged(bottomSheet: android.view.View, newState: Int) {
                // нічого не робимо
            }
        })

        // ⬇️ Ініціалізація карти
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val kyiv = LatLng(50.4501, 30.5234)
        map.addMarker(MarkerOptions().position(kyiv).title("Київ"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(kyiv, 10f))
    }
}
