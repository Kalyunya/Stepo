package com.example.fitstepo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class WeightAdapter(
    private val weights: List<Int>,
    private val onWeightSelected: (Int) -> Unit
) : RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    inner class WeightViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val weightText: TextView = view.findViewById(R.id.textWeight)

        init {
            view.setOnClickListener {
                onWeightSelected(weights[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weight, parent, false)
        return WeightViewHolder(view)
    }

    override fun getItemCount(): Int = weights.size

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        holder.weightText.text = weights[position].toString()
    }
}
