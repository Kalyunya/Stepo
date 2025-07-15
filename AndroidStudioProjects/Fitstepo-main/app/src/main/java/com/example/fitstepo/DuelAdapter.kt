package com.example.fitstepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DuelAdapter(private val items: List<DuelItem>) :
    RecyclerView.Adapter<DuelAdapter.DuelViewHolder>() {

    class DuelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.duelTitle)
        val date: TextView = view.findViewById(R.id.date)
        val score: TextView = view.findViewById(R.id.score)
        val icon: ImageView = view.findViewById(R.id.icon)
        val avatar1: ImageView = view.findViewById(R.id.avatarLeft)
        val avatar2: ImageView = view.findViewById(R.id.avatarRight)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuelViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_duel, parent, false)
        return DuelViewHolder(view)
    }

    override fun onBindViewHolder(holder: DuelViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.date.text = "Today, 14 June"
        holder.score.text = "${item.score1} vs ${item.score2}"
        holder.icon.setImageResource(item.iconResId)
        holder.avatar1.setImageResource(item.avatar1)
        holder.avatar2.setImageResource(item.avatar2)
    }

    override fun getItemCount() = items.size
}
