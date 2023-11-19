package com.example.chicagoxleftovers

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.chicagoxleftovers.databinding.ActivityLoginPembeliBinding
import com.example.chicagoxleftovers.databinding.ActivityLoginPenjualBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class LoginPembeli : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityLoginPembeliBinding

    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPembeliBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Mohon tunggu sebentar")
        progressDialog.setMessage("Masuk...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
    }

    fun fLoginPembeli(view: android.view.View) {
        validateData()
    }

    fun fKeRegisterPembeli(view: android.view.View) {
        val intKeRegisterPembeli = Intent(this, RegisterPembeli::class.java)
        startActivity(intKeRegisterPembeli)
    }
    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser


        if(firebaseUser != null){
            startActivity(Intent(this, BerandaPembeli::class.java))
            finish()
        }

    }
    private fun validateData() {
        //get data
        email = binding.etEmailPembeli.text.toString().trim()
        password = binding.etPasswordPembeli.text.toString().trim()

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //user is already logged in
            Toast.makeText(this, "Format email salah", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            //no password entered
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else{
            fLoginPembeli()
        }
    }

    private fun fLoginPembeli() {
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
//                checkUser()

                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "login as $email", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, BerandaPembeli::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this, "login fail ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}