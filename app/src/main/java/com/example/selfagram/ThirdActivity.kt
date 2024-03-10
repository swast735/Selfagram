package com.example.selfagram

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class ThirdActivity : AppCompatActivity() {
    private val fauth=FirebaseAuth.getInstance()
    val uid=fauth.currentUser?.uid
    lateinit var btm:BottomNavigationView
    lateinit var sigout:Button
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        sp=getSharedPreferences(getString(R.string.SharPref), Context.MODE_PRIVATE)
        sigout=findViewById(R.id.so)
        btm=findViewById(R.id.bottomNavigationView)
        btm.setSelectedItemId(R.id.about)
        btm.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.MyProfile->{startActivity(Intent(this@ThirdActivity,FirstActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true }
                R.id.home->{startActivity(Intent(this@ThirdActivity,SecondActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true}
                R.id.about->{startActivity(Intent(this@ThirdActivity,ThirdActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true}
            }
            false
        }
        sigout.setOnClickListener {
//           setContentView(R.layout.sign_up)
            startActivity(Intent(this@ThirdActivity,SignUp::class.java))
            sp.edit().putBoolean("IsloggedIn",false).apply()
            fauth.signOut()
        }
    }
    override fun onPause() {
        super.onPause()
    }

}