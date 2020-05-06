package com.example.firebaseauth.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.firebaseauth.R
import com.example.firebaseauth.ui.auth.LoginActivity
import com.example.firebaseauth.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val preference = UserPreference(this)
        val value = preference.getLogin()
        Handler().postDelayed({
            if (value){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, 2000L)

    }
}
