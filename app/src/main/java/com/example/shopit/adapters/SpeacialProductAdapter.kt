package com.example.shopit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopit.data.Product
import com.example.shopit.databinding.FragmentHomeBinding
import com.example.shopit.databinding.SpecialRvItemBinding

class SpeacialProductAdapter:RecyclerView.Adapter<SpeacialProductAdapter.SpecialProductsViewHolder>() {
    inner class SpecialProductsViewHolder(private val binding: SpecialRvItemBinding):RecyclerView.ViewHolder(binding.root){
    fun bind(product: Product){
        binding.apply {
            Glide.with(itemView).load(product.images[0]).into(SpecialProductimg)
            tvspecialproductname.text=product.name
            tvspecialproductprice.text=product.price.toString()



        }

    } }



    val diffcalback=object :DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }



    }
    val difer=AsyncListDiffer(this,diffcalback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
                )
        )
    }

    override fun getItemCount(): Int {
        return difer.currentList.size

    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val product=difer.currentList[position]
        holder.bind(product)
    }


}