package com.example.shopit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.data.order.order
import com.example.shopit.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderViewModel @Inject constructor(
    private  val firestore: FirebaseFirestore,
  private val firebaseAuth: FirebaseAuth
) :ViewModel(){
    private val _order= MutableStateFlow<Resource<order>>(Resource.Unspecified())
     val order= _order.asStateFlow()
    fun placeorder(order: order){
        viewModelScope.launch {
            _order.emit(Resource.Loading())
        }
        firestore.runBatch { batch->
            firestore.collection("user").document(firebaseAuth.uid!!).collection("orders").document().set(order)
            firestore.collection("ordrers").document().set(order)
            firestore.collection("user").document(firebaseAuth.uid!!).collection("cart").get().addOnSuccessListener {
                it.documents.forEach{
                    it.reference.delete()
                }
            }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _order.emit(Resource.Success(order))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _order.emit(Resource.Error(it.message.toString()))
            }
        }


    }
}