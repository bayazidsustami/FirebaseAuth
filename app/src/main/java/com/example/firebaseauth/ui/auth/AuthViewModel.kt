package com.example.firebaseauth.ui.auth

import androidx.lifecycle.ViewModel
import com.example.firebaseauth.data.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel(private val repository: UserRepository): ViewModel() {
    var authListener: AuthListener? = null
    private val disposables = CompositeDisposable()

    fun login(email: String?, password: String?){
        if (email.isNullOrEmpty() || password.isNullOrEmpty()){
            authListener?.onFailure("Invalid email or password")
        }

        authListener?.onStarted()

        //calling login from repository to perform the actual authentication
        val disposable = repository.login(email!!, password!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })

        disposables.add(disposable)
    }

    fun signUp(email: String?, password: String?){
        if (email.isNullOrEmpty() || password.isNullOrEmpty()){
            authListener?.onFailure("Please input all field")
            return
        }
        authListener?.onStarted()

        val disposable = repository.register(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })
        disposables.add(disposable)
    }

    fun resetPass(email: String){
        if (email.isEmpty()){
            authListener?.onFailure("email field must not null")
            return
        }
        authListener?.onStarted()

        val disposable = repository.forgotPassword(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                authListener?.onSuccess()
            }, {
                authListener?.onFailure(it.message!!)
            })

        disposables.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}