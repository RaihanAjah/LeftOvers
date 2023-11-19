package com.example.chicagoxleftovers

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MenuAdapter(var mContext : Context, var rvListProduk : List<Menu>) : RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {

    private lateinit var listenerClickMenu : MenuAdapter.setOnClickMenu

    fun clickButtonMenu(v : MenuAdapter.setOnClickMenu){
        this.listenerClickMenu = v
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_produk,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = rvListProduk[position]
        val foto_produk = currentitem.foto_produk
        val storageReference = FirebaseStorage.getInstance().getReference("menu/$foto_produk")
        storageReference.downloadUrl.addOnSuccessListener { dataUri->
            Glide.with(mContext)
                .load(dataUri)
                .into(holder.foto_produk)
        }
            .addOnFailureListener{
                Log.d("Error : ", it.toString())
            }
//        holder.foto_produk.loadImage(currentitem.foto_produk)
//        Glide.with(mContext)
//            .load(currentitem.nama_menu!!.toUri())
//            .into(holder.foto_produk)
        holder.nama_menu.text = currentitem.nama_menu
        holder.harga_diskon.text = currentitem.harga_diskon.toString()
        holder.tanggal_produksi.text = currentitem.tanggal_produksi
        holder.id_toko.text= currentitem.id_toko

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MetodePembayaran::class.java)

            intent.putExtra("EXTRA", holder.nama_menu.text)
            intent.putExtra("EXTRA_harga",holder.harga_diskon.text)
            intent.putExtra("EXTRA_tanggal",holder.tanggal_produksi.text)
            intent.putExtra("EXTRA_id",holder.id_toko.text)

            it.context.startActivity(intent)

        }
//        Glide.with(mContext)
//            .load(currentitem.foto_produk)


//        holder.listProduk.setOnClickListener {
//            listenerClickMenu.listProduk(rvListProduk[position])
//        }

    }

    override fun getItemCount(): Int {
        return rvListProduk.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var foto_produk: ImageView
        var nama_menu: TextView
        var harga_diskon: TextView
        var tanggal_produksi: TextView
        var id_toko : TextView
//        var alamat: TextView
//        var listProduk : RecyclerView

        init {
            foto_produk = itemView.findViewById(R.id.ivFotoProduk)
            nama_menu  = itemView.findViewById(R.id.tvNamaProduk)
            harga_diskon = itemView.findViewById(R.id.tvHargaAsli)
            tanggal_produksi = itemView.findViewById(R.id.tvTanggal)
            id_toko = itemView.findViewById(R.id.tvIdToko)
//            alamat = itemView.findViewById(R.id.tvAlamatPembeli)
//            listProduk = itemView.findViewById(R.id.rvListProduk)
        }

    }
    interface setOnClickMenu{
        fun listProduk(v : Menu)
    }
}