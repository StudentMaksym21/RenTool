package com.example.toolrentapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity() {

    private lateinit var googleMap: GoogleMap
    private lateinit var bottomNavigationBar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_maps)

        bottomNavigationBar = findViewById(R.id.bottom_navigation)
        bottomNavigationBar.selectedItemId = R.id.item_2

        // Initialize the map
        //TO-DO


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
    }
}