package com.example.kartochki

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kartochki.Models.Card

class CardAdapter(private var cardList: MutableList<Card>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private var filteredCardList = cardList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = filteredCardList[position]
        holder.itemName.text = card.name

        Glide.with(holder.itemView.context)
            .load(card.image)
            .into(holder.imageCard)

        holder.peopleCard.text = card.people
        holder.cubeCard.text = card.cube
        holder.timeCard.text = card.time

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProfileCardActivity::class.java).apply {
                putExtra("cardId", card.id)
                putExtra("cardName", card.name)
                putExtra("cardImage", card.image)
                putExtra("cardPeople", card.people)
                putExtra("cardCube", card.cube)
                putExtra("cardTime", card.time)
                putExtra("cardDescription", card.opisanie)
                putExtra("cardProc", card.process)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return filteredCardList.size
    }

    fun filter(query: String) {
        filteredCardList = if (query.isEmpty()) {
            mutableListOf()
        } else {
            cardList.filter {
                it.name?.contains(query, ignoreCase = true) == true ||
                        it.people?.contains(query, ignoreCase = true) == true ||
                        it.cube?.contains(query, ignoreCase = true) == true ||
                        it.time?.contains(query, ignoreCase = true) == true
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.card_name)
        val imageCard: ImageView = itemView.findViewById(R.id.image_card)
        val peopleCard: TextView = itemView.findViewById(R.id.people_card)
        val cubeCard: TextView = itemView.findViewById(R.id.cube_card)
        val timeCard: TextView = itemView.findViewById(R.id.time_card)
    }
}
