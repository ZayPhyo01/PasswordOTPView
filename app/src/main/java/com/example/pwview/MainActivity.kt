package com.example.pwview

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity(), PasswordPicker.Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pwBox: PasswordPicker = findViewById(R.id.pwPicker)

        val e = findViewById<MaterialButton>(R.id.btnLogin)
        pwBox.setOnPasswordSubmitListener(this)
        e.setOnClickListener {
            pwBox.verify()

        }
    }

    override fun getSubmitPassword(pw: String) {
        Toast.makeText(this, pw, Toast.LENGTH_SHORT).show()
    }
}