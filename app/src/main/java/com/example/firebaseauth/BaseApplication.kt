package com.example.firebaseauth

import android.app.Application
import com.example.firebaseauth.data.firebase.FirebaseDB
import com.example.firebaseauth.data.firebase.FirebaseSource
import com.example.firebaseauth.data.repositories.NoteRepository
import com.example.firebaseauth.data.repositories.UserRepository
import com.example.firebaseauth.ui.auth.AuthViewModelFactory
import com.example.firebaseauth.ui.editor.EditorViewModelFactory
import com.example.firebaseauth.ui.main.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class BaseApplication: Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@BaseApplication))

        bind() from singleton { FirebaseSource() }
        bind() from singleton { FirebaseDB() }
        bind() from singleton { NoteRepository(instance()) }
        bind() from singleton { UserRepository(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { EditorViewModelFactory(instance()) }
        bind() from provider { MainViewModelFactory(instance()) }
    }
}