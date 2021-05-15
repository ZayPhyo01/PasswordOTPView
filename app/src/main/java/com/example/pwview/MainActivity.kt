package com.example.pwview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    lateinit var b: AnimateEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pwBox: PasswordPicker = findViewById(R.id.pwPicker)

        val e = findViewById<MaterialButton>(R.id.btnLogin)
        pwBox.setOnPasswordSubmitListener { pw ->
            Toast.makeText(this, pw, Toast.LENGTH_SHORT).show()
        }
        e.setOnClickListener {
            pwBox.verify()

        }
        b = findViewById<AnimateEditText>(R.id.edt)


    }


}


