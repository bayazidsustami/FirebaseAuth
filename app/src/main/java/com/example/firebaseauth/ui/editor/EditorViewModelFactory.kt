package com.example.firebaseauth.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseauth.data.repositories.NoteRepository

@Suppress("UNCHECKED_CAST")
class EditorViewModelFactory(private val repository: NoteRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditorViewModel(repository) as T
    }
}