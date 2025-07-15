package com.example.fitstepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(private val items: List<NotificationItem>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private val todaySection = "Today"
    private val yesterdaySection = "Yesterday"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.avatar.setImageResource(item.avatarResId)
        holder.sender.text = item.sender
        holder.message.text = item.message
        holder.time.text = item.time

        // Додаємо Today або Yesterday вручну (просте рішення)
        when (position) {
            0 -> {
                holder.sectionTitle.visibility = View.VISIBLE
                holder.sectionTitle.text = todaySection
            }
            2 -> {
                holder.sectionTitle.visibility = View.VISIBLE
                holder.sectionTitle.text = yesterdaySection
            }
            else -> {
                holder.sectionTitle.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.imgAvatar)
        val sender: TextView = view.findViewById(R.id.tvSender)
        val message: TextView = view.findViewById(R.id.tvMessage)
        val time: TextView = view.findViewById(R.id.tvTime)
        val sectionTitle: TextView = view.findViewById(R.id.tvSectionTitle)
    }
}
