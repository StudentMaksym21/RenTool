package com.example.toolrentapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CalendarActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var toolsRentedLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val buttonCalendar: Button = findViewById(R.id.buttonCalendar)
        val buttonList: Button = findViewById(R.id.buttonList)
        val buttonProfile: Button = findViewById(R.id.buttonProfile)
        calendarView = findViewById(R.id.calendarView)
        toolsRentedLayout = findViewById(R.id.toolsRentedLayout)

        buttonCalendar.setOnClickListener {
            // Already on Calendar page
        }

        buttonList.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        buttonProfile.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        // Set a listener to handle date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = "$dayOfMonth/${month + 1}/$year"
            Toast.makeText(this, "Selected date: $date", Toast.LENGTH_SHORT).show()
        }

        // Update the tools rented list
        updateRentedToolList()
    }

    private fun updateRentedToolList() {
        toolsRentedLayout.removeAllViews()
        for (rentedTool in ListActivity.rentedToolList) {
            val textView = TextView(this).apply {
                text = rentedTool
                textSize = 16f
                setPadding(8, 8, 8, 8)
            }
            toolsRentedLayout.addView(textView)
        }
    }
}
