package com.example.chicagoxleftovers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.chicagoxleftovers.databinding.ActivityJelajahBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Jelajah : AppCompatActivity() {
    private lateinit var  binding: ActivityJelajahBinding

    private lateinit var  firebaseAuth: FirebaseAuth

    private lateinit var storeArrayList: ArrayList<modelStore>
    private lateinit var adapterCategory: AdapterStoreList



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJelajahBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadStore()

        //search
        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //called asa and when user type anything
                try{
                    adapterCategory.filter.filter(s)
                }catch(e: Exception){

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun loadStore() {
        storeArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("toko")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting add data
                storeArrayList.clear()
                for(ds in snapshot.children){
                    //get data as model
                    val model = ds. getValue(modelStore::class.java)

                    //add to arraylist
                    storeArrayList.add(model!!)
                }
                //setup adapter
                adapterCategory = AdapterStoreList(this@Jelajah, storeArrayList)
                //set adapter to recycleview
                binding.rvListStore.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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

            //set to textvie
//            binding.tv.text = email


        }
    }

    fun fKeToko(view: android.view.View) {
        startActivity(Intent(this, HalamanToko::class.java))
        finish()
    }

    fun fKeBerandaPembeli(view: android.view.View) {
        startActivity(Intent(this, BerandaPembeli::class.java))
        finish()
    }
    fun fKeProfilePembeli(view: android.view.View) {
        startActivity(Intent(this, ProfilePembeli::class.java))
        finish()
    }
}
