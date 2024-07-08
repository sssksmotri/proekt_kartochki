package com.example.kartochki

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class ProfileCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_card)

        val cardName = intent.getStringExtra("cardName")
        val cardImage = intent.getStringExtra("cardImage")
        val cardPeople = intent.getStringExtra("cardPeople")
        val cardCube = intent.getStringExtra("cardCube")
        val cardTime = intent.getStringExtra("cardTime")
        val cardDescription = intent.getStringExtra("cardDescription")
        val cardProc = intent.getStringExtra("cardProc")
        val userID = intent.getStringExtra("userID")

        findViewById<TextView>(R.id.card_name).text = cardName
        findViewById<TextView>(R.id.people_card).text = cardPeople
        findViewById<TextView>(R.id.cube_card).text = cardCube
        findViewById<TextView>(R.id.time_card).text = cardTime
        findViewById<TextView>(R.id.opisaniye).text = cardDescription
        findViewById<TextView>(R.id.process).text = cardProc

        val cardImageView = findViewById<ImageView>(R.id.image_card)
        Glide.with(this)
            .load(cardImage)
            .transform(CenterCrop(), RoundedCorners(16))
            .into(cardImageView)

        findViewById<ImageView>(R.id.back).setOnClickListener {
            onBackPressed()
        }
    }
}
