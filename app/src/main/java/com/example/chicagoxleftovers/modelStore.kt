package com.example.chicagoxleftovers

import android.media.Image

class modelStore {

    //Variabel
    var id_user:String = "";
    var nama_toko:String = "";
    var alamat_toko:String = "";
    var no_Tlp:String = "";
    var foto_toko:String = "";
    var username:String = "";
    var email:String = "";
    var password:String = "";
    var rating: Int = 0;
    var timestamp:Long = 0;

    //constructor

    constructor()
    constructor(
        id_user: String,
        nama_toko: String,
        alamat_toko: String,
        no_Tlp: String,
        foto_toko: String,
        username: String,
        email: String,
        password: String,
        rating: Int,
        timestamp: Long
    ) {
        this.id_user = id_user
        this.nama_toko = nama_toko
        this.alamat_toko = alamat_toko
        this.no_Tlp = no_Tlp
        this.foto_toko = foto_toko
        this.username = username
        this.email = email
        this.password = password
        this.rating = rating
        this.timestamp = timestamp
    }




}
