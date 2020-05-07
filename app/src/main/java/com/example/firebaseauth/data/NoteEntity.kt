package com.example.firebaseauth.data

data class NoteEntity(
    val idUser: String? =  null,
    val title: String? = null,
    val detail: String? = null,
    val time: String
) {
    constructor(): this("", "", "", "")
}