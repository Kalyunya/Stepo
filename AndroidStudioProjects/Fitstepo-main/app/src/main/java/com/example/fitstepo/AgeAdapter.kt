package com.example.fitstepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AgeAdapter(
    private val ageList: List<Int>,
    private val onAgeSelected: (Int) -> Unit
) : RecyclerView.Adapter<AgeAdapter.AgeViewHolder>() {

    private var selectedPosition = ageList.indexOf(28) // Значення за замовчуванням

    inner class AgeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ageText: TextView = itemView.findViewById(R.id.textAge)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectedPosition = position
                    notifyDataSetChanged()
                    onAgeSelected(ageList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_age, parent, false)
        return AgeViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgeViewHolder, position: Int) {
        val age = ageList[position]
        holder.ageText.text = age.toString()

        // Стиль вибраного віку
        if (position == selectedPosition) {
            holder.ageText.setTextColor(0xFF000000.toInt()) // чорний
            holder.ageText.textSize = 24f
            holder.ageText.setTypeface(null, android.graphics.Typeface.BOLD)
        } else {
            holder.ageText.setTextColor(0xFF888888.toInt()) // сірий
            holder.ageText.textSize = 18f
            holder.ageText.setTypeface(null, android.graphics.Typeface.NORMAL)
        }
    }

    override fun getItemCount(): Int = ageList.size
}
