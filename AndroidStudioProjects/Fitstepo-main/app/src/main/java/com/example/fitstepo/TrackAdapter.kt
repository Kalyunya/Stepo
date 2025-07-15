package com.example.fitstepo

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private val trackList: List<Track>,
    private val onTrackPlay: (Track) -> Unit
) : RecyclerView.Adapter<TrackAdapter.TrackViewHolder>() {

    inner class TrackViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDuration: TextView = view.findViewById(R.id.tvTrackDuration)
        val tvTitle: TextView = view.findViewById(R.id.tvTrackTitle)
        val btnPlay: ImageButton = view.findViewById(R.id.btnTrackPlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.tvDuration.text = track.duration
        holder.tvTitle.text = track.title
        holder.btnPlay.setOnClickListener {
            onTrackPlay(track)
        }
    }
}
