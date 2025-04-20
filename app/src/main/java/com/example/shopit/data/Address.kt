package com.example.shopit.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val addresTitle:String,
    val name:String,
    val city:String,
    val phone:String,
    val street:String
):Parcelable{
    constructor():this("","","","","")
}
