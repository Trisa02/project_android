package com.example.project_android_trisa

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Home : AppCompatActivity() {
    private  lateinit var text_label : TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        text_label = findViewById(R.id.subtitle_text)

        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val full_name = sharedPreferences.getString("FULL_NAME", "Guest")

        text_label.text = "Hello $full_name";
    }
}