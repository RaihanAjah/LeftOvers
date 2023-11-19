package com.example.chicagoxleftovers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.chicagoxleftovers.databinding.ActivityProfilePembeliBinding
import com.example.chicagoxleftovers.databinding.ActivityProfilePenjualBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage


class ProfilePembeli : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityProfilePembeliBinding

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private var mDatabaseRef: DatabaseReference? = null
    private var mDBListener: ValueEventListener? = null

    private lateinit var mUser:MutableList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePembeliBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()


    }

    fun fLogout(view: android.view.View) {
        firebaseAuth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun fKeBerandaPembeli(view: android.view.View) {
        startActivity(Intent(this, BerandaPembeli::class.java))
        finish()
    }

    fun fKeJelajah(view: android.view.View) {
        startActivity(Intent(this, Jelajah::class.java))
        finish()
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

            mDBListener = mDatabaseRef!!.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    mUser.clear()
                    for (UserSnapshot in snapshot.children){
                        if(firebaseAuth.currentUser!!.uid == UserSnapshot.getValue(User::class.java)!!.id_user){

                            var hemat = UserSnapshot.getValue(User::class.java)!!.poin
                            var status : String

                            val email = UserSnapshot.getValue(User::class.java)!!.email
                            val nama = UserSnapshot.getValue(User::class.java)!!.nama_pembeli
                            val user = UserSnapshot.getValue(User::class.java)!!.id_user
                            val storageReference = FirebaseStorage.getInstance().getReference("User/$user.jpg")

                            storageReference.downloadUrl.addOnSuccessListener { dataUri->
                                Glide.with(this@ProfilePembeli)
                                    .load(dataUri)
                                    .into(binding.ivProfilePengguna)
                            }
                                .addOnFailureListener{
                                    Log.d("Error : ", it.toString())
                                }

                            if (hemat != null && hemat < 100000) {
                                status = "Voucher anda sebagai member Bronze"
                                binding.tvStatus.text = status
                            }
                            else if(hemat!! < 500000 && hemat!! >= 100000){
                                status = "Voucher anda sebagai member Silver"
                                binding.tvStatus.text = status
                            }
                            else if(hemat!! >= 500000){
                                status = "Voucher anda sebagai member Gold"
                                binding.tvStatus.text = status
                            }
                            binding.tvJmlhHemat.text = hemat.toString()
                            binding.tvNamaPengguna.text = nama
                            binding.tvEmailPengguna.text = email
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }


    }

}