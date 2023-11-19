package com.example.chicagoxleftovers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RatingAdapter ( var mContext : Context, var ratingList : List<Rating>) : RecyclerView.Adapter<RatingAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ulasan_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: RatingAdapter.MyViewHolder, position: Int) {
        val currentitem = ratingList[position]

//        holder.namaPerating.text = currentitem.namaPerating.toString().trim()
        holder.rating.rating = currentitem.rating
        holder.ulasan.text = currentitem.ulasan.toString()

    }

    override fun getItemCount(): Int {
        return ratingList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

//        var namaPerating = itemView.findViewById<TextView>(R.id.tvNamaPerating)
        var rating  = itemView.findViewById<RatingBar>(R.id.rbRating)
        var ulasan = itemView.findViewById<TextView>(R.id.tvUlasan)


    }



}