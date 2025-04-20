package com.example.shopit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.data.order.order
import com.example.shopit.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _allOrders = MutableStateFlow<Resource<List<order>>>(Resource.Unspecified())
    val allOrders = _allOrders.asStateFlow()

    fun getAllOrders() {
        val userId = firebaseAuth.uid
        if (userId != null) {

            viewModelScope.launch {
                _allOrders.emit(Resource.Loading())
            }


            firestore.collection("orders")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { snapshot ->
                    val orders = snapshot.toObjects(order::class.java)
                    if (orders.isNotEmpty()) {

                        viewModelScope.launch {
                            _allOrders.emit(Resource.Success(orders))
                        }
                    } else {

                        viewModelScope.launch {
                            _allOrders.emit(Resource.Error("No orders found"))
                        }
                    }
                }
                .addOnFailureListener { exception ->

                    viewModelScope.launch {
                        _allOrders.emit(Resource.Error(exception.message.toString()))
                    }
                }
        } else {

            viewModelScope.launch {
                _allOrders.emit(Resource.Error("User not authenticated"))
            }
        }
    }
}
