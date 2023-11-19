package com.example.chicagoxleftovers

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.example.chicagoxleftovers.databinding.ActivityEditMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_tambah_menu.*
import java.util.*

class EditMenu : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val dataIntent by lazy {
        intent.getParcelableExtra<Menu>("dataMenu")
    }

    private var mImageUri: Uri? = null
    private var mStorageRef: StorageReference? = null
    private var mDatabaseRef: DatabaseReference? = null
    private val PICK_IMAGE_REQUEST = 100

    var tanggal = 0
    var bulan = 0
    var tahun = 0
    lateinit var teksTanggal: TextView

    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityEditMenuBinding

    private var namaMenu = ""
    private var hargaAsli = 0
    private var hargaDiskon = 0
    private var tanggalProduksi = ""
    private var fotoProduk = ""
    private var deskripsi = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mStorageRef = FirebaseStorage.getInstance().getReference("menu")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("menu")

        Log.d("Data Intent", dataIntent.toString())

        binding.etNamaMenu.setText(dataIntent!!.nama_menu)
        binding.etHargaAsli.setText(dataIntent!!.harga_asli.toString())
        binding.etHargaDiskon.setText(dataIntent!!.harga_diskon.toString())
        binding.etTanggal.setText(dataIntent!!.tanggal_produksi)
        binding.etDeskripsi.setText(dataIntent!!.deskripsi)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
    }

    fun fKembali(view: android.view.View) {
        startActivity(Intent(this@EditMenu, BerandaPenjual::class.java))
        finish()
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
        teksTanggal = findViewById(R.id.etTanggal)
        teksTanggal.text = "${tanggal} - ${bulan} - ${tahun}"
    }

    fun pilihGambar(view: android.view.View) {
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
        tanggalProduksi = binding.etTanggal.text.toString().trim()
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
//        val database = Firebase.database
//        val ref      = database.getReference("menu")
        val ref = FirebaseDatabase.getInstance().getReference("menu")

        //get id user
        val id_toko = FirebaseAuth.getInstance().currentUser?.uid

        //get current user uid
        val id_menu = dataIntent!!.id_menu

        //setup data to add db
        val hashMap: HashMap<String, Any?> = HashMap()
        hashMap["nama_menu"] = namaMenu
        hashMap["harga_asli"] = hargaAsli
        hashMap["harga_diskon"] = hargaDiskon
        hashMap["tanggal_produksi"] = tanggalProduksi
        hashMap["deskripsi"] = deskripsi

        //set data to db
//        ref.child(id_menu!!).setValue(hashMap)
        ref.child(id_menu!!).updateChildren(hashMap)


        startActivity(Intent(this@EditMenu, DaftarProduk::class.java))
        finish()
    }


}