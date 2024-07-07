package com.example.kartochki

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.kartochki.Models.Card
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        progressBar = findViewById(R.id.progressBar)
        container = findViewById(R.id.Container)

        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        val database = FirebaseDatabase.getInstance().getReference("cards")
        progressBar.visibility = ProgressBar.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = ProgressBar.GONE
                container.removeAllViews()
                for (cardSnapshot in snapshot.children) {
                    val card = cardSnapshot.getValue(Card::class.java)
                    if (card != null) {
                        addCardToContainer(card)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = ProgressBar.GONE
                Log.e("MainActivity", "Failed to read data from Firebase", error.toException())
            }
        })
    }

    private fun addCardToContainer(card: Card) {
        val inflater = LayoutInflater.from(this)
        val cardView = inflater.inflate(R.layout.single_card, container, false)

        val cardName = cardView.findViewById<TextView>(R.id.card_name)
        val cardImage = cardView.findViewById<ImageView>(R.id.image_card)
        val peopleCard = cardView.findViewById<TextView>(R.id.people_card)
        val cubeCard = cardView.findViewById<TextView>(R.id.cube_card)
        val timeCard = cardView.findViewById<TextView>(R.id.time_card)

        cardName.text = card.name
        Glide.with(this)
            .load(card.image)
            .into(cardImage)
        peopleCard.text = card.people
        cubeCard.text = card.cube
        timeCard.text = card.time

        container.addView(cardView)
    }
}
