package com.example.chicagoxleftovers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.media.Image
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "LeftOvers.db", null, 1) {

//    companion object{
//        internal val dbname = "LeftOvers"
//        internal val factory = "null"
//        internal val version = "1"
//
//    }
    override fun onCreate(db: SQLiteDatabase?) {
//        db?.execSQL("CREATE TABLE session(id integer PRIMARY KEY, login text)")
//        db?.execSQL("CREATE TABLE toko(id_toko integer PRIMARY KEY AUTOINCREMENT," +
//                "nama varchar(30), alamat varchar(50), nomor varchar(30), foto image, username varchar(30), password varchar(30), rating double)")
//        db?.execSQL("CREATE TABLE menu(id_menu integer PRIMARY KEY AUTOINCREMENT," +
//            "nama_menu varchar(30), harga_asli INT, harga_diskon INT, tanggal_produksi date, foto_produk image, deskripsi varchar(100), id_toko INT)")
//        db?.execSQL("CREATE TABLE menu(id_menu integer PRIMARY KEY AUTOINCREMENT," +
//            "nama_menu varchar(30), harga_asli INT, harga_diskon INT, foto_produk image, deskripsi varchar(100), id_toko INT)")
    //        db?.execSQL("INSERT INTO session(id, login) VALUES (1,'kosong')")

        var tabelToko = "CREATE TABLE menu(id_menu integer PRIMARY KEY AUTOINCREMENT," +
            "nama_menu varchar(30), harga_asli INT, harga_diskon INT, tanggal_produksi date, foto_produk image, deskripsi varchar(100), id_toko INT)"
        var tabelMenu = "CREATE TABLE toko(id_toko integer PRIMARY KEY AUTOINCREMENT," +
                "nama varchar(30), alamat varchar(50), nomor varchar(30), foto image, username varchar(30), password varchar(30), rating double)"

        db?.execSQL(tabelToko)
        db?.execSQL(tabelMenu)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS session")
        db?.execSQL("DROP TABLE IF EXISTS toko")
        db?.execSQL("DROP TABLE IF EXISTS menu")
        onCreate(db)
    }

    fun insertUserData(nama :String, alamat : String, nomor: String, username: String, password: String){
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()
        values.put("nama", nama)
        values.put("alamat", alamat)
        values.put("nomor", nomor)
        values.put("username", username)
        values.put("password", password)

        db.insert("toko", null, values)
        db.close()
    }

    fun insertMenuData(nama_menu :String, harga_asli : Int, harga_diskon: Int, deskripsi : String){
        val db: SQLiteDatabase = writableDatabase
        val values: ContentValues = ContentValues()
        values.put("nama_menu", nama_menu)
        values.put("harga_asli", harga_asli)
        values.put("harga_diskon", harga_diskon)
//        values.put("foto_produk", foto_produk)
        values.put("deskripsi", deskripsi)

        db.insert("menu", null, values)
        db.close()
    }

    fun userPresent(username: String, password: String): Boolean{
        val db = writableDatabase
        val query = "SELECT * FROM toko WHERE username = '$username' AND password = '$password'"
        val cursor = db.rawQuery(query,null)
        if(cursor.count <= 0){
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

//    //check session
//    fun checkSession(sessionValues: String): Boolean {
//        val db: SQLiteDatabase = this.readableDatabase
//        val cursor: Cursor = db.rawQuery("SELECT * FROM session WHERE login = ?", Array<String?>(99){sessionValues})
//        if(cursor.getCount()>0){
//            return true
//        }
//        else{
//            return false
//        }
//    }
//
//    //upgrade session
//    fun upgradeSession(sessionValues: String, id: Int): Boolean {
//        val db: SQLiteDatabase = this.writableDatabase
//        val contenValues: ContentValues =  ContentValues()
//        val update : Int = db.update("session", contenValues,"id= "+id,null)
//        if(update == -1){
//            return false
//        }
//        else{
//            return true
//        }
//    }

}


