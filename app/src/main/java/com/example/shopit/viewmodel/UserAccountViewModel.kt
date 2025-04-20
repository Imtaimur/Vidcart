package com.example.shopit.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopit.ShopitApplication
import com.example.shopit.data.User
import com.example.shopit.utils.RegisterValidation
import com.example.shopit.utils.Resource
import com.example.shopit.utils.validationemail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.security.interfaces.RSAKey
import java.util.UUID
import javax.inject.Inject
@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val storage: StorageReference,

    app:Application

):AndroidViewModel(app) {
    private val _user=MutableStateFlow<Resource<User>>(Resource.Unspecified())
     val user=_user.asStateFlow()
    private val _updateinfo=MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val updateinfo=_updateinfo.asStateFlow()
    fun getUser(){
        viewModelScope.launch {
            _user.emit(Resource.Loading())
        }
            firestore.collection("user").document(firebaseAuth.uid!!).get().addOnSuccessListener {
                val user=it.toObject(User::class.java)
                user?.let {
                    viewModelScope.launch { _user.emit(Resource.Success(it)) }

                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _user.emit(Resource.Error(it.message.toString()))
                }
            }
    }
    fun updateuser(user: User,imageuri:Uri?){
        val validateEmail= validationemail(user.email) is RegisterValidation.Succes
                &&user.firstname.trim().isNotEmpty()
                &&user.lastname.trim().isNotEmpty()
        viewModelScope.launch {
            _updateinfo.emit(Resource.Loading())
        }
        if (imageuri==null){
            saveUserinfo(user,true)

        }else{
            saveUserwithnewimg(user,imageuri)
        }


    }

    private fun saveUserwithnewimg(user: User, imgUri: Uri) {
        viewModelScope.launch {
            try {
                val imagbitmap=MediaStore.Images.Media.getBitmap(getApplication<ShopitApplication>().contentResolver,imgUri)
                val byteArrayOutputStream =ByteArrayOutputStream()
                imagbitmap.compress(Bitmap.CompressFormat.JPEG,96,byteArrayOutputStream)
                val imageByteArray=byteArrayOutputStream.toByteArray()
                val imageDirectory=storage.child("profileImages/${firebaseAuth.uid}/${UUID.randomUUID()}")
                 val result=imageDirectory.putBytes(imageByteArray).await()
                val imageurl=result.storage.downloadUrl.await().toString()
                saveUserinfo(user.copy(imgpath = imageurl),false)

            }catch (e:Exception){
                viewModelScope.launch {
                    _updateinfo.emit(Resource.Error(e.message.toString()))}




            }
        }


    }

    private fun saveUserinfo(user: User, shouldRetriveoldimg: Boolean) {
        firestore.runTransaction{transaction->
            val documentRef=firestore.collection("user").document(firebaseAuth.uid!!)

            if (shouldRetriveoldimg){
                val currenntuser=transaction.get(documentRef).toObject(User::class.java)
                val newuser=user.copy(imgpath = currenntuser?.imgpath?:""    )
                transaction.set(documentRef,newuser)
            }
            else{
                transaction.set(documentRef,user)
            }
        }.addOnSuccessListener {
            viewModelScope.launch {
                _updateinfo.emit(Resource.Success(user))}


        }.addOnFailureListener {
            viewModelScope.launch {
                _updateinfo.emit(Resource.Error(it.message.toString()))}
        }


    }
}