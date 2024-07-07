package com.example.kartochki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class RegisterActivity : AppCompatActivity() {

    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText
    private lateinit var edUsername: EditText
    private lateinit var btnRegister: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var avtorizTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        edEmail = findViewById(R.id.emailEditText)
        edPassword = findViewById(R.id.passwordEditText)
        avtorizTxt = findViewById(R.id.logLinktoLoginActivity)
        btnRegister = findViewById(R.id.registerButton)

        avtorizTxt.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            if (edEmail.text.toString().isEmpty() || edPassword.text.toString().isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "Fields are empty")
            } else {
                Log.d("RegisterActivity", "Attempting to register")
                mAuth.createUserWithEmailAndPassword(edEmail.text.toString(), edPassword.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("RegisterActivity", "Registration successful")
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            saveUserToFirestore()
                            startActivity(intent)
                        } else {
                            Log.e("RegisterActivity", "Registration failed", task.exception)
                        }
                    }
            }
        }
    }

    private fun saveUserToFirestore() {
        val userId = mAuth.currentUser?.uid ?: return
        val email = edEmail.text.toString()
        val password = edPassword.text.toString()
        val db = FirebaseFirestore.getInstance()
        val user = User(userId, "Klient", email, "", password, "", FieldValue.serverTimestamp().toString(), "", "")

        db.collection("users").document(userId)
            .set(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "User data created in Firestore")
            }
            .addOnFailureListener { e ->
                Log.e("RegisterActivity", "Error creating user data in Firestore", e)
            }
    }
}