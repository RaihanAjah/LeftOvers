package com.example.chicagoxleftovers

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.chicagoxleftovers.databinding.ActivityTambahMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import kotlinx.android.synthetic.main.activity_tambah_menu.*
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.logging.Handler
import java.util.logging.SimpleFormatter

class TambahMenu : AppCompatActivity(), DatePickerDialog.OnDateSetListener  {

    private var mImageUri: Uri? = null
    private var mStorageRef: StorageReference? = null
    private var mDatabaseRef: DatabaseReference? = null
    private var mUploadTask: StorageTask<*>? = null
    private val PICK_IMAGE_REQUEST = 100

    //viewBinding
    private lateinit var binding: ActivityTambahMenuBinding

    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

//    private lateinit var etTanggal : etTanggal

    private var namaMenu = ""
    private var hargaAsli = 0
    private var hargaDiskon = 0
    private var tanggalProduksi = ""
    private var fotoProduk = ""
    private var deskripsi = ""

    var tanggal = 0
    var bulan = 0
    var tahun = 0
    lateinit var teksTanggal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mStorageRef = FirebaseStorage.getInstance().getReference("menu")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("menu")

//        var database = FirebaseDatabase.getInstance().reference

    //configure progress Dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Mohon tunggu sebentar")
        progressDialog.setMessage("Membuat menu...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

    }

    fun getSaatIni(){
        val kal: Calendar = Calendar.getInstance()
        tanggal = kal.get(Calendar.DAY_OF_MONTH)
        bulan = kal.get(Calendar.MONTH)
        tahun = kal.get(Calendar.YEAR)
    }

    fun fSetTanggal(view: View){
        getSaatIni()
        DatePickerDialog(this, this, tahun, bulan, tanggal).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        tanggal = dayOfMonth
        bulan = month
        tahun = year
        teksTanggal = findViewById(R.id.tTanggal)
        teksTanggal.text = "${tanggal} - ${bulan} - ${tahun}"
    }

    fun pilihGambar(view: View){
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
            ivFotoMenu.setImageURI(mImageUri)

        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }


    private fun uploadFile() {

        if(mImageUri != null){
            val fileReference = mStorageRef!!.child(
                FirebaseDatabase.getInstance().getReference("menu").toString() +"." + getFileExtension(mImageUri!!)
            )
            fileReference.putFile(mImageUri!!)
        }
        else{
            Toast.makeText(this, "tidak ada data yang dipilih", Toast.LENGTH_SHORT).show()
        }



    }

//    fun datePicker(view: View){
//        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view,mYear,mMonth,mDay ->
//            etTanggal.setText(""+ mDay +"/" + mMonth + "/" + mYear )
//        }, year,month, day)
//    }

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

    fun fSimpanMenu(view: View) {
        namaMenu = binding.etNamaMenu.text.toString().trim()
        hargaAsli = binding.etHargaAsli.text.toString().toInt()
        hargaDiskon = binding.etHargaDiskon.text.toString().toInt()
        tanggalProduksi = binding.tTanggal.text.toString().trim()
        fotoProduk = binding.ivFotoMenu.toString()
        deskripsi = binding.etDeskripsi.text.toString().trim()

        //validate data
        if(namaMenu.isEmpty()){
            Toast.makeText(this, "Nama menu tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(hargaAsli == null){
            Toast.makeText(this, "Harga asli tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(hargaDiskon == null){
            Toast.makeText(this, "Harga Diskon tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(mImageUri == null){
            Toast.makeText(this, "Foto tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(tanggalProduksi.isEmpty()){
            Toast.makeText(this, "Tanggal produksi tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else if(deskripsi.isEmpty()){
            Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
        else{
            saveData()

        }

    }

    private fun saveData() {
        //show progress
        progressDialog.setMessage("Menyimpan data")
        progressDialog.show()

//        val database = Firebase.database
//        val ref      = database.getReference("menu")
        val ref = FirebaseDatabase.getInstance().getReference("menu")

        //get id user
        val id_toko = FirebaseAuth.getInstance().currentUser?.uid

        //get current user uid
        val id_menu = ref.push().key

        //setup data to add db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["id_menu"] = id_menu
        hashMap["nama_menu"] = namaMenu
        hashMap["harga_asli"] = hargaAsli
        hashMap["harga_diskon"] = hargaDiskon
        hashMap["tanggal_produksi"] = tanggalProduksi
        hashMap["foto_produk"] = id_menu + "." + getFileExtension(mImageUri!!)
        hashMap["deskripsi"] = deskripsi
        hashMap["id_toko"] = "$id_toko"

        //upload image to database
        val fileReference = mStorageRef!!.child(
            id_menu + "." + getFileExtension(mImageUri!!)
        )
        fileReference.putFile(mImageUri!!)

        //set data to db
        ref.child(id_menu!!).setValue(hashMap)

        progressDialog.dismiss()

        startActivity(Intent(this@TambahMenu, DaftarProduk::class.java))
        finish()
    }

    fun fKembali(view: android.view.View) {
        startActivity(Intent(this@TambahMenu, BerandaPenjual::class.java))
        finish()
    }



}

