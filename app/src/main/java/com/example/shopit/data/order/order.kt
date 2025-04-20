package com.example.shopit.data.order

import com.example.shopit.data.Address
import com.example.shopit.data.CartProduct
import java.text.SimpleDateFormat
import kotlin.random.Random.Default.nextLong
import java.util.Date
import java.util.Locale

data class order(
    val orderStatus: String = "",
    val totalPrice: Float = 0f,
    val prouducts: List<CartProduct> = emptyList(),  // Provide default empty list
    val address: Address = Address(),
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
    val orderId: Long = nextLong(0, 100000000) + totalPrice.toLong()
) {

    constructor() : this(
        orderStatus = "",
        totalPrice = 0f,
        prouducts = emptyList(),
        address = Address(),
        date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()),
        orderId = nextLong(0, 100000000) + 0L
    )
}
