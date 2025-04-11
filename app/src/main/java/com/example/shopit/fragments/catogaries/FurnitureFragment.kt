package com.example.shopit.fragments.catogaries

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.shopit.data.Categary
import com.example.shopit.utils.Resource
import com.example.shopit.viewmodel.CatogaryViewmModel
import com.example.shopit.viewmodel.Factory.BaseCategoryViewModelfactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
@AndroidEntryPoint
class FurnitureFragment:base_catogaryfragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore


    val viewmModel by viewModels<CatogaryViewmModel> {
        BaseCategoryViewModelfactory(firestore,Categary.Furniture)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {



            viewmModel.offerProduct.collectLatest {
                when (it){
                    is Resource.Loading->{
                        shoofferloading()

                    }
                    is Resource.Success->{
                        offerAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error->{
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_SHORT).show()
                        hidefferloading()

                    }

                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {

            viewmModel.bestProducts.collectLatest {
                when (it){
                    is Resource.Loading->{


                    }
                    is Resource.Success->{
                        bestProductAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error->{
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_SHORT).show()


                    }

                    else -> Unit
                }
            }
        }



    }
}
