package com.example.shopit.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopit.R
import com.example.shopit.data.CartProduct
import com.example.shopit.databinding.CartProductItemBinding

class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {

    inner class CartProductViewHolder(val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                // Load the product image
                Glide.with(itemView).load(cartProduct.product.images[0])
                    .into(imagecartproduct)
                productName.text = cartProduct.product.name
                tvproductcartprice.text = cartProduct.product.price.toString()

                // Change background based on selection
                val backgroundColor = if (cartProduct.isSelected) {
                    itemView.context.getColor(R.color.g_blue100) // Selected
                } else {
                    itemView.context.getColor(R.color.white) // Default
                }
                cartiemcontainer.setBackgroundColor(backgroundColor)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    val difer = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        return CartProductViewHolder(
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = difer.currentList.size

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartProduct = difer.currentList[position]
        holder.bind(cartProduct)

        // Log selection state
        Log.d("CartAdapter", "Item at $position selected: ${cartProduct.isSelected}")

        holder.itemView.setOnClickListener {
            val currentList = difer.currentList.toMutableList()
            val currentItem = currentList[position]
            val updatedItem = currentItem.copy(isSelected = !currentItem.isSelected)

            // Update the list with toggled item
            currentList[position] = updatedItem
            difer.submitList(currentList)

            // Log new selection
            Log.d("CartAdapter", "Toggled selection at $position: ${updatedItem.isSelected}")
        }



        holder.binding.imageminus.setOnClickListener {
            Onminusclick?.invoke(cartProduct)
        }

        holder.itemView.setOnLongClickListener {
            OnProductclick?.invoke(cartProduct)
            true
        }
    }

    var OnProductclick: ((CartProduct) -> Unit)? = null
    var Onplusclick: ((CartProduct) -> Unit)? = null
    var Onminusclick: ((CartProduct) -> Unit)? = null

    // Returns selected items by filtering the list
    fun getSelectedItems(): List<CartProduct> {
        return difer.currentList.filter { it.isSelected }
    }
}
