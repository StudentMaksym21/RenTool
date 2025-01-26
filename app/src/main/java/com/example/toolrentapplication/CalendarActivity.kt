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
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class CalendarActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var calendarView: CalendarView
    private lateinit var toolsRentedLayout: LinearLayout
    private lateinit var googleMap: GoogleMap
    private lateinit var bottomNavigationBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val buttonZoomIn: Button = findViewById(R.id.buttonZoomIn)
        val buttonZoomOut: Button = findViewById(R.id.buttonZoomOut)
        val buttonReset: Button = findViewById(R.id.buttonReset)
        calendarView = findViewById(R.id.calendarView)
        toolsRentedLayout = findViewById(R.id.toolsRentedLayout)

        bottomNavigationBar = findViewById(R.id.bottom_navigation)

        bottomNavigationBar.selectedItemId = R.id.item_4

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
