package com.example.notes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.databinding.ItemRvBinding
import com.example.notes.models.GetDetails

class DetailsAdapter(val rvAction: RvAction, val list: ArrayList<GetDetails>):RecyclerView.Adapter<DetailsAdapter.Vh>()  {
    inner class Vh(val itemRvBinding: ItemRvBinding): RecyclerView.ViewHolder(itemRvBinding.root){
        fun onBind(getDetails: GetDetails, position: Int){
            itemRvBinding.sarlavha.text = getDetails.sarlavha
            itemRvBinding.batafsil.text = getDetails.batafsil
            itemRvBinding.date.text = getDetails.muddat
            itemRvBinding.btnMore.setOnClickListener {
                rvAction.moreClick(getDetails, position, itemRvBinding.btnMore)
            }
            itemRvBinding.root.setOnClickListener {
                rvAction.itemClick(getDetails)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
       return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun getItemCount(): Int =list.size


    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    interface RvAction{
        fun moreClick(getDetails: GetDetails, position: Int, imageView: ImageView)
        fun itemClick(getDetails: GetDetails)
    }
}