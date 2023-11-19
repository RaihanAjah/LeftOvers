package com.example.chicagoxleftovers

import android.widget.Filter

class FilterStore: Filter {

    private var filterList: ArrayList<modelStore>

    //adapter
    private var adapterCategory: AdapterStoreList

    //constructor
    constructor(filterList: ArrayList<modelStore>, adapterCategory: AdapterStoreList) {
        this.filterList = filterList
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        //value shouldn't be null
        if(constraint != null && constraint.isNotEmpty()){

            //change to uppercase
            constraint = constraint.toString().uppercase()
            val filteredModel:ArrayList<modelStore> = ArrayList()
            for(i in 0 until filterList.size){
                //validate

                if(filterList[i].nama_toko.uppercase().contains(constraint)){
                    //add to filtered list
                    filteredModel.add(filterList[i])
                }
            }
            results.count = filteredModel.size
            results.values = filteredModel

        }
        else{
            //search value is either null or empty
            results.count = filterList.size
            results.values = filterList

        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        //apply filter
        adapterCategory.storeArrayList = results.values as ArrayList<modelStore>

        adapterCategory.notifyDataSetChanged()
    }


}
