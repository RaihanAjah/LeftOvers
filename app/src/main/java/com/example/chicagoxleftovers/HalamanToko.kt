package com.example.chicagoxleftovers

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chicagoxleftovers.databinding.ActivityBerandaPenjualBinding
import com.example.chicagoxleftovers.databinding.ActivityHalamanTokoBinding
import com.example.chicagoxleftovers.databinding.ActivityMetodePembayaranBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_beranda_penjual.*
import kotlinx.android.synthetic.main.activity_daftar_produk.*
import kotlinx.android.synthetic.main.activity_daftar_produk.menuList
import kotlinx.android.synthetic.main.activity_halaman_toko.*

class HalamanToko : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private val PICK_IMAGE_REQUEST = 12
    private lateinit var filepath: Uri


    //Firebase
    lateinit var storageReference: StorageReference

    //viewBinding
    private lateinit var binding: ActivityHalamanTokoBinding
    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private var mDatabaseRef: DatabaseReference? = null
    private var mStorageRef: StorageReference? = null
    private var mDBListener: ValueEventListener? = null

    private lateinit var mMenu:MutableList<Menu>
    private lateinit var listAdapter: MenuTokoAdapter
    private lateinit var menuRecyclerView: RecyclerView


    private lateinit var mToko:MutableList<Toko>
    private lateinit var mUser:MutableList<User>

    private lateinit var recyclerViewVertical: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHalamanTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id_toko = intent.getStringExtra("EXTRA_id")
        val nama_toko = intent.getStringExtra("EXTRA")
        val alamat_toko = intent.getStringExtra("EXTRA_alamat")

        binding.tvNamaToko.text = nama_toko
        binding.tvAlamatToko.text = alamat_toko

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        menuRecyclerView = findViewById(R.id.rvListMenuToko)
        menuRecyclerView.layoutManager = LinearLayoutManager(this@HalamanToko,
            LinearLayoutManager.VERTICAL, false)


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("menu")

        mMenu = ArrayList()
        listAdapter = MenuTokoAdapter(this@HalamanToko, mMenu)
        rvListMenuToko.adapter = listAdapter

        mDBListener = mDatabaseRef!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mMenu.clear()
                for (MenuSnapshot in snapshot.children){
                    if(id_toko == MenuSnapshot.getValue(Menu::class.java)!!.id_toko){
                        val upload = MenuSnapshot.getValue(Menu::class.java)
                        upload!!.id_menu = MenuSnapshot.key
                        mMenu.add(upload)
                    }
                }
                listAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HalamanToko,error.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    fun fKembali(view: android.view.View) {
        startActivity(Intent(this@HalamanToko, BerandaPembeli::class.java))
        finish()
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser


        if(firebaseUser == null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }


    fun fKePembayaran(view: android.view.View) {
        startActivity(Intent(this, MetodePembayaran::class.java))
        finish()
    }
}