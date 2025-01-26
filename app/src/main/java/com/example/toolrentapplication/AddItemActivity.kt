package com.example.toolrentapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class AddItemActivity : AppCompatActivity() {

    private lateinit var toolNameInputLayout: TextInputLayout
    private lateinit var toolDescInputLayout: TextInputLayout
    private lateinit var toolPriceInputLayout: TextInputLayout

    private lateinit var toolName: EditText
    private lateinit var toolDesc: EditText
    private lateinit var toolPrice: EditText
    private lateinit var addItemButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        toolNameInputLayout = findViewById(R.id.toolNameInputLayout)
        toolDescInputLayout = findViewById(R.id.toolDescInputLayout)
        toolPriceInputLayout = findViewById(R.id.toolPriceInputLayout)

        toolName = findViewById(R.id.toolNameInput)
        toolDesc = findViewById(R.id.toolDescInput)
        toolPrice = findViewById(R.id.toolPriceInput)
        addItemButton = findViewById(R.id.addItemButton)

        addItemButton.setOnClickListener {
            val name = toolName.text.toString()
            val description = toolDesc.text.toString()
            val price = toolPrice.text.toString()

            if (name.isEmpty()) {
                toolNameInputLayout.error = "Please enter the tool name"
                toolName.requestFocus()
            } else if (description.isEmpty()) {
                toolDescInputLayout.error = "Please enter the description"
                toolDesc.requestFocus()
            } else if (price.isEmpty()) {
                toolPriceInputLayout.error = "Please enter the price"
                toolPrice.requestFocus()
            } else {
                val resultIntent = Intent()
                resultIntent.putExtra("name", name)
                resultIntent.putExtra("description", description)
                resultIntent.putExtra("price", price)
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close AddItemActivity
            }
        }
    }
}

