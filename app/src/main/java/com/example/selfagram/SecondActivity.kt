package com.example.selfagram

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import kotlin.random.Random

class SecondActivity : AppCompatActivity(){
    private val fauth: FirebaseAuth = FirebaseAuth.getInstance()
    private val uid=fauth.currentUser?.uid
    val strg: StorageReference = FirebaseStorage.getInstance().reference.child("$uid").child("Images")
    private val uref: DatabaseReference = FirebaseDatabase.getInstance().reference.child("$uid").child("Galley")
    private lateinit var btm:BottomNavigationView
    lateinit var picRecList:ArrayList<Pic>
    lateinit var picRecview: RecyclerView
    lateinit var ap:Button
    lateinit var ud: Uri
    private var rnd=0
    var purl= arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        btm=findViewById(R.id.bottomNavigationView)
        btm.selectedItemId = R.id.home
        btm.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.MyProfile->{startActivity(Intent(this@SecondActivity,FirstActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true }
                R.id.home->{startActivity(Intent(this@SecondActivity,SecondActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true}
                R.id.about->{startActivity(Intent(this@SecondActivity,ThirdActivity::class.java))
                    overridePendingTransition(0,0)
                    return@setOnNavigationItemSelectedListener true}
            }
            false
        }
        picRecview=findViewById(R.id.piclist)
        picRecview.layoutManager= GridLayoutManager(this,3)
        picRecList= arrayListOf()
        uref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    for(ds in snapshot.children){
                        purl.add(ds.child("Pic").value.toString())
                        var pics=ds.getValue(Pic::class.java)
                        picRecList.add(pics!!)
                    }
                    val adapter=MyAdapter(picRecList,this@SecondActivity)
                    picRecview.adapter=adapter
                    adapter.setOnPicClickListener(object :MyAdapter.Recclicklist{
                        override fun onPicClick(pos: Int) {
                            Log.d("msg",purl[pos].toString())
                            Toast.makeText(this@SecondActivity, purl[pos].toString(), Toast.LENGTH_LONG).show()
                            val alt= Dialog(this@SecondActivity)
                            alt.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            alt.setCancelable(true)
                            val im = ImageView(this@SecondActivity)
                            Picasso.get().load(purl[pos]).into(im)
                            alt.addContentView(im,RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT))
                            alt.show()
                        }
                    })
                }
                else
                    Toast.makeText(this@SecondActivity, "Pics can't be fetched", Toast.LENGTH_SHORT).show()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SecondActivity,error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        ap=findViewById(R.id.adpi)
        ap.setOnClickListener {
            rnd=Random.nextInt()
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/*"
            startActivityForResult(i, 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ud = data?.data!!
            CropImage.activity(ud).start(this)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val res: CropImage.ActivityResult = CropImage.getActivityResult(data)
            ud = res.uri
            val m=ProgressDialog(this)
            m.setMessage("Changing Things...")
            if (ud != null){
                m.show()
                strg.child(rnd.toString()).putFile(ud).addOnCompleteListener {
                    if (it.isSuccessful) {
                        strg.child(rnd.toString()).downloadUrl.addOnSuccessListener {
                            Log.d("msg",it.toString())
                            uref.child(Random.nextInt().toString()).child("Pic").setValue(it.toString())
                            }
                            m.dismiss()
                    }else
                            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}