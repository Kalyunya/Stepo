package com.example.fitstepo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior

class Frame18Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame18)

        val bottomSheet: View = findViewById(R.id.bottomSheet)
        val behavior = BottomSheetBehavior.from(bottomSheet)

        // Початковий стан
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

        // Опціонально: дозволити свайп
        behavior.isHideable = false
        behavior.peekHeight = 100 // Висота в пікселях для стану COLLAPSED
    }
}
