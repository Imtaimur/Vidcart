package com.example.shopit.utils

import android.util.Patterns

fun validationemail(
    email:String
):RegisterValidation{


    if (email.isEmpty())
        return RegisterValidation.Failed("Email can't be Empty")
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Email Formate is wrong")
    return RegisterValidation.Succes

}
fun validatePassword(Password:String):RegisterValidation{
    if (Password.isEmpty())
        return RegisterValidation.Failed("Password can't be empty")
    if (Password.length<6)
        return RegisterValidation.Failed("Password Should have At least 6 char")
    return RegisterValidation.Succes
}
fun Validname(
    firstname:String,
    secondname:String):RegisterValidation{
        if (firstname.isEmpty())
            return RegisterValidation.Failed("first name should not be empty")
    if(secondname.isEmpty())
        return RegisterValidation.Failed("Second name shludl not be empty")
    return RegisterValidation.Succes
    }
