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

class MenuTokoAdapter(var mContext : Context, var rvListMenuToko : List<Menu>) : RecyclerView.Adapter<MenuTokoAdapter.MyViewHolder>() {

    private lateinit var listenerClickMenu : MenuTokoAdapter.setOnClickMenu

    fun clickButtonMenu(v : MenuTokoAdapter.setOnClickMenu){
        this.listenerClickMenu = v
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuTokoAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_toko_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = rvListMenuToko[position]
        val foto_produk = currentitem.foto_produk

//        holder.foto_produk.loadImage(currentitem.foto_produk)
//        Glide.with(mContext)
//            .load(currentitem.nama_menu!!.toUri())
//            .into(holder.foto_produk)
        holder.nama_menu.text = currentitem.nama_menu
        holder.harga_diskon.text = currentitem.harga_diskon.toString()
        holder.tanggal.text = currentitem.tanggal_produksi
        holder.id_toko.text= currentitem.id_toko

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, MetodePembayaran::class.java)

            intent.putExtra("EXTRA", holder.nama_menu.text)
            intent.putExtra("EXTRA_id", holder.id_toko.text)
            intent.putExtra("EXTRA_harga",holder.harga_diskon.text)
            intent.putExtra("EXTRA_tanggal",holder.tanggal.text)
            it.context.startActivity(intent)

        }
        val storageReference = FirebaseStorage.getInstance().getReference("menu/$foto_produk")

        storageReference.downloadUrl.addOnSuccessListener { dataUri->
            Glide.with(mContext)
                .load(dataUri)
                .into(holder.foto_produk)
        }
            .addOnFailureListener{
                Log.d("Error : ", it.toString())
            }


//        holder.listProduk.setOnClickListener {
//            listenerClickMenu.listProduk(rvListProduk[position])
//        }

    }

    override fun getItemCount(): Int {
        return rvListMenuToko.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var foto_produk: ImageView
        var nama_menu: TextView
        var harga_diskon: TextView
        var deskripsi: TextView
        var tanggal: TextView
        var id_toko : TextView
//        var listProduk : RecyclerView

        init {
            foto_produk = itemView.findViewById(R.id.ivFotoMenuToko)
            nama_menu  = itemView.findViewById(R.id.tvNamaMenuToko)
            harga_diskon = itemView.findViewById(R.id.tvHargaMenuToko)
            deskripsi = itemView.findViewById(R.id.tvDeskripsiMenuToko)
            tanggal = itemView.findViewById(R.id.tvTanggalProduksi)
            id_toko = itemView.findViewById(R.id.tvIdTokoMenu)
        //            listProduk = itemView.findViewById(R.id.rvListProduk)
        }

    }
    interface setOnClickMenu{
        fun listProduk(v : Menu)
    }
}