package com.example.chicagoxleftovers

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chicagoxleftovers.databinding.CardStoreBinding
import com.google.firebase.storage.FirebaseStorage

class AdapterStoreList :RecyclerView.Adapter<AdapterStoreList.HolderStore>,Filterable {


    private val context:Context
    public var storeArrayList: ArrayList<modelStore>
    private lateinit var binding: CardStoreBinding
    private var filterList: ArrayList<modelStore>

    private var filter: FilterStore? = null

    //constructor
    constructor(context: Context, storeArrayList: ArrayList<modelStore>) {
        this.context = context
        this.storeArrayList = storeArrayList
        this.filterList = storeArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderStore {
        //inflate binding card_store.xml
        binding = CardStoreBinding.inflate(LayoutInflater.from(context), parent, false)

        return HolderStore(binding.root)
    }

    override fun onBindViewHolder(holder: HolderStore, position: Int) {
        //Get data, Set data, Handle Clicks etc

        //get data
        val model = storeArrayList[position]
        val id_user = model.id_user
        val nama_toko = model.nama_toko
        val alamat_toko = model.alamat_toko
        val no_Tlp = model.no_Tlp
        val foto_toko = model.foto_toko
        val username = model.username
        val email = model.email
        val password = model.password
        val rating = model.rating
        val timestamp = model.timestamp

        //set data
        val storageReference = FirebaseStorage.getInstance().getReference("Toko/$foto_toko")

        storageReference.downloadUrl.addOnSuccessListener { dataUri->
            Glide.with(context)
                .load(dataUri)
                .into(holder.ivProfileShop)
        }
            .addOnFailureListener{
                Log.d("Error : ", it.toString())
            }
//        holder.ivProfileShop.loadImage(foto_toko)
        holder.tvShopName.text = nama_toko
        holder.tvShopAddress.text = alamat_toko
        holder.tvRate.text = rating.toString()
        //    holder.tvDistance.text = nama_toko

        //handle clicks
        holder.tvShopName.setOnClickListener{

        }




    }

    override fun getItemCount(): Int {
        return storeArrayList.size

    }

    inner class HolderStore(itemView: View): RecyclerView.ViewHolder(itemView){
        //init UI Views
        var ivProfileShop:ImageView = binding.ivProfileShop
        var tvShopName:TextView = binding.tvShopName
        var tvShopAddress:TextView = binding.tvShopAddress
        var tvRate:TextView = binding.tvRate
        //   var tvDistance:TextView = binding.tvDistance

    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilterStore(filterList, this)
        }
        return filter as FilterStore
    }


}
