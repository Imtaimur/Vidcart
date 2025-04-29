package com.example.shopit.data

data class VideosModel(
    val videoUrl: String = "",
    val productId: String = "",
    val productName: String = "",
    val price: Int = 0,
    val videos: List<String> = listOf()
)
