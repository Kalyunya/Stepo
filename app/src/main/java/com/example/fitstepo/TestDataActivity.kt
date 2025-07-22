package com.example.fitstepo;

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class TestDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this)
        setContentView(textView)

        val url = "http://192.168.1.105/data" // <-- IP ESP32

        val requestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val temp = response.getDouble("temperature")
                val hum = response.getDouble("humidity")
                textView.text = "Темп: $temp °C\nВологість: $hum %"
            },
            { error ->
                textView.text = "Помилка: ${error.message}"
            }
        )

        requestQueue.add(request)
    }
}
