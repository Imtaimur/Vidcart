package com.example.shopit.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shopit.data.User
import com.example.shopit.utils.Constants.USER_COLLECTION
import com.example.shopit.utils.RegisterValidation
import com.example.shopit.utils.Resource
import com.example.shopit.utils.Resource.Success
import com.example.shopit.utils.Validname
import com.example.shopit.utils.validatePassword
import com.example.shopit.utils.validationemail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class  RegisterViewModel @Inject constructor (
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
):ViewModel(){
    private val _register=MutableStateFlow<Resource<User>>(Resource.Unspecified())
     val register: Flow<Resource<User>> =_register
    private val _validtation= Channel<RegisterValidation.RegisterFieldsState>()
    val validation=_validtation.receiveAsFlow()


    fun createAccountwithEmailnPassword(user: User,password:String){
        if (CheckValidation(user, password)
        ){
        runBlocking { _register.emit(Resource.Loading())}
        firebaseAuth.createUserWithEmailAndPassword(user.email,password).addOnSuccessListener {

            it.user?.let{

                _register.value= Success(user)
                saveUserdata(it.uid,user)
            }

        }.addOnFailureListener {
            _register.value=Resource.Error(it.message.toString())
        }}
        else
        {val registerFeildstate=RegisterValidation.RegisterFieldsState(
                validationemail(user.email)
                , validatePassword(password),
            Validname(firstname =user.firstname, secondname = user.lastname )

            )
            runBlocking { _validtation.send(registerFeildstate)
        }
        }

    }

    private fun saveUserdata(userUid:String,user: User) {
        db.collection(USER_COLLECTION).document(userUid).set(user).addOnSuccessListener {
            _register.value= Success(user)
        }

    }

    private fun CheckValidation(user: User, password: String):Boolean {
        val emailValid = validationemail(user.email)
        val passValid = validatePassword(password)
        val shouldRegister =
            emailValid is RegisterValidation.Succes && passValid is RegisterValidation.Succes
        return shouldRegister
    }}

