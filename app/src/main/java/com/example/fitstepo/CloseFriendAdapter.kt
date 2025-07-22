package com.example.fitstepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView

data class CloseFriend(val name: String, val avatarRes: Int, var isClose: Boolean)

class CloseFriendAdapter(private val list: List<CloseFriend>) :
    RecyclerView.Adapter<CloseFriendAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAvatar: ImageView = view.findViewById(R.id.imgAvatar)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val switchCloseFriend: SwitchCompat = view.findViewById(R.id.switchCloseFriend)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_close_friend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = list[position]

        holder.imgAvatar.setImageResource(friend.avatarRes)
        holder.tvName.text = friend.name

        // ❗ Очищуємо старий слухач перед виставленням isChecked
        holder.switchCloseFriend.setOnCheckedChangeListener(null)

        // Встановлюємо актуальний стан перемикача
        holder.switchCloseFriend.isChecked = friend.isClose

        // Знову додаємо слухача
        holder.switchCloseFriend.setOnCheckedChangeListener { _, isChecked ->
            friend.isClose = isChecked
        }
    }


    override fun getItemCount(): Int = list.size
}
