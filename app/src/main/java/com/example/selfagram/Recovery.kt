package com.example.selfagram

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class Recovery : AppCompatActivity() {

    lateinit var dt:TextView
    lateinit var rc:Button
    lateinit var em:EditText
    lateinit var msg:TextView
    val c:Calendar = Calendar.getInstance()
    var d=c.get(Calendar.DAY_OF_MONTH)
    var m=c.get(Calendar.MONTH)
    var y=c.get(Calendar.YEAR)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recovery)
        dt=findViewById(R.id.date)
        rc=findViewById(R.id.log)
        em=findViewById(R.id.email)
        msg=findViewById(R.id.msg)
        dt.text=""
        val dd=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
         c.set(Calendar.YEAR,year)
         c.set(Calendar.MONTH,month)
         c.set(Calendar.DAY_OF_MONTH,dayOfMonth)
         d=c.get(Calendar.DAY_OF_MONTH)
         m=c.get(Calendar.MONTH)
         y=c.get(Calendar.YEAR)
         dt.text = "$d - ${m+1} - $y"
        }
        dt.setOnClickListener{
            DatePickerDialog(this,dd,y,m,d).show()
        }
        val fauth:FirebaseAuth=FirebaseAuth.getInstance()
        val uid=fauth.currentUser?.uid.toString()
        val uref:DatabaseReference=FirebaseDatabase.getInstance().getReference(uid)
        rc.setOnClickListener {
                        fauth.sendPasswordResetEmail(em.text.toString()).addOnCompleteListener {
                            if (it.isSuccessful) {
                                msg.text = "Mail Sent!!!"
                                Toast.makeText(this@Recovery, "Check your Mail", Toast.LENGTH_SHORT)
                                    .show()
                                rc.visibility = View.INVISIBLE
                            }
                        }
        }
    }
}