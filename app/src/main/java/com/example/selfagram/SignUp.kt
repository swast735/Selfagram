package com.example.selfagram

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {
    lateinit var usn:EditText
    lateinit var pas:EditText
    lateinit var lg:Button
    lateinit var sp:SharedPreferences
    val fauth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        sp=getSharedPreferences(getString(R.string.SharPref),Context.MODE_PRIVATE)
        title = ""
        super.onCreate(savedInstanceState)
        Log.d("msg1",sp.getBoolean("IsloggedIn",false).toString())
        if(sp.getBoolean("IsloggedIn",false)){
            Log.d("msg2",sp.getBoolean("IsloggedIn",false).toString())
        startActivity(Intent(this@SignUp,FirstActivity::class.java))
            this.finish()
        }
        setContentView(R.layout.sign_up)
        usn = findViewById(R.id.usn)
        pas = findViewById(R.id.pass)
        lg = findViewById(R.id.log)
        val m = ProgressDialog(this)
        lg.setOnClickListener {
            when {
                usn.text.toString().isEmpty() -> Toast.makeText(
                    this,
                    "Enter valid username",
                    Toast.LENGTH_SHORT
                ).show()
                pas.text.toString().isEmpty() -> Toast.makeText(
                    this,
                    "Enter Password",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    m.setMessage("Signing In...")
                    m.show()
                    fauth.signInWithEmailAndPassword(usn.text.toString(), pas.text.toString())
                        .addOnSuccessListener {
                            m.dismiss()
                            sp.edit().putBoolean("IsloggedIn",true).apply()
                            Log.d("msg",sp.getBoolean("IsloggedIn",false).toString())
                            startActivity(Intent(this@SignUp, FirstActivity::class.java))
                        }.addOnFailureListener {
                        Toast.makeText(this, "Incorrect Details", Toast.LENGTH_SHORT).show()
                            m.dismiss()
                    }
                }
            }
        }
    }
    fun signUp(view: View) {
        startActivity(Intent(this@SignUp,Sign_Up::class.java))
    }
    fun frgtpass(view: View) {
        startActivity(Intent(this@SignUp,Recovery::class.java))
    }
    override fun onPause() {
        super.onPause()
        if(sp.getBoolean("IsloggedIn",false))
        {
            finish()
        }
    }
}