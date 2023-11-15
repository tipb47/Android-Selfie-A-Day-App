package com.example.project9

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project9.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Project9)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}