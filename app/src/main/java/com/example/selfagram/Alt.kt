package com.example.selfagram

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.net.toUri

class Alt : AppCompatActivity() {
    lateinit var iv:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alert_image_preview)
        iv=findViewById(R.id.img)


    }
}