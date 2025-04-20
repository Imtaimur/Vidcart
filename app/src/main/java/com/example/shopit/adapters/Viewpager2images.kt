package com.example.shopit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.shopit.databinding.ViewpagerImageItemBinding

class Viewpager2images: RecyclerView.Adapter<Viewpager2images.Viewpager2imagesViewHolder>() {
    inner class Viewpager2imagesViewHolder(val binding:ViewpagerImageItemBinding):ViewHolder(binding.root){
        fun bind(imagePath:String){
            Glide.with(itemView).load(imagePath).into(binding.imageproductdetail)



        }
    }

    private val diffCallback=object :DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return  oldItem==newItem
        }
    }
    val differ=AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewpager2imagesViewHolder {
        return Viewpager2imagesViewHolder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: Viewpager2imagesViewHolder, position: Int) {
      val image=differ.currentList[position]
        holder.bind(image)
    }
}