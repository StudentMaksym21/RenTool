package com.example.toolrentapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.math.sign

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sEmail: String
    private lateinit var sPassword: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        val email_layout: TextInputLayout = findViewById(R.id.emailInputLayout)
        val email: EditText = findViewById(R.id.emailInput)
        val password_layout: TextInputLayout = findViewById(R.id.passwordInputLayout)
        val password: EditText = findViewById(R.id.passwordInput)
        val forgotPasswordText: TextView = findViewById(R.id.forgot_password_text)
        val login_button: Button = findViewById(R.id.loginButton)
        val sign_up_text: TextView = findViewById(R.id.signUpText)


        sign_up_text.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        login_button.setOnClickListener {
            sEmail = email.text.toString().trim()
            sPassword = password.text.toString().trim()

            when {
                sEmail.isEmpty() -> {
                    email_layout.error = "Email is required."
                    email.requestFocus()
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches() -> {
                    email_layout.error = "Invalid email format."
                    email.requestFocus()
                }
                sPassword.isEmpty() -> {
                    email_layout.error = null
                    password_layout.error = "Password is required."
                    password.requestFocus()
                }
                sPassword.length < 8 -> {
                    email_layout.error = null
                    password_layout.error = "Password must be at least 8 characters."
                    password.requestFocus()
                }
                else -> {
                    email_layout.error = null
                    password_layout.error = null

                    // Attempt to sign in
                    auth.signInWithEmailAndPassword(sEmail, sPassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success
                                Log.d("TAG", "signInWithEmail:success")
                                Toast.makeText(
                                    baseContext,
                                    "Authentication success.",
                                    Toast.LENGTH_LONG,
                                ).show()
                                val user = auth.currentUser
                                updateUI(user)
                            } else {
                                // Sign in failure
                                Log.w("TAG", "signInWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext,
                                    "Invalid credentials.",
                                    Toast.LENGTH_LONG,
                                ).show()
                            }
                        }
                }
            }
        }

    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}