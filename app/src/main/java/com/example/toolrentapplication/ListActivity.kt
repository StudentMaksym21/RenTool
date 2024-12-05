package com.example.toolrentapplication

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ListActivity : AppCompatActivity() {

    private lateinit var toolsAvailableLayout: LinearLayout
    private val ADD_ITEM_REQUEST_CODE = 1
    private val RENT_TOOL_REQUEST_CODE = 2

    companion object {
        val toolList = mutableListOf<String>()
        val rentedToolList = mutableListOf<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val buttonCalendar: Button = findViewById(R.id.buttonCalendar)
        val buttonList: Button = findViewById(R.id.buttonList)
        val buttonProfile: Button = findViewById(R.id.buttonProfile)
        val addItemButton: Button = findViewById(R.id.addItemButton)
        val cleanButton: Button = findViewById(R.id.cleanButton)
        val filterButton: Button = findViewById(R.id.filterButton)
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        toolsAvailableLayout = findViewById(R.id.toolsAvailableLayout)

        buttonCalendar.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }

        buttonList.setOnClickListener {
            // Already on List page
        }

        buttonProfile.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
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
            val query = searchEditText.text.toString()
            filterTools(query)
        }

        // Populate tools available for rent
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
                val tool = "$name - $description - $price/day"
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
            }
        }
    }

    private fun addToolItem(name: String, description: String, price: String) {
        val toolItemLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(8, 8, 8, 8)
            setBackgroundColor(ContextCompat.getColor(this@ListActivity, android.R.color.darker_gray))
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
            setPadding(16, 0, 0, 0)
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

    private fun filterTools(query: String) {
        val priceRegex = Regex("""(\d+)/day""")
        val queryPrice = priceRegex.find(query)?.groupValues?.get(1)?.toIntOrNull()
        val queryName = query.replace(priceRegex, "").trim()

        val filteredList = toolList.filter { tool ->
            tool.contains(queryName, ignoreCase = true)
        }.sortedWith(compareBy {
            val toolNameMatch = it.contains(queryName, ignoreCase = true)
            if (queryPrice != null && toolNameMatch) {
                val toolPrice = priceRegex.find(it)?.groupValues?.get(1)?.toIntOrNull()
                toolPrice?.let { price -> Math.abs(price - queryPrice) } ?: Int.MAX_VALUE
            } else {
                Int.MAX_VALUE
            }
        })

        updateToolList(filteredList)
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
