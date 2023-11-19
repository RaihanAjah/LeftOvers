package com.example.chicagoxleftovers

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.example.chicagoxleftovers.databinding.ActivityLoginPenjualBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class LoginPenjual : AppCompatActivity() {

    //viewBinding
    private lateinit var binding: ActivityLoginPenjualBinding

    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPenjualBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //configure progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Mohon tunggu sebentar")
        progressDialog.setMessage("Masuk...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun checkUser() {
        progressDialog.setMessage("Mengecek User...")

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = Firebase.database.getReference("toko")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    val value = snapshot.getValue<String>()
//                    Log.d(TAG, "Value is: " + value)
                }

                override fun onCancelled(error : DatabaseError){

                }


            })
    }

    fun fKeRegisterPenjual(view : View){
        val intKeRegister = Intent(this, RegisterPenjual::class.java)
        startActivity(intKeRegister)
    }

    fun fLogin(view : View){
        //before login validate data
        validateData()
    }

    private fun validateData() {
        //get data
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

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
            loginToko()
        }
    }

    private fun loginToko() {
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

                //
                startActivity(Intent(this, BerandaPenjual::class.java))
                finish()
            }
            .addOnFailureListener{ e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this, "login fail ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
}