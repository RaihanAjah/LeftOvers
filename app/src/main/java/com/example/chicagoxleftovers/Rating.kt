package com.example.chicagoxleftovers

import com.google.firebase.database.Exclude

class Rating (
    var id_Rating: String? = null,
    val id_Toko: String,
    val id_user: String,
    val rating: Float,
    val ulasan: String? = null
)