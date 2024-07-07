package com.example.kartochki

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText
    private lateinit var btn_login: Button
    private lateinit var mAuth: FirebaseAuth

    private lateinit var register_txt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        edEmail = findViewById(R.id.loginEditText)
        edPassword = findViewById(R.id.passwordEditText)
        btn_login = findViewById(R.id.loginButton)
        register_txt = findViewById(R.id.loginLinktoRegisterActivity)

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val savedUserID = sharedPreferences.getString("userID", null)
        Log.d("SavedUserID", "UserID: $savedUserID")
        if (savedUserID != null) {
            Log.d("LoginScreen", "User already logged in, redirecting to MainActivity")
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d("LoginScreen", "Initializing login screen")
            initializeLoginScreen()
        }
    }

    private fun initializeLoginScreen() {
        register_txt.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        btn_login.setOnClickListener {
            val email = edEmail.text.toString()
            val password = edPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {
                            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("userID", mAuth.currentUser!!.uid)
                            editor.apply()

                            Log.d("LoginScreen", "User logged in successfully, redirecting to MainActivity")
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Log.w("LoginScreen", "signInWithEmail:failure", task.exception)
                            Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
    }
}
