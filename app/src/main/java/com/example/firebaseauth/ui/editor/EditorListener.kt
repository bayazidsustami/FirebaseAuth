package com.example.firebaseauth.ui.editor

interface EditorListener {
    fun onStarted()
    fun onSuccess()
    fun onFailure(message: String)
}