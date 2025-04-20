package com.example.shopit.firebase

import com.example.shopit.data.CartProduct
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class firebasecommon (
    private val firebasefirestore:FirebaseFirestore,
    private val auth: FirebaseAuth,
//    private val firebasecommon: firebasecommon
){
    private val cartCollection=firebasefirestore.collection("user").document(auth.uid!!).collection(  "cart")
    fun addproducttocart(cartProduct: CartProduct,onResult:(CartProduct?,Exception?)->Unit){
        cartCollection.document().set(cartProduct)


            .addOnSuccessListener {

                onResult(cartProduct,null)
            }



            .addOnFailureListener {
                onResult(null ,it)
            }

    }




}