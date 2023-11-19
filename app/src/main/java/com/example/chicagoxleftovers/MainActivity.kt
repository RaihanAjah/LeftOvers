package com.example.chicagoxleftovers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun fKeNotifBerhasil(view : View){
        val intKeNotif= Intent(this, NotifBerhasil::class.java)
        startActivity(intKeNotif)
    }
    fun fKePengiriman(view : View){
        val intKePengiriman = Intent(this, Pengiriman::class.java)
        startActivity(intKePengiriman)
    }
    fun fKePembayaran(view : View){
        val intKePembayaran = Intent(this, MetodePembayaran::class.java)
        startActivity(intKePembayaran)
    }
    fun fKeToko(view : View){
        val intKeToko = Intent(this, HalamanToko::class.java)
        startActivity(intKeToko)
    }
    fun fKeListMakanan(view : View){
        val intKeListMakanan = Intent(this, ListMakanan::class.java)
        startActivity(intKeListMakanan)
    }
    fun fKeLoginPenjual(view : View){
        val intKeLogin = Intent(this, LoginPenjual::class.java)
        startActivity(intKeLogin)
    }
    fun fKeRegisterPenjual(view : View){
        val intKeRegister = Intent(this, RegisterPenjual::class.java)
        startActivity(intKeRegister)
    }
    fun fKeLoginPembeli(view : View){
        val intKeLogin = Intent(this, LoginPembeli::class.java)
        startActivity(intKeLogin)
    }
    fun fKeRegisterPembeli(view : View){
        val intKeRegister = Intent(this, RegisterPembeli::class.java)
        startActivity(intKeRegister)
    }
}