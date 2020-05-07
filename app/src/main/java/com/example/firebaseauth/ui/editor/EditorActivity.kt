package com.example.firebaseauth.ui.editor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseauth.R
import com.example.firebaseauth.data.repositories.NoteRepository
import kotlinx.android.synthetic.main.activity_editor.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class EditorActivity : AppCompatActivity(), EditorListener, KodeinAware {

    override val kodein by kodein()

    private lateinit var viewModel: EditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        val factory = EditorViewModelFactory(NoteRepository.instance)

        viewModel = ViewModelProvider(this, factory)[EditorViewModel::class.java]
        viewModel.editorListener = this

        btn_submit.setOnClickListener {
            val title = edt_title.text.toString()
            val detail = edt_detail.text.toString()

            viewModel.insert(title, detail)
        }

    }

    override fun onStarted() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onSuccess() {
        progressBar.visibility = View.GONE
        Toast.makeText(this, "Success submit note", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(message: String) {
        progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
