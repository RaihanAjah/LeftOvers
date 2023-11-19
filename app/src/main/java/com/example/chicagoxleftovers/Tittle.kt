package com.example.chicagoxleftovers

import com.google.firebase.database.Exclude

class Tittle {
    var id_tittle: String? = null
    var id_user: String?  = null
    var status: String? = null
    @get:Exclude
    @set:Exclude
    var poin:  Int? =  0
}