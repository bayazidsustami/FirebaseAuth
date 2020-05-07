package com.example.firebaseauth.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseauth.R
import com.example.firebaseauth.data.repositories.UserRepository
import com.example.firebaseauth.ui.UserPreference
import com.example.firebaseauth.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class LoginActivity : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()

    private lateinit var viewModel: AuthViewModel

    private lateinit var preference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        preference = UserPreference(this)

        val factory = AuthViewModelFactory(UserRepository.instance)
        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        login_btn.setOnClickListener {
            val email: String = email_edt_text.text.toString()
            val password: String = pass_edt_text.text.toString()

            Log.d("TAG", "CLICKED")
            viewModel.login(email, password)
        }

        signup_btn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }

        reset_pass_tv.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            finish()
        }

        viewModel.authListener = this
    }

    override fun onStarted() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressBar.visibility = View.GONE
        preference.setLogin(true)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onFailure(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
