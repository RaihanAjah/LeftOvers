package com.example.chicagoxleftovers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

//class MyAdapter(private val menuList : ArrayList<Menu>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
class MyAdapter( var mContext : Context, var menuList : MutableList<Menu>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    val ref = FirebaseDatabase.getInstance().getReference("menu")

    private lateinit var listenerClickEdit : setOnClickEdit

    private lateinit var listenerClickDelete : setOnClickDelete

    fun clickButtonEdit(v : setOnClickEdit){
        this.listenerClickEdit = v
    }

    fun clickButtonDelete(listener: setOnClickDelete){
        this.listenerClickDelete  = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.menu_item,
        parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = menuList[position]
//        Glide.with(mContext)
//            .load(currentitem.foto_produk!!.toUri())
//            .into(holder.foto_produk)

//        holder.foto_produk.loadImage(currentitem.foto_produk)
        holder.nama_menu.text = currentitem.nama_menu
        holder.harga_diskon.text = currentitem.harga_asli.toString()
        holder.tanggal_produksi.text = currentitem.tanggal_produksi
        holder.editData.setOnClickListener {
            listenerClickEdit.editMenu(menuList[position])
        }
        holder.deleteData.setOnClickListener {
            listenerClickDelete.deleteDataMenu(menuList[position])
            ref.child(menuList[position].id_menu.toString()).removeValue()
            menuDelete(position)
        }

//28.39
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var foto_produk:ImageView
        var nama_menu:TextView
        var harga_diskon:TextView
        var tanggal_produksi:TextView
        var editData:Button
        var deleteData : Button

        init {
            foto_produk = itemView.findViewById(R.id.ivMenu)
            nama_menu  = itemView.findViewById(R.id.tvNamaMenu)
            harga_diskon = itemView.findViewById(R.id.tvHargaAsli)
            tanggal_produksi = itemView.findViewById(R.id.tvTanggal)
            editData = itemView.findViewById(R.id.btnEdit)
            deleteData = itemView.findViewById(R.id.btnDelete)
//            editData.setOnClickListener {
////                when(it)
//            }

        }

    }

    interface setOnClickEdit{
        fun editMenu(v : Menu)
    }

    interface setOnClickDelete {
        fun deleteDataMenu(menu: Menu)
    }

    fun menuDelete(position: Int){
        this.menuList.removeAt(position)



        notifyItemRemoved(position)
//        ref.child(position)
    }
}

