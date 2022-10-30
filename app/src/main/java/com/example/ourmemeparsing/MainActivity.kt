package com.example.ourmemeparsing

import android.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val start_btn: Button = findViewById(R.id.button1)
        start_btn.setOnClickListener(View.OnClickListener {
            try {
                parse.parsing()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })
    }
}