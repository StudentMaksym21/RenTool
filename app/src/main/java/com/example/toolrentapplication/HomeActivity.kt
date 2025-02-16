package com.example.toolrentapplication

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Snackbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.Color
import androidx.core.content.ContextCompat

@Suppress("DEPRECATION")
class HomeActivity : AppCompatActivity() {
    private lateinit var avatarImageView: ImageView
    private lateinit var rentedToolsList: LinearLayout
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var editProfileButton: MaterialButton
    private lateinit var bottomNavigationBar: BottomNavigationView
    private lateinit var emailRegex: Regex
    private lateinit var phoneRegex: Regex
    private lateinit var balanceTextView: TextView
    private lateinit var addCreditButton: Button
    private lateinit var withdrawButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private var isEditing = false
    private var balance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        avatarImageView = findViewById(R.id.avatarImageView)
        rentedToolsList = findViewById(R.id.rentedToolsList)
        usernameEditText = findViewById(R.id.usernameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        editProfileButton = findViewById(R.id.editProfileButton)
        balanceTextView = findViewById(R.id.balanceTextView)
        addCreditButton = findViewById(R.id.addCreditButton)
        withdrawButton = findViewById(R.id.withdrawButton)
        bottomNavigationBar = findViewById(R.id.bottom_navigation)

        sharedPreferences = getSharedPreferences("com.example.toolrentapplication", MODE_PRIVATE)
        balance = sharedPreferences.getFloat("balance", 0.0f).toDouble()

        phoneRegex = Regex("^\\+48 \\d{3} \\d{3} \\d{3}$")
        emailRegex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")

        bottomNavigationBar.selectedItemId = R.id.item_1

        editProfileButton.setOnClickListener {
            profileEdit()
        }

        addCreditButton.setOnClickListener {
            showAddCreditDialog()
        }

        withdrawButton.setOnClickListener {
            showWithdrawDialog()
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
                    // Navigate to CalendarActivity
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Populate rented tools with sample data
        updateRentedTools()
        updateBalanceDisplay()
    }

    private fun showAddCreditDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_credit, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add Credit")
        val alertDialog = builder.show()

        val cardNumberEditText = dialogView.findViewById<EditText>(R.id.cardNumberEditText)
        val cardHolderNameEditText = dialogView.findViewById<EditText>(R.id.cardHolderNameEditText)
        val expirationDateEditText = dialogView.findViewById<EditText>(R.id.expirationDateEditText)
        val amountEditText = dialogView.findViewById<EditText>(R.id.amountEditText)
        val addCreditDialogButton = dialogView.findViewById<Button>(R.id.addCreditDialogButton)

        addCreditDialogButton.setOnClickListener {
            val amountText = amountEditText.text.toString()
            if (amountText.isNotEmpty()) {
                val amount = amountText.toDouble()
                addCredit(amount)
            }
            // Close the dialog
            alertDialog.dismiss()
        }
    }

    private fun addCredit(amount: Double) {
        balance += amount
        sharedPreferences.edit().putFloat("balance", balance.toFloat()).apply()
        updateBalanceDisplay()
        Toast.makeText(this, "Added $amount to balance", Toast.LENGTH_SHORT).show()
    }

    private fun showWithdrawDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_withdraw, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Withdraw")
        val alertDialog = builder.show()

        val withdrawAmountEditText = dialogView.findViewById<EditText>(R.id.withdrawAmountEditText)
        val withdrawDialogButton = dialogView.findViewById<Button>(R.id.withdrawDialogButton)

        withdrawDialogButton.setOnClickListener {
            val amountText = withdrawAmountEditText.text.toString()
            if (amountText.isNotEmpty()) {
                val amount = amountText.toDouble()
                withdrawAmount(amount)
            }
            // Close the dialog
            alertDialog.dismiss()
        }
    }

    private fun withdrawAmount(amount: Double) {
        if (balance >= amount) {
            balance -= amount
            sharedPreferences.edit().putFloat("balance", balance.toFloat()).apply()
            updateBalanceDisplay()
            Toast.makeText(this, "Withdrawn $amount from balance", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Insufficient balance", Toast.LENGTH_SHORT).show()
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
                    setBackgroundResource(R.drawable.tool_item_background) // Apply background drawable
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
                    setPadding(16, 0, 16, 0)
                    setTextColor(Color.WHITE)
                    background = ContextCompat.getDrawable(this@HomeActivity, R.drawable.rounded_button_background) // Set the new drawable as background
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

    private fun updateBalanceDisplay() {
        balanceTextView.text = "Balance: $$balance"
    }

    fun onUploadAvatarClick(view: View) {
        // Code to handle avatar upload
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
                Toast.makeText(this, "Username must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
            } else if (!email.matches(emailRegex)) {
                Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show()
            } else if (!phone.matches(phoneRegex)) {
                Toast.makeText(this, "Phone number must be in the format +48 xxx xxx xxx.", Toast.LENGTH_SHORT).show()
            } else {
                // Save the changes
                usernameEditText.isEnabled = false
                emailEditText.isEnabled = false
                phoneEditText.isEnabled = false
                editProfileButton.text = "Edit Profile"
                isEditing = false
                // Setting back to normal colors
                usernameEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
                emailEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
                phoneEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent))
                usernameEditText.setTextColor(ContextCompat.getColor(this, R.color.black))
                emailEditText.setTextColor(ContextCompat.getColor(this, R.color.grey))
                phoneEditText.setTextColor(ContextCompat.getColor(this, R.color.grey))
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Enable editing
            usernameEditText.isEnabled = true
            emailEditText.isEnabled = true
            phoneEditText.isEnabled = true
            // Setting background colors
            usernameEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
            emailEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
            phoneEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.light_grey))
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

    fun onLogoutClick(view: View) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun onMessageClick(view: View) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_message, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Send Message")
        val alertDialog = builder.show()
        // Get references to views in the dialog
        val emailLayout = dialogView.findViewById<TextInputLayout>(R.id.emailInputLayout)
        val messageLayout = dialogView.findViewById<TextInputLayout>(R.id.messageInputLayout)
        val sendButton = dialogView.findViewById<MaterialButton>(R.id.sendButton)
        val emailinput = emailLayout?.editText
        val messageinput = messageLayout?.editText
        sendButton?.setOnClickListener {
            val email = emailinput?.text?.toString() ?: ""
            val message = messageinput?.text?.toString() ?: ""
            if (email.isNotEmpty() && message.isNotEmpty()) {
                try {
                    Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(this, "Error sending message: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                when {
                    email.isEmpty() && message.isEmpty() -> "Please fill in both email and message"
                    email.isEmpty() -> "Please enter your email"
                    else -> "Please enter your message"
                }.let { errorMessage ->
                    Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
