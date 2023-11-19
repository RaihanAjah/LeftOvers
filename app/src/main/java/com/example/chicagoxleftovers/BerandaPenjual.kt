package com.example.chicagoxleftovers

import android.accounts.AccountManager.get
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ClipDrawable.VERTICAL
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.chicagoxleftovers.databinding.ActivityBerandaPenjualBinding
import com.example.chicagoxleftovers.databinding.ActivityLoginPenjualBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.lang.StringBuilder
import com.google.firebase.storage.StorageReference
import android.provider.MediaStore
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_beranda_pembeli.*
import kotlinx.android.synthetic.main.activity_beranda_penjual.*


class BerandaPenjual : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private val PICK_IMAGE_REQUEST = 12
    private lateinit var filepath: Uri

    //Firebase
    lateinit var storageReference: StorageReference

    //viewBinding
    private lateinit var binding: ActivityBerandaPenjualBinding

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private var mDatabaseRef: DatabaseReference? = null
    private var mStorageRef: StorageReference? = null
    private var mDBListener: ValueEventListener? = null

    private lateinit var mToko:MutableList<Toko>
    private lateinit var mPemesan:MutableList<Pemesan>
    private lateinit var listAdapterPemesan: PemesanAdapter

    private lateinit var recyclerViewVertical: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBerandaPenjualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        recyclerViewVertical = findViewById(R.id.rvListPemesan)
        recyclerViewVertical.layoutManager = LinearLayoutManager(this@BerandaPenjual,
            LinearLayoutManager.VERTICAL,false)

        mPemesan = ArrayList()
        listAdapterPemesan = PemesanAdapter(this@BerandaPenjual, mPemesan)
        rvListPemesan.adapter = listAdapterPemesan

        mDBListener = mDatabaseRef!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mPemesan.clear()
                for (PemesanSnapshot in snapshot.children){
                    if(firebaseAuth.currentUser!!.uid == PemesanSnapshot.getValue(Pemesan::class.java)!!.id_toko){
                        val upload = PemesanSnapshot.getValue(Pemesan::class.java)
                        upload!!.id_user = PemesanSnapshot.key
                        mPemesan.add(upload)
                    }
                }
                listAdapterPemesan.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@BerandaPenjual,error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


    fun fKeTambahMenu(view : View){
        startActivity(Intent(this, TambahMenu::class.java))
        finish()
    }

    fun fKeDaftarProduk(view : View){
        startActivity(Intent(this, DaftarProduk::class.java))
        finish()
    }

    fun fKeProfil(view : View){
        startActivity(Intent(this, ProfilePenjual::class.java))
        finish()
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
                                Glide.with(this@BerandaPenjual)
                                    .load(dataUri)
                                    .into(binding.ivProfilePenjual)
                            }
                                .addOnFailureListener{
                                    Log.d("Error : ", it.toString())
                                }

                            val alamat = TokoSnapshot.getValue(Toko::class.java)!!.alamat_toko
                            val namaToko = TokoSnapshot.getValue(Toko::class.java)!!.nama_toko
                            binding.tvAlamat.text = alamat
                            binding.tvNamaToko.text = namaToko

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

//            val Alamat = FirebaseDatabase.getInstance().getReference("Toko")

            //set to textvie
//            binding.tvAlamat.text = email

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            filepath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
                binding.ivProfilePenjual.setImageBitmap(bitmap)
            } catch ( e: Exception  ) {
                e.printStackTrace()
            }
        }
    }
}