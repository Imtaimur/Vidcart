package com.example.shopit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.data.Categary
import com.example.shopit.data.Product
import com.example.shopit.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CatogaryViewmModel(


    private val firestore:FirebaseFirestore,
    private val categary: Categary

):ViewModel() {
    private val _offerProducts= MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val offerProduct=_offerProducts.asStateFlow()

    private val _bestProducts= MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestProducts=_bestProducts.asStateFlow()
    init {
        fetchofferProducts()
        fetchBestProducts()
    }

    fun fetchofferProducts(){
        viewModelScope.launch { _offerProducts.emit(Resource.Loading()) }
        firestore.collection("products").whereEqualTo("category",categary.Categary )
            .whereNotEqualTo("offerPercentage",null).get().addOnSuccessListener {
                val products= it.toObjects(Product::class.java)
                viewModelScope.launch { _offerProducts.emit(Resource.Success(products)) }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(Resource.Error(it.message.toString()))
                }
            }

    }
    fun fetchBestProducts(){
        viewModelScope.launch { _bestProducts.emit(Resource.Loading()) }
        firestore.collection("products").whereEqualTo("category",categary.Categary)
            .whereEqualTo("offerPercentage",null).get().addOnSuccessListener {
                val products= it.toObjects(Product::class.java)
                viewModelScope.launch { _bestProducts.emit(Resource.Success(products)) }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }

    }



}