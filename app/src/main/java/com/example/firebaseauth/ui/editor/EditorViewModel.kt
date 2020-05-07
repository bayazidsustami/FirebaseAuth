package com.example.firebaseauth.ui.editor

import androidx.lifecycle.ViewModel
import com.example.firebaseauth.data.NoteEntity
import com.example.firebaseauth.data.repositories.NoteRepository
import com.example.firebaseauth.data.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class EditorViewModel(private val repository: NoteRepository): ViewModel() {
    private val disposables = CompositeDisposable()
    var editorListener: EditorListener? = null

    fun insert(title: String?, detail: String?){
        if (title.isNullOrEmpty() || detail.isNullOrEmpty()){
            editorListener?.onFailure("title and detail must not empty")
            return
        }

        editorListener?.onStarted()

        val time = currentDate()
        val repoUser = UserRepository
        val idUser = repoUser.instance.currentUser()?.email
        val notes = NoteEntity(idUser, title, detail, time)

        val disposable = repository.insert(notes)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                editorListener?.onSuccess()
            }, {
                it.message?.let { it1 -> editorListener?.onFailure(it1) }
            })
        disposables.add(disposable)
    }

    private fun currentDate(): String {
        val sdf = SimpleDateFormat("dd/MMM/yyyy hh:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}