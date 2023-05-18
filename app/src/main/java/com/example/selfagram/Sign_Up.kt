package com.example.selfagram

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
class Sign_Up : AppCompatActivity() {
    lateinit var dt:TextView
    lateinit var bt:Button
    lateinit var usrn:EditText
    lateinit var em:EditText
    lateinit var ps:EditText
    lateinit var psv:EditText
    lateinit var fn:EditText
    lateinit var ln:EditText
    lateinit var txt:TextView
    val c:Calendar= Calendar.getInstance()
    var y = c.get(Calendar.YEAR)
    var m = c.get(Calendar.MONTH)
    var d = c.get(Calendar.DAY_OF_MONTH)
    val fauth: FirebaseAuth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        dt=findViewById(R.id.date)
        bt=findViewById(R.id.signUp)
        usrn=findViewById(R.id.usn)
        em=findViewById(R.id.email)
        ps=findViewById(R.id.pass)
        psv=findViewById(R.id.passV)
        fn=findViewById(R.id.usn2)
        ln=findViewById(R.id.usn3)
        txt=findViewById(R.id.txt)
        dt.text=""
        val dd=DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            y = c.get(Calendar.YEAR)
            m = c.get(Calendar.MONTH)
            d = c.get(Calendar.DAY_OF_MONTH)
            dt.text = "$d - ${m+1} - $y"
            if(((Calendar.getInstance().get(Calendar.YEAR)*365+Calendar.getInstance().get(Calendar.MONTH)*30+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-(y*365+m*30+d))/365)<18){
                txt.text="Not Eligible!"
                bt.isEnabled=false
                usrn.isEnabled=false
                em.isEnabled=false
                ps.isEnabled=false
                psv.isEnabled=false
            }
            else {
                txt.text = "Eligible"
                bt.isEnabled=true
                usrn.isEnabled=true
                em.isEnabled=true
                ps.isEnabled=true
                psv.isEnabled=true
            }
        }
            dt.setOnClickListener {
               val dd= DatePickerDialog(this,dd,c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH))
                dd.show()
            }
        bt.setOnClickListener {
            val md=ProgressDialog(this)
            md.setMessage("Registering")
            md.show()
            when{
                fn.text.isEmpty()->{Toast.makeText(this,"Enter name",Toast.LENGTH_SHORT).show()
                md.dismiss()}
                usrn.text.isEmpty()-> {Toast.makeText(this, "Choose Username", Toast.LENGTH_SHORT).show()
                md.dismiss()}
                dt.text.equals("")-> {Toast.makeText(this, "Choose Date of Birth", Toast.LENGTH_SHORT).show()
                    md.dismiss()}
                em.text.isEmpty()-> {Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
                    md.dismiss()}
                ps.text.isEmpty()-> {Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
                    md.dismiss()}
                psv.text.isEmpty()-> {Toast.makeText(this, "Enter password again", Toast.LENGTH_SHORT).show()
                    md.dismiss()}
               psv.text.toString()!=ps.text.toString()->{Toast.makeText(this, "Password Not Matching", Toast.LENGTH_SHORT).show()
                   md.dismiss()}
                else->{

                    fauth.createUserWithEmailAndPassword(em.text.toString(),psv.text.toString()).addOnSuccessListener {
                            Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                        md.dismiss()
                        val uid= fauth.currentUser?.uid
                        val uref:DatabaseReference=FirebaseDatabase.getInstance().reference.child("$uid")
                        uref.child("Age").setValue(((Calendar.getInstance().get(Calendar.YEAR)*365+Calendar.getInstance().get(Calendar.MONTH)*30+Calendar.getInstance().get(Calendar.DAY_OF_MONTH)-(y*365+m*30+d))/365).toString())
                        uref.child("Name").setValue(fn.text.toString())
                        uref.child("DOB").setValue(dt.text.toString())
                        startActivity(Intent(this@Sign_Up,SignUp::class.java))
                    }.addOnFailureListener{
                        Toast.makeText(this, "${it.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
    }
}