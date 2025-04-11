package com.example.shopit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.data.Product
import com.example.shopit.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainCatogaryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) :ViewModel() {
    private val _specialPoroducts=MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts:StateFlow<Resource<List<Product>>> = _specialPoroducts


    private val _bestdeealsproducts=MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestdealProduct:StateFlow<Resource<List<Product>>> = _bestdeealsproducts



    private val _bestProduct=MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val bestproduct:StateFlow<Resource<List<Product>>> = _bestProduct
    init {
        fetchspecialProducts()
        fetchBestproduct()
        fetchBestdeals()


    }
    fun fetchspecialProducts(){
        viewModelScope.launch {
            _specialPoroducts.emit(Resource.Loading())
        }

        firestore.collection("products").whereEqualTo("category","Special Products").get()
            .addOnSuccessListener {result->
                viewModelScope.launch {
                    val specialProductlist=result.toObjects(Product::class.java)
                    viewModelScope.launch {
                        _specialPoroducts.emit(Resource.Success(specialProductlist))

                    }
                }

        }.addOnFailureListener {
            viewModelScope.launch { _specialPoroducts.emit(Resource.Error(it.message.toString())) }



        }

        }
    fun fetchBestdeals() {
        viewModelScope.launch { _bestdeealsproducts.emit(Resource.Loading()) }

        firestore.collection("products").whereEqualTo("category", "Best Deals").get()


            .addOnSuccessListener { result ->
                val bestdeals = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestdeealsproducts.emit(Resource.Success(bestdeals))
                }


            }


            .addOnFailureListener {
                viewModelScope.launch {
                    _bestdeealsproducts.emit(Resource.Error(it.message.toString()))

                }



            }


    }
    fun fetchBestproduct(){
        viewModelScope.launch {
            _bestProduct.emit(Resource.Loading())
        }
        firestore.collection("products").get()

            .addOnSuccessListener {result->
                val bestproductlist=result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProduct.emit(Resource.Success(bestproductlist))
                }
            }


            .addOnFailureListener{
                viewModelScope.launch {
                    _bestdeealsproducts.emit(Resource.Error(it.message.toString()))

                }
            }

    }


    }

