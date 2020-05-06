package com.example.firebaseauth.ui

import android.content.Context

internal class UserPreference(context: Context) {
    companion object{
        private const val PREF_NAME = "login_pref"
        private const val ISLOGIN = "isLogin"
    }

    private val preference = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setLogin(value: Boolean){
        val editor = preference.edit()
            editor.putBoolean(ISLOGIN, value)
            editor.apply()
    }

    fun getLogin(): Boolean {
        val value = false
        return preference.getBoolean(ISLOGIN, value)
    }
}