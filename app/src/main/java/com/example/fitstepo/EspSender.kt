package com.example.fitstepo

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

object EspSender {

    private const val ESP_IP = "http://192.168.188.56"  // ← Заміни на IP своєї ESP32

    fun sendFullNameToEsp(fullName: String) {
        val client = OkHttpClient()
        val requestBody = fullName.toRequestBody("text/plain".toMediaType())
        val request = Request.Builder()
            .url("$ESP_IP/name") // ← /name обробляється на ESP32
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ESP", "Помилка відправки імені", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.d("ESP", "Ім’я успішно передано: $fullName")
                } else {
                    Log.w("ESP", "Помилка відповіді від ESP: ${response.code}")
                }
            }
        })
    }
}
