package com.example.firebaseauth.ui.main

import androidx.lifecycle.ViewModel
import com.example.firebaseauth.data.repositories.UserRepository

class MainViewModel(private val repository: UserRepository): ViewModel() {

    val user by lazy {
        repository.currentUser()
    }

    fun logout(){
        repository.logout()
    }

}