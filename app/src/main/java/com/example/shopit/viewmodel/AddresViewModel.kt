package com.example.shopit.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AddresViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth

):ViewModel() {
    private val _addNewAddress= MutableStateFlow<Resource<com.example.shopit.data.Address>>(Resource.Unspecified())
    val addNewAddres=_addNewAddress.asStateFlow()
    private val _error= MutableSharedFlow<String>()
    val error=_error.asSharedFlow()
    fun addAddress(address:com.example.shopit.data.Address){
        val validateinputs=validinputs(address)
        if (validateinputs){
            viewModelScope.launch { _addNewAddress.emit(Resource.Loading()) }

        firestore.collection("user").document(auth.uid!!).collection("address").document().set(address)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _addNewAddress.emit(Resource.Success(address))
                }


            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _addNewAddress.emit(Resource.Error(it.message.toString()))
                }
            }

    }
    else{

        viewModelScope.launch {
            _error.emit("Fill the Required Information")
        }
    }
    }

    private fun validinputs(address: com.example.shopit.data.Address): Boolean {
        return address.addresTitle.trim().isNotEmpty()
                &&address.city.trim().isNotEmpty()
                &&address.name.trim().isNotEmpty()
                &&address.phone.trim().isNotEmpty()

    }
}