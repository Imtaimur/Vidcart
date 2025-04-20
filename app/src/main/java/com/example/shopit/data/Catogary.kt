package com.example.shopit.data

sealed class Categary(val Categary:String) {
    object Chair:Categary("Chair")
    object Table:Categary("Table")
    object Cuboard:Categary("Cuboard")
    object Furniture:Categary("Furniture")


}