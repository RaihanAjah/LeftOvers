package com.example.chicagoxleftovers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import com.example.chicagoxleftovers.databinding.ActivityMetodePembayaranBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_metode_pembayaran.*

class MetodePembayaran : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private var mDBListener: ValueEventListener? = null

    //viewBinding
    private lateinit var binding: ActivityMetodePembayaranBinding

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var mUser:MutableList<User>
    private lateinit var mPemesan:MutableList<Pemesan>
    private lateinit var nama_user:String
    private lateinit var alamat_user:String
    private val dataIntent by lazy {
        intent.getParcelableExtra<Menu>("dataMenu")
    }

    var banyak = 1
    private var pemesanan=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMetodePembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = FirebaseAuth.getInstance().currentUser
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference()

        checkUser()

        val id_pembayaran = database.push().key
        val name = intent.getStringExtra("EXTRA")
        var harga = intent.getStringExtra("EXTRA_harga")
        val tanggal = intent.getStringExtra("EXTRA_tanggal")
        var id_toko  = intent.getStringExtra("EXTRA_id")
        val a = harga?.toInt()



//        var Tharga : Int?
//
//        btnTambah.setOnClickListener {
//            banyak = banyak + 1
////            Tharga = banyak * harga!!.toInt()
//            tvBanyak.setText(banyak.toString())
//
//        }
//        btnKurang.setOnClickListener {
//            banyak = banyak -1
//            tvBanyak.setText(banyak.toString())
//        }
//
//        harga *= banyak


        binding.tvNamaPesan.text = name
        binding.textView8.text= harga
        binding.tvHTotal.text= harga
//        binding.tvHargaProduk.text = harga.toString()
        binding.tvHargaProduk.text = harga
        binding.tvTanggalProd.text = tanggal
        binding.tvBanyak.text = banyak.toString()

        radioG.setOnCheckedChangeListener{ radioG, i ->
            val radio: RadioButton = findViewById(i)
            pemesanan = radio.text.toString()
            if(radio.text == "Ambil Sendiri"){
                antar1.setVisibility(View.GONE);
                antar2.setVisibility(View.GONE);
                antar3.setVisibility(View.GONE);
            }
            else if(radio.text == "Antar Pesanan"){
                antar1.setVisibility(View.VISIBLE);
                antar2.setVisibility(View.VISIBLE);
                antar3.setVisibility(View.VISIBLE);
            }

        }

        binding.pay.setOnClickListener {
            val mDatabaseRef = FirebaseDatabase.getInstance().getReference("transaksi")
            mDatabaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (UserSnapshot in snapshot.children){
                        if (user != null) {
//                            Log.d("refo", "user ada")
                            if(user.uid == UserSnapshot.getValue(User::class.java)!!.id_user){
//                                Log.d("refo", "ada user")
                                if (tanggal != null && harga != null && name != null) {
//                                    Log.d("refo", "gada yg null")
                                    val pembayaran =
                                        a?.let { it1 -> Pembayaran(it1, tanggal, name,id_toko, user.uid,nama_user,alamat_user) }
//                                    binding.pay.setOnClickListener {

                                    if (id_pembayaran != null) {
                                        mDatabaseRef.child(id_pembayaran).setValue(pembayaran)
                                    }

                                            val intent = Intent(it.context, NotifBerhasil::class.java)
                                            intent.putExtra("EXTRA_id",id_toko)
                                            startActivity(intent)

//                                    }
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


        }




//        if (tanggal != null && harga != null && name != null && user != null) {
//            val pembayaran = Pembayaran(harga, tanggal, name, user.id_user!!)
//            binding.pay.setOnClickListener {
//                database.child("transaksi").child(user.id_user!!).setValue(pembayaran)
//            }
//        }
    }

    private fun radioListener() {

    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser


        if(firebaseUser == null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{

            mUser = ArrayList()
            database = FirebaseDatabase.getInstance().getReference("user")


            mDBListener = database!!.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    mUser.clear()
                    for (UserSnapshot in snapshot.children){
                        if(firebaseAuth.currentUser!!.uid == UserSnapshot.getValue(User::class.java)!!.id_user){
                            alamat_user = UserSnapshot.getValue(User::class.java)!!.alamat_pembeli.toString()
                            nama_user = UserSnapshot.getValue(User::class.java)!!.nama_pembeli.toString()
                            binding.tvNamaPembeli.text = nama_user
                            binding.tvAlamatPembeli.text = alamat_user
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
    }

    fun fKeNotifBerhasil(view : View){
        val intKeNotif= Intent(this, NotifBerhasil::class.java)
        startActivity(intKeNotif)
    }

    fun fKembali(view : View){
        val intKembali = Intent(this, BerandaPembeli::class.java)
        startActivity(intKembali)
    }


}

