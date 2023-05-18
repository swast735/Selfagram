package com.example.selfagram

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
class EditProfile : AppCompatActivity() {
    lateinit var pp:ImageView
    val fauth: FirebaseAuth = FirebaseAuth.getInstance()
    val uid=fauth.currentUser?.uid
    val uref:DatabaseReference=FirebaseDatabase.getInstance().reference.child("$uid")
    val strg: StorageReference =FirebaseStorage.getInstance().reference.child("$uid")
    lateinit var em:EditText
    lateinit var emNew:EditText
    lateinit var pass:EditText
    lateinit var sem:EditText
    lateinit var fn:EditText
    lateinit var ln:EditText
    lateinit var ud:Uri
    lateinit var upld:Button
    lateinit var cred:AuthCredential
    lateinit var pasbtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        pp = findViewById(R.id.profile_image)
        strg.downloadUrl.addOnSuccessListener {
            Picasso.get().load(it).into(pp)
        }
        em = findViewById(R.id.email)
        emNew = findViewById(R.id.emailNew)
        pass = findViewById(R.id.pass)
        sem = findViewById(R.id.sem)
        fn = findViewById(R.id.usn2)
        pasbtn=findViewById(R.id.pb)
        ln = findViewById(R.id.usn3)
        upld = findViewById(R.id.signUp)
        pass.visibility = View.INVISIBLE
        emNew.visibility=View.INVISIBLE
        val m=ProgressDialog(this)
        m.setMessage("Verifying...")
        pasbtn.setOnClickListener {
            if (em.text.toString().isNotBlank()) {
                pass.visibility = View.VISIBLE
                pasbtn.setOnClickListener {
                    m.show()
                  cred = EmailAuthProvider.getCredential(em.text.toString(), pass.text.toString())
                  fauth.currentUser?.reauthenticate(cred)?.addOnSuccessListener {
                      m.dismiss()
                      pasbtn.visibility=View.INVISIBLE
                      pasbtn.isEnabled=false
                      em.isEnabled=false
                      pass.isEnabled=false
                      emNew.visibility=View.VISIBLE
                  }?.addOnFailureListener {
                      Toast.makeText(this, "Incorrect Details", Toast.LENGTH_SHORT).show()
                  }
                }
            }
        }
        pp.setOnClickListener {
            val i = Intent(Intent.ACTION_GET_CONTENT)
            i.type = "image/*"
            startActivityForResult(i, 1)
        }
        upld.setOnClickListener {
            if(emNew.text.toString().isNotBlank()) {
                m.setMessage("Changing Things")
                m.show()
                fauth.currentUser?.reauthenticate(cred)?.addOnSuccessListener {
                    Log.d("nem",emNew.text.toString())
                    fauth.currentUser?.updateEmail(emNew.text.toString())
                    emNew.isEnabled=false
                    m.dismiss()
                }
            }
            if(sem.text.toString().isNotBlank())
                uref.child("Sem").setValue(sem.text.toString())
            if(fn.text.toString().isNotBlank()){
                uref.child("Name").setValue(fn.text.toString())
            }
            startActivity(Intent(this@EditProfile,FirstActivity::class.java))
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
            pp.setImageURI(ud)
            upld.setOnClickListener {
                val m=ProgressDialog(this)
                m.setMessage("Changing Things...")
                if (ud != null){
                    m.show()
                    strg.child("DP").putFile(ud).addOnCompleteListener {
                        if (it.isSuccessful) {
                            strg.downloadUrl.addOnSuccessListener {
                                val url=it
                                Picasso.get().load(it).into(pp)
                            }
                            m.dismiss()
                        } else
                            Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show()
                    }
            }

           }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@EditProfile,FirstActivity::class.java))
    }
}