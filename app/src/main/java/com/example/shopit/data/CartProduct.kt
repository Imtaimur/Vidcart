package com.example.shopit.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartProduct(
    val product: Product=Product(),
    val price:Int,
    val quantiy:Int,
    var isSelected:Boolean=false
):Parcelable{
    constructor():this(Product(),1,1,false)
}
