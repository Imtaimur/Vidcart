package com.example.shopit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.data.Product
import com.example.shopit.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainCatogaryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) :ViewModel() {
    private val _specialPoroducts=MutableStateFlow<Resource<List<Product>>>(Resource.Unspecified())
    val specialProducts:StateFlow<Resource<List<Product>>> = _specialPoroducts
    init {
        fetchspecialProducts()
    }
    fun fetchspecialProducts(){
        viewModelScope.launch {
            _specialPoroducts.emit(Resource.Loading())
        }

        firestore.collection("products").whereEqualTo("category","Special product").get()
            .addOnSuccessListener {result->
                viewModelScope.launch {
                    val specialProductlist=result.toObjects(Product::class.java)
                }

        }.addOnFailureListener {
            viewModelScope.launch { _specialPoroducts.emit(Resource.Error(it.message.toString())) }



        }


    }

}