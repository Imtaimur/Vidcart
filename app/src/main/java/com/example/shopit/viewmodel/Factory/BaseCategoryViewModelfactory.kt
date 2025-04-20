package com.example.shopit.viewmodel.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopit.data.Categary
import com.example.shopit.viewmodel.CatogaryViewmModel
import com.google.firebase.firestore.FirebaseFirestore

class BaseCategoryViewModelfactory(
    private val firestore: FirebaseFirestore,
    private val categary: Categary

) :ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CatogaryViewmModel(firestore,categary) as T
    }

}