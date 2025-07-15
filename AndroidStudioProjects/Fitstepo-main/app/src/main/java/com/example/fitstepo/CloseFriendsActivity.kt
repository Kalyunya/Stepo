package com.example.fitstepo

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CloseFriendsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CloseFriendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_close_friends)

        recyclerView = findViewById(R.id.recyclerCloseFriends)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val friends = listOf(
            CloseFriend("Linh Nguyen", R.drawable.avatar_linh, true),
            CloseFriend("Wayne Leonard", R.drawable.avatar_wayne, false),
            CloseFriend("Angelina Borbutska", R.drawable.avatar_angelina, true),
            CloseFriend("Mikola Chervinskii", R.drawable.avatar_mykola, true)
        )

        adapter = CloseFriendAdapter(friends)
        recyclerView.adapter = adapter

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.btnAdd).setOnClickListener {
            // Додати нових друзів (можна реалізувати інший екран)
        }
    }
}
