package com.example.shopit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.data.CartProduct
import com.example.shopit.firebase.firebasecommon
import com.example.shopit.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DetailviewModel @Inject constructor(
    private val firestore:FirebaseFirestore,
    private val auth:FirebaseAuth,
    private val firebasecommon: firebasecommon
)

    :ViewModel(){
    private  val _addtocart=MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val addtocart=_addtocart.asStateFlow()
fun addupdateproductinCart(cartProduct: CartProduct){
    viewModelScope.launch { _addtocart.emit(Resource.Unspecified()) }
    firestore.collection("user").document(auth.uid!!).collection("cart").whereEqualTo("product.id",cartProduct.product).get()
        .addOnSuccessListener {
            it.documents.let {
                if (it.isEmpty()){
                    addnewProduct(cartProduct)

                }
                else{
                    val product=it.first().toObject(CartProduct::class.java)
                }
            }

        }.addOnFailureListener {
            viewModelScope.launch { _addtocart.emit(Resource.Error(it.message.toString())) }

        }
}
    private fun addnewProduct(cartProduct: CartProduct){
        firebasecommon.addproducttocart(cartProduct){addedproduct,e->
            viewModelScope.launch { if (e==null )
            _addtocart.emit(Resource.Success(addedproduct!!))
                else
                    _addtocart.emit(Resource.Error(e.message.toString()))
            }

        }
    }
}
