package com.example.shopit.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.R
import com.example.shopit.fragments.Account_option_fragment
import com.example.shopit.utils.Constants.INTODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class introductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseauth: FirebaseAuth



):ViewModel(){
    private val _navigate= MutableStateFlow(0)
    val navigate:StateFlow<Int> = _navigate
    companion object{
        const val SHOPPING_ACTIVITY=23
        val Account_option_fragment= R.id.action_introduction_Fragment_to_account_option_fragment
    }

    init {
        val isButtonClicked=sharedPreferences.getBoolean(INTODUCTION_KEY,false)
        val user=firebaseauth.currentUser
        if (user!=null){
            viewModelScope.launch { _navigate.emit(SHOPPING_ACTIVITY) }



        }
        else if (isButtonClicked){
            viewModelScope.launch { _navigate.emit(Account_option_fragment)}}

                else{
            Unit



        }
    }
    fun StartButtonClick(){
        sharedPreferences.edit().putBoolean(INTODUCTION_KEY,true).apply()
    }
}