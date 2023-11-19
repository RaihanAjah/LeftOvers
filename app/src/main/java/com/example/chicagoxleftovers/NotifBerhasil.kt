package com.example.chicagoxleftovers

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.chicagoxleftovers.databinding.ActivityBerandaPembeliBinding
import com.example.chicagoxleftovers.databinding.ActivityNotifBerhasilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_notif_berhasil.*

class NotifBerhasil : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityNotifBerhasilBinding

    private lateinit var mUser:MutableList<User>
    private var mDatabaseRef: DatabaseReference? = null
    private var mDBListener: ValueEventListener? = null

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifBerhasilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        btRating.setOnClickListener{
            var dialog = CustomDialogFragment()

            dialog.show(supportFragmentManager,"customDialog")
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser


        if(firebaseUser == null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{

            mUser = ArrayList()
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("user")

            mDBListener = mDatabaseRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mUser.clear()
                    for (UserSnapshot in snapshot.children){
                        if(firebaseAuth.currentUser!!.uid == UserSnapshot.getValue(User::class.java)!!.id_user){
                            val user = UserSnapshot.getValue(User::class.java)!!.id_user
                            val alamat = UserSnapshot.getValue(User::class.java)!!.alamat_pembeli
                        }


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
    }

    fun fKeBeranda(view: android.view.View) {
        val intKeBeranda = Intent(this, BerandaPembeli::class.java)
        startActivity(intKeBeranda)
    }



//    fun fRating(view: android.view.View) {
//        val intKeRating = Intent(this, BerandaPembeli::class.java)
//        startActivity(intKeRating)
//
//    }
}