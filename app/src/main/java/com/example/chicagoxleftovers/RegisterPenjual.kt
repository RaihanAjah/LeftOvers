package com.example.chicagoxleftovers

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.chicagoxleftovers.databinding.ActivityRegisterPenjualBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.FirebaseDatabaseKtxRegistrar
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register_penjual.*
import kotlinx.android.synthetic.main.activity_tambah_menu.*

class RegisterPenjual : AppCompatActivity() {

    private var mImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 100
    private var mStorageRef: StorageReference? = null

    //viewBinding
    private lateinit var binding: ActivityRegisterPenjualBinding

    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    private lateinit var database : DatabaseReference

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var namaToko = ""
    private var alamat = ""
    private var noTlp = ""
    private var fotoToko = ""
    private var username = ""
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterPenjualBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        //configure actionbar
//        actionBar = supportActionBar!!
//        actionBar.title = "Register"
//        actionBar.setDisplayHomeAsUpEnabled(true)
//        actionBar.setDisplayShowHomeEnabled(true)
//
        mStorageRef = FirebaseStorage.getInstance().getReference("Toko")

        //configure progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Mohon tunggu sebentar")
        progressDialog.setMessage("Membuat akun...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

    }

    fun milihGambar(view: View){
        openFileChoose()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST
            && resultCode == RESULT_OK
            && data != null
            && data.data != null
        ){

            mImageUri = data?.data!!
            ivFotoProfil.setImageURI(mImageUri)

        }
    }

    private fun openFileChoose() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)

    }

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }


    fun fKeLoginPenjual(view : View){
        startActivity(Intent(this@RegisterPenjual, LoginPenjual::class.java))
        finish()
//        val intKeLogin = Intent(this, LoginPenjual::class.java)
//        startActivity(intKeLogin)
    }

    fun fRegister(view : View){
        validateData()

    }

    private fun validateData() {
        //input data
        namaToko = binding.etNamaToko.text.toString().trim()
        alamat = binding.etAlamat.text.toString().trim()
        noTlp = binding.etNoTelp.text.toString().trim()
        fotoToko = binding.ivFotoProfil.toString()
        username = binding.etUsername.text.toString().trim()
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()
        val cPassword = binding.etUlangiPassword.text.toString().trim()

        //validate data
        if(namaToko.isEmpty()){
            Toast.makeText(this, "Nama toko tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(alamat.isEmpty()){
            Toast.makeText(this, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(noTlp.isEmpty()){
            Toast.makeText(this, "Nomor telepon tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(fotoToko == null){
            Toast.makeText(this, "Foto tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(username.isEmpty()){
            Toast.makeText(this, "Usernam tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Format email salah", Toast.LENGTH_SHORT).show()
        }
        else if(password.isEmpty()){
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(cPassword.isEmpty()){
            Toast.makeText(this, "Masukkan ulang password", Toast.LENGTH_SHORT).show()
        }
        else if(password != cPassword){
            Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
        }
        else{
            createAccount()
        }
    }

    private fun createAccount() {
        //show progress
        progressDialog.setMessage("Membuat akun...")
        progressDialog.show()

        //create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                updateTokoInfo()
//                Toast.makeText(this, "masuk sini", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{ e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this, "gagal membuat akun ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateTokoInfo() {
        //menambah user info ke dalam database
        progressDialog.setMessage("Menyimpan info toko...")

        //timestamp
        val timestamp = System.currentTimeMillis()

        //get current user uid
        val id_user = firebaseAuth.uid

        //setup data to add db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["id_user"] = id_user
        hashMap["nama_toko"] = namaToko
        hashMap["alamat_toko"] = alamat
        hashMap["no_Tlp"] = noTlp
        hashMap["foto_toko"] = id_user + "." + getFileExtension(mImageUri!!)
        hashMap["username"] = username
        hashMap["email"] = email
        hashMap["password"] = password
        hashMap["timestamp"] = timestamp

        //upload image to database
        val fileReference = mStorageRef!!.child(
            id_user + "." + getFileExtension(mImageUri!!)
        )
        fileReference.putFile(mImageUri!!)

        //set data to db
        val ref = FirebaseDatabase.getInstance().getReference("toko")

        ref.child(id_user!!).setValue(hashMap)
        progressDialog.dismiss()

        startActivity(Intent(this@RegisterPenjual, LoginPenjual::class.java))
        finish()

    }
}