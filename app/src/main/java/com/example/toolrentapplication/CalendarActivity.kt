package com.example.toolrentapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CalendarActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var calendarView: CalendarView
    private lateinit var toolsRentedLayout: LinearLayout
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val buttonCalendar: Button = findViewById(R.id.buttonCalendar)
        val buttonList: Button = findViewById(R.id.buttonList)
        val buttonProfile: Button = findViewById(R.id.buttonProfile)
        val buttonZoomIn: Button = findViewById(R.id.buttonZoomIn)
        val buttonZoomOut: Button = findViewById(R.id.buttonZoomOut)
        val buttonReset: Button = findViewById(R.id.buttonReset)
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

        // Initialize the map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Set button click listeners for map interaction
        buttonZoomIn.setOnClickListener {
            googleMap.animateCamera(CameraUpdateFactory.zoomIn())
        }

        buttonZoomOut.setOnClickListener {
            googleMap.animateCamera(CameraUpdateFactory.zoomOut())
        }

        buttonReset.setOnClickListener {
            val sampleLocation = LatLng(51.77700400331281, 19.489334002285585) // Example: Łódź, Poland
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sampleLocation, 10f))
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Enable map gestures
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true

        // Add a marker at a sample location and move the camera
        val sampleLocation = LatLng(51.77700400331281, 19.489334002285585) // Example: Łódź, Poland
        googleMap.addMarker(MarkerOptions().position(sampleLocation).title("Sample Location"))

        val samp2 = LatLng(51.77924765428514, 19.492427241510807) // Example: Warsaw, Poland
        googleMap.addMarker(MarkerOptions().position(samp2).title(""))

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sampleLocation, 10f))

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
