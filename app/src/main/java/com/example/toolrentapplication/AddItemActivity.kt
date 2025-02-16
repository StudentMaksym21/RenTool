package com.example.toolrentapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import android.widget.Toast

class AddItemActivity : AppCompatActivity() {

    private val REQUEST_STORAGE_PERMISSION = 100

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

        // Request storage permissions at runtime
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
        }

        toolNameInputLayout = findViewById(R.id.toolNameInputLayout)
        toolDescInputLayout = findViewById(R.id.toolDescInputLayout)
        toolPriceInputLayout = findViewById(R.id.toolPriceInputLayout)

        toolName = findViewById(R.id.toolNameInput)
        toolDesc = findViewById(R.id.toolDescInput)
        toolPrice = findViewById(R.id.toolPriceInput)
        addItemButton = findViewById(R.id.addItemButton)

        addItemButton.setOnClickListener {
            val name = toolName.text?.toString() ?: ""
            val description = toolDesc.text?.toString() ?: ""
            val price = toolPrice.text?.toString() ?: ""

            if (name.isEmpty()) {
                toolNameInputLayout.error = "Please enter the tool name"
                toolName.requestFocus()
                Toast.makeText(this, "Tool name is empty", Toast.LENGTH_SHORT).show()
            } else if (description.isEmpty()) {
                toolDescInputLayout.error = "Please enter the description"
                toolDesc.requestFocus()
                Toast.makeText(this, "Description is empty", Toast.LENGTH_SHORT).show()
            } else if (price.isEmpty()) {
                toolPriceInputLayout.error = "Please enter the price"
                toolPrice.requestFocus()
                Toast.makeText(this, "Price is empty", Toast.LENGTH_SHORT).show()
            } else {
                val resultIntent = Intent().apply {
                    putExtra("name", name)
                    putExtra("description", description)
                    putExtra("price", price)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish() // Close AddItemActivity
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with the operation
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(this, "Storage permission is required to access files.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
