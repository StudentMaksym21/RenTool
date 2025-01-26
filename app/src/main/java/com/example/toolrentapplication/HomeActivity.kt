package com.example.toolrentapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button

import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat  // <- This is the import statement you need
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {


    private lateinit var avatarImageView: ImageView
    private lateinit var rentedToolsList: LinearLayout

    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var editProfileButton: MaterialButton
    private var isEditing = false
    private lateinit var bottomNavigationBar: BottomNavigationView
    private lateinit var emailRegex : Regex
    private lateinit var phoneRegex : Regex


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        avatarImageView = findViewById(R.id.avatarImageView)
        rentedToolsList = findViewById(R.id.rentedToolsList)

        usernameEditText = findViewById(R.id.usernameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        editProfileButton = findViewById(R.id.editProfileButton)
        phoneRegex = Regex("^\\+48 \\d{3} \\d{3} \\d{3}$")
        emailRegex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        bottomNavigationBar = findViewById(R.id.bottom_navigation)

        bottomNavigationBar.selectedItemId = R.id.item_1

        editProfileButton.setOnClickListener{
            profileEdit()
        }

        bottomNavigationBar.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_1 -> {
                    // Navigate to HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_2 -> {
                    // Navigate to MapsActivity
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_3 -> {
                    // Navigate to ListActivity
                    val intent = Intent(this, ListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_4 -> {
                    // Navigate to Calendar Activity
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }


        // Populate rented tools with sample data
        updateRentedTools()
    }

    fun onUploadAvatarClick(view: View) {
        // Code to handle avatar upload
        //code to test the function
        Toast.makeText(this, "Avatar uploaded!", Toast.LENGTH_SHORT).show()
    }

    fun profileEdit() {
        if (isEditing) {
            // Save mode
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "All fields must be filled out.", Toast.LENGTH_SHORT).show()
            } else if (username.length < 6) {
                // Check if username is at least 6 characters long
                Toast.makeText(this, "Username must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
            } else if (!email.matches(emailRegex)) {
                // Check if email format is valid
                Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show()
            }else if (!phone.matches(phoneRegex)) {
                // Check if phone format is valid
                Toast.makeText(this, "Phone number must be in the format +48 xxx xxx xxx.", Toast.LENGTH_SHORT).show()
            }  else {
                // Save the changes
                usernameEditText.isEnabled = false
                emailEditText.isEnabled = false
                phoneEditText.isEnabled = false

                editProfileButton.text = "Edit Profile"
                isEditing = false

                //setting back to normal colors
                usernameEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
                emailEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
                phoneEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))

                //setting back to normal colors
                usernameEditText.setTextColor(ContextCompat.getColor(this, R.color.black))
                emailEditText.setTextColor(ContextCompat.getColor(this, R.color.grey))
                phoneEditText.setTextColor(ContextCompat.getColor(this, R.color.grey))

                // Show success message
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Enable editing
            usernameEditText.isEnabled = true
            emailEditText.isEnabled = true
            phoneEditText.isEnabled = true

            //setting background colors
            usernameEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
            emailEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
            phoneEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))

            //editing hint color
            usernameEditText.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            emailEditText.setHintTextColor(ContextCompat.getColor(this, R.color.red))
            phoneEditText.setHintTextColor(ContextCompat.getColor(this, R.color.red))

            usernameEditText.setTextColor(ContextCompat.getColor(this, R.color.red))
            emailEditText.setTextColor(ContextCompat.getColor(this, R.color.red))
            phoneEditText.setTextColor(ContextCompat.getColor(this, R.color.red))

            editProfileButton.text = "Save"
            isEditing = true
        }
    }


    private fun updateRentedTools() {
        rentedToolsList.removeAllViews()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = Date()

        for ((index, rentedTool) in ListActivity.rentedToolList.withIndex()) {
            val parts = rentedTool.split(" - From: ", " To: ", " - Total: $")
            if (parts.size == 4) {
                val toolName = parts[0]
                val endDateStr = parts[2]
                val endDate = dateFormat.parse(endDateStr)
                val remainingDays = ((endDate.time - currentDate.time) / (1000 * 60 * 60 * 24)).toInt()

                val toolItemLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(8, 8, 8, 8)
                    setBackgroundColor(ContextCompat.getColor(this@HomeActivity, android.R.color.darker_gray))
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 16)
                    }
                }

                val toolTextView = TextView(this).apply {
                    text = "$toolName - Remaining Days: $remainingDays"
                    textSize = 16f
                    setPadding(8, 8, 8, 8)
                }

                val returnButton = Button(this).apply {
                    text = "Return Early"
                    setPadding(16, 0, 0, 0)
                    setOnClickListener {
                        ListActivity.rentedToolList.removeAt(index)
                        updateRentedTools()
                    }
                }

                toolItemLayout.addView(toolTextView)
                toolItemLayout.addView(returnButton)

                rentedToolsList.addView(toolItemLayout)
            }
        }
    }

    fun onLogoutClick(view: View) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}
