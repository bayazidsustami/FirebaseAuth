package com.example.firebaseauth.data.repositories

import com.example.firebaseauth.data.firebase.FirebaseSource

class UserRepository(private val firebase: FirebaseSource) {
    fun login(email: String, password: String) = firebase.login(email, password)

    fun register(email: String, password: String) = firebase.register(email, password)

    fun currentUser() = firebase.currentUser()

    fun logout() = firebase.logout()

    companion object{
        val instance by lazy { UserRepository(FirebaseSource()) }
    }
}