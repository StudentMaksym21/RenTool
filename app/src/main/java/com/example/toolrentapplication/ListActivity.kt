package com.example.toolrentapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.SearchView
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.LayoutInflater
import android.graphics.Color

@Suppress("DEPRECATION")
class ListActivity : AppCompatActivity() {

    private lateinit var toolsAvailableLayout: LinearLayout
    private val ADD_ITEM_REQUEST_CODE = 1
    private val RENT_TOOL_REQUEST_CODE = 2
    private lateinit var bottomNavigationBar: BottomNavigationView

    companion object {
        val toolList = mutableListOf<String>()
        val rentedToolList = mutableListOf<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val addItemButton: Button = findViewById(R.id.addItemButton)
        val cleanButton: Button = findViewById(R.id.cleanButton)
        val filterButton: Button = findViewById(R.id.filterButton)
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val searchBar: SearchView = findViewById(R.id.searchBar)

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search text change
                return false
            }
        })

        toolsAvailableLayout = findViewById(R.id.toolsAvailableLayout)
        bottomNavigationBar = findViewById(R.id.bottom_navigation)
        bottomNavigationBar.selectedItemId = R.id.item_3

        bottomNavigationBar.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_1 -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_2 -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_3 -> {
                    val intent = Intent(this, ListActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item_4 -> {
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        addItemButton.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivityForResult(intent, ADD_ITEM_REQUEST_CODE)
        }

        cleanButton.setOnClickListener {
            toolList.clear()
            updateToolList()
        }

        filterButton.setOnClickListener {
            showFilterDialog()
        }

        populateAvailableTools()
        updateToolList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            val name = data?.getStringExtra("name")
            val description = data?.getStringExtra("description")
            val price = data?.getStringExtra("price")
            if (name != null && description != null && price != null) {
                val tool = "$name - $description - $$price"
                toolList.add(tool)
                updateToolList()
            }
        } else if (requestCode == RENT_TOOL_REQUEST_CODE && resultCode == RESULT_OK) {
            val toolName = data?.getStringExtra("toolName")
            val startDate = data?.getStringExtra("startDate")
            val endDate = data?.getStringExtra("endDate")
            val totalPrice = data?.getStringExtra("totalPrice")
            if (toolName != null && startDate != null && endDate != null && totalPrice != null) {
                val rentedTool = "$toolName - From: $startDate To: $endDate - Total: $$totalPrice"
                rentedToolList.add(rentedTool)
                toolList.removeAll { it.startsWith(toolName) } // Ensure only the correct tool is removed
                updateToolList()
            }
        }
    }

    private fun showFilterDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_filter, null)
        val priceSeekBar: SeekBar = dialogView.findViewById(R.id.priceSeekBar)
        val priceRangeTextView: TextView = dialogView.findViewById(R.id.priceRangeTextView)
        val searchButton: Button = dialogView.findViewById(R.id.searchButton)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        priceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                priceRangeTextView.text = "Up to $$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        searchButton.setOnClickListener {
            val maxPrice = priceSeekBar.progress
            filterTools(maxPrice)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun filterTools(maxPrice: Int) {
        val priceRegex = Regex("""\$(\d+)""")
        val filteredList = toolList.filter { tool ->
            val toolPrice = priceRegex.find(tool)?.groupValues?.get(1)?.toIntOrNull()
            val isSuitable = toolPrice != null && toolPrice <= maxPrice
            isSuitable
        }
        updateToolList(filteredList)
    }

    private fun addToolItem(name: String, description: String, price: String) {
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

        val nameTextView = TextView(this).apply {
            text = name
            textSize = 18f
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        val descriptionTextView = TextView(this).apply {
            text = description
            setTypeface(null, android.graphics.Typeface.NORMAL)
            setPadding(0, 4, 0, 4)
        }

        val priceTextView = TextView(this).apply {
            text = "Price: $${price}/day"
        }

        val rentButton = Button(this).apply {
            text = "Rent"
            setPadding(16, 0, 16, 0)
            setTextColor(Color.WHITE) // Set text color to white
            background = ContextCompat.getDrawable(this@ListActivity, R.drawable.rounded_button_background) // Set the new drawable as background
            setOnClickListener {
                val intent = Intent(this@ListActivity, RentActivity::class.java).apply {
                    putExtra("toolName", name)
                    putExtra("toolPrice", price)
                }
                startActivityForResult(intent, RENT_TOOL_REQUEST_CODE)
            }
        }

        val priceAndButtonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(priceTextView)
            addView(rentButton)
            gravity = Gravity.END or Gravity.CENTER_VERTICAL
        }

        toolItemLayout.addView(nameTextView)
        toolItemLayout.addView(descriptionTextView)
        toolItemLayout.addView(priceAndButtonLayout)

        toolsAvailableLayout.addView(toolItemLayout)
    }

    private fun updateToolList(filteredList: List<String> = toolList) {
        toolsAvailableLayout.removeAllViews()
        for (tool in filteredList) {
            val parts = tool.split(" - ")
            if (parts.size == 3) {
                addToolItem(parts[0], parts[1], parts[2].replace("/day", ""))
            }
        }
    }

    private fun populateAvailableTools() {
        // Initial sample data (if any)
    }
}
