package com.example.chicagoxleftovers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chicagoxleftovers.databinding.ActivityProfilePenjualBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_daftar_produk.*
import kotlinx.android.synthetic.main.activity_profile_penjual.*
import java.util.HashMap

class ProfilePenjual : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityProfilePenjualBinding

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private var mDatabaseRef: DatabaseReference? = null
    private var mDatabaseRefToko: DatabaseReference? = null
    private var mDBListener: ValueEventListener? = null
    private lateinit var mRating:MutableList<Rating>
    private lateinit var listAdapter: RatingAdapter

    private lateinit var mToko:MutableList<Toko>

    private var rating : Double= 0.0
    private var ratingRata : Double= 0.0
    private var userRating : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePenjualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ratingList.setHasFixedSize(false)
        ratingList.layoutManager = LinearLayoutManager(this@ProfilePenjual)

        mRating = ArrayList()
        listAdapter = RatingAdapter(this@ProfilePenjual, mRating)
        ratingList.adapter = listAdapter

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("rating")
        mDatabaseRefToko = FirebaseDatabase.getInstance().getReference("toko")

        mDBListener = mDatabaseRef!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mRating.clear()
                for (RatingSnapshot in snapshot.children){
                    if(firebaseAuth.currentUser!!.uid == RatingSnapshot.getValue(Menu::class.java)!!.id_toko) {
                        val upload = RatingSnapshot.getValue(Rating::class.java)
                        upload!!.id_Rating = RatingSnapshot.key
                        rating =  rating + upload.rating!!
                        userRating += 1
//                        userRating = upload.namaPerating!!.length.toDouble()
                        mRating.add(upload)
                    }
                }
                ratingRata = rating/userRating
                val hashMap: HashMap<String, Any?> = HashMap()
                hashMap["rating"] = ratingRata
//                mDatabaseRefToko!!.child(firebaseAuth.currentUser!!.uid).updateChildren(hashMap)

                binding.tvRating.text = ratingRata.toString()

                listAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfilePenjual,error.message, Toast.LENGTH_SHORT).show()
            }

        })

        //init firebaseAuth

    }

    fun fLogout(view : View){
        firebaseAuth.signOut()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser


        if(firebaseUser == null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{

            //login get and dhow user info
            val email = firebaseUser.email

            mToko = ArrayList()
            mDatabaseRef = FirebaseDatabase.getInstance().getReference("toko")

            mDBListener = mDatabaseRef!!.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    mToko.clear()
                    for (TokoSnapshot in snapshot.children){
                        if(firebaseAuth.currentUser!!.uid == TokoSnapshot.getValue(Toko::class.java)!!.id_user){

                            val user = TokoSnapshot.getValue(Toko::class.java)!!.id_user
                            val storageReference = FirebaseStorage.getInstance().getReference("Toko/$user.jpg")

                            storageReference.downloadUrl.addOnSuccessListener { dataUri->
                                Glide.with(this@ProfilePenjual)
                                    .load(dataUri)
                                    .into(binding.ivppPenjual)
                            }
                                .addOnFailureListener{
                                    Log.d("Error : ", it.toString())
                                }




                            val alamat = TokoSnapshot.getValue(Toko::class.java)!!.alamat_toko
                            val namaToko = TokoSnapshot.getValue(Toko::class.java)!!.nama_toko
                            val nomorTlp = TokoSnapshot.getValue(Toko::class.java)!!.no_Tlp
                            binding.tvAlamat.text = alamat
                            binding.tvNamaToko.text = namaToko
                            binding.tvNoTlp.text = nomorTlp
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })



        }
    }

    fun fKembali(view: android.view.View) {
        startActivity(Intent(this@ProfilePenjual, BerandaPenjual::class.java))
        finish()
    }
}