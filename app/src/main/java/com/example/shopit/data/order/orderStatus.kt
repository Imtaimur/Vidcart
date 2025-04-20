package com.example.shopit.data.order

sealed class  orderStatus(val status:String){
    object Ordered:orderStatus("Ordered")
    object Canceled:orderStatus("Canceld")
    object Confirmed:orderStatus("Confirmed")
    object Delivered:orderStatus("Delivered")
    object Returned:orderStatus("Returned")
}
