package com.example.firebaseauth.data.repositories

import com.example.firebaseauth.data.NoteEntity
import com.example.firebaseauth.data.firebase.FirebaseDB

class NoteRepository(private val firebase: FirebaseDB) {
    fun insert(noteEntity: NoteEntity) = firebase.insert(noteEntity)

    companion object{
        val instance by lazy { NoteRepository(FirebaseDB()) }
    }
}