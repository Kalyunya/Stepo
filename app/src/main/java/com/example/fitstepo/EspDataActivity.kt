package com.example.fitstepo

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class EspDataActivity : AppCompatActivity() {

    private lateinit var tvTemperature: TextView
    private lateinit var tvHumidity: TextView
    private lateinit var btnBack: ImageButton
    private var email: String? = null // ← ДОДАНО

    private val espUrl = "http://192.168.188.56/data" // ← Переконайся, що це IP твоєї ESP32
    private val client = OkHttpClient()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esp_data)

        email = intent.getStringExtra(Constants.EXTRA_EMAIL)

        tvTemperature = findViewById(R.id.tvTemperature)
        tvHumidity = findViewById(R.id.tvHumidity)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            intent.putExtra(Constants.EXTRA_EMAIL, email)
            startActivity(intent)
            finish()
        }

        startPeriodicUpdate()
    }

    private fun startPeriodicUpdate() {
        handler.post(object : Runnable {
            override fun run() {
                fetchSensorData()
                handler.postDelayed(this, 2000) // оновлення кожні 2 секунди
            }
        })
    }

    private fun fetchSensorData() {
        val request = Request.Builder().url(espUrl).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    tvTemperature.text = "Помилка з'єднання"
                    tvHumidity.text = ""
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val bodyStr = response.body?.string()
                if (response.isSuccessful && !bodyStr.isNullOrEmpty()) {
                    try {
                        val json = JSONObject(bodyStr)
                        val temp = json.getDouble("temperature")
                        val hum = json.getDouble("humidity")

                        runOnUiThread {
                            tvTemperature.text = "Температура: $temp°C"
                            tvHumidity.text = "Вологість: $hum%"
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            tvTemperature.text = "Невірний формат JSON"
                            tvHumidity.text = ""
                        }
                    }
                } else {
                    runOnUiThread {
                        tvTemperature.text = "Помилка даних"
                        tvHumidity.text = ""
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}
