package com.example.shopit.utils

sealed class RegisterValidation(){
    object Succes:RegisterValidation()
    data class Failed(val message:String):RegisterValidation()
    data class RegisterFieldsState(
        val email:RegisterValidation,
        val password:RegisterValidation,
        val name:RegisterValidation
    )
}
