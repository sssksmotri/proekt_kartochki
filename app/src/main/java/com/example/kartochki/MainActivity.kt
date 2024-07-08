package com.example.kartochki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.kartochki.Models.Card
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var container: LinearLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var userID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.navigation_bar)
        progressBar = findViewById(R.id.progressBar)
        container = findViewById(R.id.Container)


        userID = intent.getStringExtra("userID") ?: run {
            Log.e("MainActivity", "userID not found in Intent, retrieving from SharedPreferences")
            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            sharedPreferences.getString("userID", "") ?: ""
        }

        if (userID.isEmpty()) {
            Log.e("MainActivity", "userID is empty, redirecting to login activity")
            val intent = Intent(this, login::class.java)
            startActivity(intent)
            finish()
            return
        }

        Log.d("MainActivity", "UserID: $userID")

        fetchDataFromFirebase(userID)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_search -> {
                    val intent = Intent(this, Search::class.java)
                    intent.putExtra("userID", userID)
                    startActivity(intent)
                    true
                }

                R.id.menu_like -> {
                    val intent = Intent(this, likepage::class.java)
                    intent.putExtra("userID", userID)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun fetchDataFromFirebase(userID: String) {
        val database = FirebaseDatabase.getInstance().getReference("cards")
        progressBar.visibility = ProgressBar.VISIBLE

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progressBar.visibility = ProgressBar.GONE
                container.removeAllViews()
                for (cardSnapshot in snapshot.children) {
                    val card = cardSnapshot.getValue(Card::class.java)
                    if (card != null) {
                        addCardToContainer(card, userID)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressBar.visibility = ProgressBar.GONE
                Log.e("MainActivity", "Failed to read data from Firebase", error.toException())
            }
        })
    }

    private fun addCardToContainer(card: Card, userID: String) {
        val inflater = LayoutInflater.from(this)
        val cardView = inflater.inflate(R.layout.single_card, container, false)

        val cardName = cardView.findViewById<TextView>(R.id.card_name)
        val cardImage = cardView.findViewById<ImageView>(R.id.image_card)
        val peopleCard = cardView.findViewById<TextView>(R.id.people_card)
        val cubeCard = cardView.findViewById<TextView>(R.id.cube_card)
        val timeCard = cardView.findViewById<TextView>(R.id.time_card)
        val likeButton = cardView.findViewById<ImageView>(R.id.like_button)

        cardName.text = card.name
        Glide.with(this)
            .load(card.image)
            .transform(CenterCrop(), RoundedCorners(16))
            .into(cardImage)
        peopleCard.text = card.people
        cubeCard.text = card.cube
        timeCard.text = card.time

        cardView.setOnClickListener {
            val intent = Intent(this, ProfileCardActivity::class.java).apply {
                putExtra("cardId", card.id)
                putExtra("cardName", card.name)
                putExtra("cardImage", card.image)
                putExtra("cardPeople", card.people)
                putExtra("cardCube", card.cube)
                putExtra("cardTime", card.time)
                putExtra("cardDescription", card.opisanie)
                putExtra("cardProc", card.process)
                putExtra("userID", userID)
            }
            startActivity(intent)
        }

        card.id?.let { cardName ->
            val likedRef = FirebaseDatabase.getInstance().getReference("Liked").child(userID)
                .child(cardName) // Replace new1.getId_zapis() with card.getId_zapis()
            likedRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val isLiked = dataSnapshot.exists()
                    if (isLiked) {
                        likeButton.setImageResource(R.drawable.like2)
                    } else {
                        likeButton.setImageResource(R.drawable.like)
                    }

                    likeButton.setOnClickListener {
                        likedRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val currentlyLiked = snapshot.exists()
                                if (currentlyLiked) {
                                    likedRef.removeValue()
                                    likeButton.setImageResource(R.drawable.like)
                                } else {
                                    likedRef.setValue(true)
                                    likeButton.setImageResource(R.drawable.like2)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(
                                    "main_start",
                                    "Failed to update like status.",
                                    error.toException()
                                )
                            }
                        })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("main_start", "Failed to read like status.", databaseError.toException())
                }
            })
        }
        container.addView(cardView)
    }
}
