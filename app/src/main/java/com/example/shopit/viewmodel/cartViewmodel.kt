package com.example.shopit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.data.CartProduct
import com.example.shopit.firebase.firebasecommon
import com.example.shopit.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class cartViewmodel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebasecommon: firebasecommon
) : ViewModel() {

    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Unspecified())
    val cartProducts = _cartProducts.asStateFlow()

    val productlprice = cartProducts.map {
        when (it) {
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }

            else -> null
        }
    }

    private var cartproductdocument = emptyList<DocumentSnapshot>()

    fun deleteitemfromcart(cartProduct: CartProduct) {
        val index = cartProducts.value.data?.indexOfFirst { it.product.id == cartProduct.product.id }
        if (index != null && index != -1) {
            val documentId = cartproductdocument[index].id
            firestore.collection("user")
                .document(firebaseAuth.uid!!)
                .collection("cart")
                .document(documentId)
                .delete()
        }
    }

    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantiy).toDouble()
        }.toFloat()
    }

    init {
        getcartproducts()
    }

    private fun getcartproducts() {
        viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }

        firestore.collection("user")
            .document(firebaseAuth.uid!!)
            .collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    viewModelScope.launch {
                        _cartProducts.emit(Resource.Error(error.message ?: "Unknown error"))
                    }
                } else {
                    value?.let {
                        val cartProduct = it.toObjects(CartProduct::class.java)
                        cartproductdocument = it.documents
                        viewModelScope.launch {
                            _cartProducts.emit(Resource.Success(cartProduct))
                        }
                    }
                }
            }
    }

    fun Float?.getProductPrice(originalPrice: Float): Float {
        return if (this != null) {
            originalPrice - (originalPrice * this / 100f)
        } else {
            originalPrice
        }
    }
}
