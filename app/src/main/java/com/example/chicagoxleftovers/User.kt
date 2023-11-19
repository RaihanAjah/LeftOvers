package com.example.chicagoxleftovers

import com.google.firebase.database.Exclude

class User (
    var nama_pembeli: String ? = null,
    var alamat_pembeli: String ? = null,
    var email: String ? = null,
    @get:Exclude
    @set:Exclude
    var foto_user: String ? = null,
    var id_user: String ? = null,
    var poin: Int ? = 0
)