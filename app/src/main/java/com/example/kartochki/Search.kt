package com.example.kartochki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.SearchView
import com.example.kartochki.Models.Card
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Search : AppCompatActivity() {
    private lateinit var cardAdapter: CardAdapter
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private val cardList = mutableListOf<Card>()
    private val db = FirebaseDatabase.getInstance().getReference("cards")
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        bottomNavigationView = findViewById(R.id.navigation_bar)
        cardAdapter = CardAdapter(cardList)
        searchView = findViewById(R.id.search_view)
        recyclerView = findViewById(R.id.recycler_view)
        bottomNavigationView.selectedItemId = R.id.menu_search
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cardAdapter

        searchView.setIconifiedByDefault(false)

        loadCardsFromFirebase()
        setupSearchView()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_main -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }


    private fun loadCardsFromFirebase() {
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                cardList.clear()
                for (snapshot in dataSnapshot.children) {
                    val card = snapshot.getValue(Card::class.java)
                    if (card != null) {
                        cardList.add(card)
                    }
                }
                cardAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("Search", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                cardAdapter.filter(newText.orEmpty())
                return true
            }
        })


        searchView.setOnClickListener {
            searchView.isIconified = false
        }
    }
}