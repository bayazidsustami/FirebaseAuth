package com.example.firebaseauth.data.firebase

import com.example.firebaseauth.data.NoteEntity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.reactivex.Completable

class FirebaseDB {
    private val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("NOTE")
    }

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