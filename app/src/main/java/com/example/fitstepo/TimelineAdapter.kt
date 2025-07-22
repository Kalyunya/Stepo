package com.example.fitstepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimelineAdapter(private val items: List<TimelineItem>) :
    RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    class TimelineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val duration: TextView = view.findViewById(R.id.duration)
        val distance: TextView = view.findViewById(R.id.distance)
        val kcal: TextView = view.findViewById(R.id.kcal)
        val icon: ImageView = view.findViewById(R.id.icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timeline, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.duration.text = item.duration
        holder.distance.text = item.distance
        holder.kcal.text = item.kcal
        holder.icon.setImageResource(item.iconResId)
    }

    override fun getItemCount() = items.size
}
