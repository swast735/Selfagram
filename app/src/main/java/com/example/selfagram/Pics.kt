package com.example.selfagram

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Pics : AppCompatActivity() {
    lateinit var image:ImageView
    val fauth: FirebaseAuth = FirebaseAuth.getInstance()
    val uid=fauth.currentUser?.uid
    val uref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("$uid").child("Images")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pics)
    }

}