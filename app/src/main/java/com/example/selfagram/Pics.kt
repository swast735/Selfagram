package com.example.selfagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

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