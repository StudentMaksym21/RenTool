package com.example.toolrentapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
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
    private var isStartDateSelected = false
    private val selectedDates = mutableListOf<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)

        calendarView = findViewById(R.id.calendarView)
        priceTextView = findViewById(R.id.priceTextView)
        bookButton = findViewById(R.id.bookButton)

        toolName = intent.getStringExtra("toolName") ?: ""
        toolPrice = intent.getStringExtra("toolPrice") ?: "0"

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            if (!isStartDateSelected) {
                startDate = selectedDate.time
                isStartDateSelected = true
                selectedDates.clear()
                selectedDates.add(selectedDate.timeInMillis)
            } else {
                endDate = selectedDate.time
                selectedDates.add(selectedDate.timeInMillis)
                isStartDateSelected = false
            }
            highlightSelectedDates()
        }

        bookButton.setOnClickListener {
            if (::startDate.isInitialized && ::endDate.isInitialized) {
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
            } else {
                Toast.makeText(this, "Please select a start and end date", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun highlightSelectedDates() {
        calendarView.invalidate()
        // Highlight selected dates
        selectedDates.forEach { millis ->
            val calendar = Calendar.getInstance().apply { timeInMillis = millis }
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            calendarView.setDate(millis, false, true)
            val textView = calendarView.getChildAt(0) as? TextView
            textView?.setBackgroundColor(Color.YELLOW)
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
