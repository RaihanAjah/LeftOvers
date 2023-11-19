package com.example.chicagoxleftovers

import com.google.firebase.database.Exclude

class Pemesan (
    var id_user: String? = null,
    var id_toko: String ? = null,
    var harga: Int ? = 0,
    var namaMak: String ? = null,
    @get:Exclude
    @set:Exclude
    var tanggalProd: String ? = null,
)