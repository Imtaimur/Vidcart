package com.example.shopit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shopit.data.order.order
import com.example.shopit.databinding.OrderItemBinding

class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(order: order) {
            binding.apply {
                tvOrderId.text = "Order ID: ${order.orderId}"
                tvOrderDate.text = "Date: ${order.date}"
                tvOrderStatus.text = "Status: ${order.orderStatus}"
                tvTotalAmount.text = "Total: $${order.totalPrice}"

                // Address Info
                tvAddress.text = """
                    Name: ${order.address.name}
                    Street: ${order.address.street}
                    City: ${order.address.city}
                    Phone: ${order.address.phone}
                """.trimIndent()

                val firstProduct = order.prouducts.firstOrNull()
                firstProduct?.let {
                    tvProductName.text = it.product.name
                    tvProductQuantity.text = "Qty: ${it.quantiy}"
                    tvProductPrice.text = "Price: $${it.product.price}"

                    Glide.with(itemView)
                        .load(it.product.images.firstOrNull())
                        .into(ivProductImage)
                }

                root.setOnClickListener {
                    onClick?.invoke(order)
                }
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<order>() {
        override fun areItemsTheSame(oldItem: order, newItem: order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: order, newItem: order): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(
            OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.onBind(differ.currentList[position])
    }

    var onClick: ((order) -> Unit)? = null
}
