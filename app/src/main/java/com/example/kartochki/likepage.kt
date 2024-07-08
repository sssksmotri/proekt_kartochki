package com.example.kartochki
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kartochki.Models.Card
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class likepage : AppCompatActivity() {
    private lateinit var zakladkiContainer: RecyclerView
    private lateinit var progressBar: ProgressBar
    private var userID: String? = null
    private lateinit var bottomNavigationView: BottomNavigationView

    private var likedCardsList: MutableList<Card> = mutableListOf()
    private lateinit var adapter: CardLikeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_likepage)
        bottomNavigationView = findViewById(R.id.navigation_bar)
        zakladkiContainer = findViewById(R.id.zakladkiContainer)
        progressBar = findViewById(R.id.progressBar)
        bottomNavigationView.selectedItemId = R.id.menu_like
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_main -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("userID", userID)
                    startActivity(intent)
                    true
                }

                R.id.menu_search -> {
                    val intent = Intent(this, Search::class.java)
                    intent.putExtra("userID", userID)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        val layoutManager = LinearLayoutManager(this)
        zakladkiContainer.layoutManager = layoutManager

        userID = intent.getStringExtra("userID")
        adapter = CardLikeAdapter(likedCardsList)
        zakladkiContainer.adapter = adapter

        progressBar.visibility = View.VISIBLE

        val likedCardsRef = FirebaseDatabase.getInstance().getReference("Liked").child(userID!!)
        likedCardsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                likedCardsList.clear()
                for (snapshot in dataSnapshot.children) {
                    val liked = snapshot.getValue(Boolean::class.java) ?: false
                    if (liked) {
                        val cardId = snapshot.key
                        if (cardId != null) {
                            getCardById(cardId)
                        }
                    }
                }
                progressBar.visibility = View.GONE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("likepage", "Failed to read value.", databaseError.toException())
                progressBar.visibility = View.GONE
            }
        })
    }

    private fun getCardById(cardId: String) {
        val cardsRef = FirebaseDatabase.getInstance().getReference("cards").child(cardId)
        cardsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val card = dataSnapshot.getValue(Card::class.java)
                    if (card != null) {
                        likedCardsList.add(card)
                        adapter.notifyDataSetChanged()
                        Log.d("likepage", "Card found: $card")
                    } else {
                        Log.e("likepage", "Card with ID $cardId is null")
                    }
                } else {
                    Log.e("likepage", "Card with ID $cardId does not exist in the database")
                }

                // Проверка, если список пуст, чтобы показать сообщение об отсутствии данных
                if (likedCardsList.isEmpty()) {
                    // Показать сообщение или выполнить другие действия, например, установить видимость текстового представления
                    // или вывести toast
                    Log.d("likepage", "No cards found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("likepage", "Failed to read card data for ID $cardId", databaseError.toException())
            }
        })
    }
}
