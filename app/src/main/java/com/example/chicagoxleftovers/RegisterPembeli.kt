package com.example.chicagoxleftovers

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.example.chicagoxleftovers.databinding.ActivityRegisterPembeliBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_register_pembeli.*

class RegisterPembeli : AppCompatActivity() {

    private var mImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 100
    private var mStorageRef: StorageReference? = null

    //viewBinding
    private lateinit var binding: ActivityRegisterPembeliBinding

    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    private lateinit var database : DatabaseReference

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var fotoPembeli = ""
    private var namaPembeli = ""
    private var alamatPembeli = ""
    private var username = ""
    private var email = ""
    private var poin = 0
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterPembeliBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mStorageRef = FirebaseStorage.getInstance().getReference("User")

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

    private fun openFileChoose() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST
            && resultCode == RESULT_OK
            && data != null
            && data.data != null
        ){

            mImageUri = data?.data!!
            ivFProfil.setImageURI(mImageUri)

        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }

    fun fKeLoginPembeli(view : View){
        startActivity(Intent(this@RegisterPembeli, LoginPembeli::class.java))
        finish()

    }

    fun fRegister(view : View){
        validateData()
    }

    private fun validateData() {
        //input data
        fotoPembeli = binding.ivFProfil.toString()
        namaPembeli = binding.etNamaPembeli.text.toString().trim()
        alamatPembeli = binding.etAlamatPembeli.text.toString().trim()
        username = binding.etUsernamePembeli.text.toString().trim()
        email = binding.etEmailPembeli.text.toString().trim()
        password = binding.etPasswordPembeli.text.toString().trim()
        val cPassword = binding.etUlangiPasswordPembeli.text.toString().trim()

        //validate data
        if(username.isEmpty()){
            Toast.makeText(this, "Username tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(namaPembeli.isEmpty()){
            Toast.makeText(this, "Nama pembeli tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(alamatPembeli.isEmpty()){
            Toast.makeText(this, "Alamat pembeli tidak boleh kosong tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(fotoPembeli == null){
                Toast.makeText(this, "Foto tidak boleh kosong", Toast.LENGTH_SHORT).show()
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
                updateUserInfo()
            }
            .addOnFailureListener{ e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this, "gagal membuat akun ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo() {
        //menambah user info ke dalam database
        progressDialog.setMessage("Menyimpan info user...")

        //timestamp
        val timestamp = System.currentTimeMillis()

        //get current user uid
        val id_user = firebaseAuth.uid

        //setup data to add db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["id_user"] = id_user
        hashMap["nama_pembeli"] = namaPembeli
        hashMap["alamat_pembeli"] = alamatPembeli
        hashMap["foto_user"] = id_user + "." + getFileExtension(mImageUri!!)
        hashMap["username"] = username
        hashMap["email"] = email
        hashMap["password"] = password
        hashMap["poin"] = 0
        hashMap["timestamp"] = timestamp

        //upload image to database
        val fileReference = mStorageRef!!.child(
            id_user + "." + getFileExtension(mImageUri!!)
        )
        fileReference.putFile(mImageUri!!)

        //set data to db
        val ref = FirebaseDatabase.getInstance().getReference("user")

        ref.child(id_user!!).setValue(hashMap)
        progressDialog.dismiss()

        startActivity(Intent(this@RegisterPembeli, LoginPembeli::class.java))
        finish()

    }
}