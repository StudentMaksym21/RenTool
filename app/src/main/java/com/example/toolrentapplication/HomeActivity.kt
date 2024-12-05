package com.example.toolrentapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat  // <- This is the import statement you need
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var aboutMeEditText: EditText
    private lateinit var contactInfoEditText: EditText
    private lateinit var editUsernameButton: Button
    private lateinit var editAboutMeButton: Button
    private lateinit var editContactInfoButton: Button
    private lateinit var avatarImageView: ImageView
    private lateinit var rentedToolsList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        usernameEditText = findViewById(R.id.usernameEditText)
        aboutMeEditText = findViewById(R.id.aboutMeEditText)
        contactInfoEditText = findViewById(R.id.contactInfoEditText)
        editUsernameButton = findViewById(R.id.editUsernameButton)
        editAboutMeButton = findViewById(R.id.editAboutMeButton)
        editContactInfoButton = findViewById(R.id.editContactInfoButton)
        avatarImageView = findViewById(R.id.avatarImageView)
        rentedToolsList = findViewById(R.id.rentedToolsList)

        val buttonCalendar: Button = findViewById(R.id.buttonCalendar)
        val buttonList: Button = findViewById(R.id.buttonList)
        val buttonProfile: Button = findViewById(R.id.buttonProfile)

        buttonCalendar.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }

        buttonList.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        buttonProfile.setOnClickListener {
            // Already on Profile page (Home)
        }

        // Populate rented tools with sample data
        updateRentedTools()
    }

    fun onUploadAvatarClick(view: View) {
        // Code to handle avatar upload
    }

    fun onEditUsernameClick(view: View) {
        toggleEditMode(usernameEditText, editUsernameButton)
    }

    fun onEditAboutMeClick(view: View) {
        toggleEditMode(aboutMeEditText, editAboutMeButton)
    }

    fun onEditContactInfoClick(view: View) {
        toggleEditMode(contactInfoEditText, editContactInfoButton)
    }

    private fun toggleEditMode(editText: EditText, button: Button) {
        if (button.text == "Edit") {
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
            editText.requestFocus()
            button.text = "Save"
        } else {
            editText.isFocusable = false
            editText.isFocusableInTouchMode = false
            button.text = "Edit"
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
}
