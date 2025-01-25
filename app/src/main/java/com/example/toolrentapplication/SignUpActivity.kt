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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sEmail: String
    private lateinit var sPassword: String
    private lateinit var sRetypePassword: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        val email_layout: TextInputLayout = findViewById(R.id.emailInputLayout)
        val email: EditText = findViewById(R.id.emailInput)
        val password_layout: TextInputLayout = findViewById(R.id.passwordInputLayout)
        val password: EditText = findViewById(R.id.passwordInput)
        val retype_password_layout: TextInputLayout = findViewById(R.id.retypePasswordInputLayout)
        val retype_password: EditText = findViewById(R.id.retypePasswordInput)
        val create_account_btn: Button = findViewById(R.id.create_account_btn)
        val login_text: TextView = findViewById(R.id.loginText)

        login_text.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        create_account_btn.setOnClickListener {
            sEmail = email.text.toString().trim()
            sPassword = password.text.toString().trim()
            sRetypePassword = retype_password.text.toString().trim()

            when {
                sEmail.isEmpty() -> {
                    email_layout.error = "Email is required."
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(sEmail).matches() -> {
                    email_layout.error = "Invalid email format."
                }
                sPassword.isEmpty() -> {
                    email_layout.error = null // Clear email error
                    password_layout.error = "Password is required."
                }
                sPassword.length < 8 -> {
                    email_layout.error = null
                    password_layout.error = "Password must be at least 8 characters long."
                }
                sRetypePassword.isEmpty() -> {
                    password_layout.error = null
                    retype_password_layout.error = "Please retype your password."
                }
                sPassword != sRetypePassword -> {
                    retype_password_layout.error = "Passwords do not match."
                }
                else -> {
                    email_layout.error = null
                    password_layout.error = null
                    retype_password_layout.error = null

                    // Attempt to create the user
                    auth.createUserWithEmailAndPassword(sEmail, sPassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign up success
                                Log.d("TAG", "createUserWithEmail:success")
                                Toast.makeText(
                                    baseContext,
                                    "Account created successfully.",
                                    Toast.LENGTH_LONG,
                                ).show()
                                val user = auth.currentUser
                                updateUI(user)
                            } else {
                                // If sign up fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext,
                                    "Sign up failed. Try again. ${task.exception?.localizedMessage}",
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
            startActivity(intent)
            finish()
        }
    }
}


