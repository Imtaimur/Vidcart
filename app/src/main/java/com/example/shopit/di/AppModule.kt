package com.example.shopit.di


import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.shopit.firebase.firebasecommon
import com.example.shopit.utils.Constants.INTRODUCION_SP
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebase()= FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore()= Firebase.firestore
    @Provides
    fun provideIntroductionsp(
        application: Application
    )=application.getSharedPreferences(INTRODUCION_SP,MODE_PRIVATE)
    @Provides
    @Singleton
    fun providefirebasecommon(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    )=firebasecommon(firebaseFirestore,firebaseAuth)
}