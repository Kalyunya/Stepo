package com.example.fitstepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HeightAdapter(
    private val heights: List<Int>,
    private val onHeightSelected: (Int) -> Unit
) : RecyclerView.Adapter<HeightAdapter.HeightViewHolder>() {

    inner class HeightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val heightText: TextView = view.findViewById(R.id.itemText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeightViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_value, parent, false)
        return HeightViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeightViewHolder, position: Int) {
        val value = heights[position]
        holder.heightText.text = value.toString()
        holder.itemView.setOnClickListener {
            onHeightSelected(value)
        }
    }

    override fun getItemCount() = heights.size
}
