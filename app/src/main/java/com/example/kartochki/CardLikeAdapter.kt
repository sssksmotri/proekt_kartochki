package com.example.kartochki

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kartochki.Models.Card

class CardLikeAdapter(private val likedCardsList: List<Card>) : RecyclerView.Adapter<CardLikeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCard = likedCardsList[position]

        holder.card_nameTextView.text = currentCard.name ?: ""
        holder.people_cardTextView.text = currentCard.people ?: ""
        holder.cube_cardTextView.text = currentCard.cube ?: ""
        holder.time_cardTextView.text = currentCard.time ?: ""

        Glide.with(holder.itemView.context)
            .load(currentCard.image)
            .into(holder.image_card)

        holder.likeButton.setImageResource(R.drawable.like2)
    }

    override fun getItemCount(): Int {
        return likedCardsList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var card_nameTextView: TextView = itemView.findViewById(R.id.card_name)
        var people_cardTextView: TextView = itemView.findViewById(R.id.people_card)
        var cube_cardTextView: TextView = itemView.findViewById(R.id.cube_card)
        var time_cardTextView: TextView = itemView.findViewById(R.id.time_card)
        var image_card: ImageView = itemView.findViewById(R.id.image_card)
        var likeButton: ImageView = itemView.findViewById(R.id.like_button)
    }
}
