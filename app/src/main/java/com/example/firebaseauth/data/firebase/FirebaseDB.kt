package com.example.firebaseauth.data.firebase

import android.util.Log
import com.example.firebaseauth.data.NoteEntity
import com.example.firebaseauth.ui.main.MainListAdapter
import com.google.firebase.database.*
import io.reactivex.Completable

class FirebaseDB {
    private val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("NOTE")
    }

    private lateinit var myAdapter: MainListAdapter

    private val list = mutableListOf<NoteEntity>()
    fun insert(notes: NoteEntity) = Completable.create{emitter ->
        val id = database.push().key.toString()
        database.child(id).setValue(notes).addOnCompleteListener {
            if (!emitter.isDisposed){
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }
}