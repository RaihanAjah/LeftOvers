package com.example.chicagoxleftovers

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Menu(
    var nama_menu: String ? = null,
    var deskripsi: String ? = null,
    var foto_produk: String ? = null,
    var id_menu: String ? = null,
    var id_toko: String? = null,
    var harga_asli: Int ? = null,
    var harga_diskon: Int ? = null,
    var tanggal_produksi: String ? = null):Parcelable
