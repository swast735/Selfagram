package com.example.selfagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.EventListener
import java.util.Objects
import java.util.Random

class FirstActivity : AppCompatActivity() {
    lateinit var btm:BottomNavigationView
    lateinit var pp:de.hdodenhof.circleimageview.CircleImageView
    lateinit var age:TextView
    lateinit var sem:TextView
    lateinit var name:TextView
    lateinit var edit:Button
    lateinit var note:EditText
    lateinit var notep:TextView
    lateinit var sb:Button
    lateinit var eb:Button
    val fauth:FirebaseAuth= FirebaseAuth.getInstance()
    val uid=fauth.currentUser?.uid
    val uref:DatabaseReference=FirebaseDatabase.getInstance().reference.child("$uid")
    val strg: StorageReference = FirebaseStorage.getInstance().reference.child("$uid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        btm=findViewById(R.id.bottomNavigationView)
        pp=findViewById(R.id.profile_image)
        btm.setSelectedItemId(R.id.MyProfile)
        age=findViewById(R.id.ageno)
        eb=findViewById(R.id.edno)
        sb=findViewById(R.id.shno)
        sem=findViewById(R.id.semno)
        name=findViewById(R.id.name)
        edit=findViewById(R.id.edit)
        note=findViewById(R.id.note)
        notep=findViewById(R.id.notep)
        eb.visibility=View.INVISIBLE
        notep.visibility=View.INVISIBLE
        uref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                age.text=snapshot.child("Age").value.toString()
                name.text="Hello, "+(snapshot.child("Name").value.toString())
                sem.text=snapshot.child("Sem").value.toString()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
        strg.child("DP").downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(pp)
        }
        btm.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.MyProfile->{startActivity(Intent(this@FirstActivity,FirstActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true }
                R.id.home->{startActivity(Intent(this@FirstActivity,SecondActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true}
                R.id.about->{startActivity(Intent(this@FirstActivity,ThirdActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true}
            }
            false
        }
        val Nref:DatabaseReference=FirebaseDatabase.getInstance().reference.child("$uid")
        sb.setOnClickListener {
            if (note.text.isNotBlank()) {
                Nref.child("Notes").child(kotlin.random.Random.nextInt().toString())
                    .setValue(note.text!!.toString())
            }
             Nref.child("Notes").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        notep.text=null
                        for (ds in snapshot.children) {
                            notep.append(ds.value.toString() + "\n")
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
             })
             note.visibility = View.INVISIBLE
             sb.visibility = View.INVISIBLE
             notep.visibility = View.VISIBLE
             eb.visibility = View.VISIBLE
        }
        eb.setOnClickListener {

            note.visibility=View.VISIBLE
            sb.visibility=View.VISIBLE
            notep.visibility=View.INVISIBLE
            eb.visibility=View.INVISIBLE
        }
        edit.setOnClickListener {
            startActivity(Intent(this@FirstActivity,EditProfile::class.java))
        }
    }
    override fun onPause() {
        super.onPause()
        finish()
    }
}