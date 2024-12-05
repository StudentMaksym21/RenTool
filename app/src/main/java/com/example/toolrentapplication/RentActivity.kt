package com.example.toolrentapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class RentActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var priceTextView: TextView
    private lateinit var bookButton: Button
    private lateinit var startDate: Date
    private lateinit var endDate: Date
    private lateinit var toolName: String
    private lateinit var toolPrice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)

        calendarView = findViewById(R.id.calendarView)
        priceTextView = findViewById(R.id.priceTextView)
        bookButton = findViewById(R.id.bookButton)

        toolName = intent.getStringExtra("toolName") ?: ""
        toolPrice = intent.getStringExtra("toolPrice") ?: "0"

        var isStartDateSelected = false

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            if (!isStartDateSelected) {
                startDate = selectedDate.time
                isStartDateSelected = true
            } else {
                endDate = selectedDate.time
                calculatePrice()
                isStartDateSelected = false
            }
        }

        bookButton.setOnClickListener {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDateStr = dateFormat.format(startDate)
            val endDateStr = dateFormat.format(endDate)
            val totalDays = ((endDate.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt() + 1
            val totalPrice = totalDays * toolPrice.toInt()

            val resultIntent = Intent().apply {
                putExtra("toolName", toolName)
                putExtra("startDate", startDateStr)
                putExtra("endDate", endDateStr)
                putExtra("totalPrice", totalPrice.toString())
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun calculatePrice() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val startDateStr = dateFormat.format(startDate)
        val endDateStr = dateFormat.format(endDate)
        val totalDays = ((endDate.time - startDate.time) / (1000 * 60 * 60 * 24)).toInt() + 1
        val totalPrice = totalDays * toolPrice.toInt()
        priceTextView.text = "Rent from $startDateStr to $endDateStr\nTotal Price: $$totalPrice"
    }
}
