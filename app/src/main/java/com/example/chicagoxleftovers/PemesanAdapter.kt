package com.example.chicagoxleftovers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PemesanAdapter (var mContext : Context, var rvListPemesan : List<Pemesan>) : RecyclerView.Adapter<PemesanAdapter.MyViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PemesanAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.pesanan_item,
            parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PemesanAdapter.MyViewHolder, position: Int) {
        val currentitem = rvListPemesan[position]

        holder.nama_user.text = currentitem.id_user
        holder.nama_makanan.text = currentitem.namaMak
    }

    override fun getItemCount(): Int {
        return rvListPemesan.size
    }


    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var nama_user: TextView
        var nama_makanan: TextView

        init {
            nama_user  = itemView.findViewById(R.id.tvNamaPemesan)
            nama_makanan = itemView.findViewById(R.id.tvAlamatPemesan)

        }

    }

}
