package com.example.firebaseauth.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseauth.R
import com.example.firebaseauth.data.repositories.UserRepository
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class ForgotPasswordActivity : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val factory = AuthViewModelFactory(UserRepository.instance)

        viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        reset_pass_btn.setOnClickListener {
            val email = email_edt_text.text.toString()
            viewModel.resetPass(email)
        }

        login_btn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        viewModel.authListener = this
    }

    override fun onStarted() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressBar.visibility = View.GONE
        Toast.makeText(this, "Link reset send to your email", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
