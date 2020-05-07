package com.example.firebaseauth.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebaseauth.R
import com.example.firebaseauth.data.NoteEntity
import com.example.firebaseauth.data.repositories.NoteRepository
import com.example.firebaseauth.data.repositories.UserRepository
import com.example.firebaseauth.ui.UserPreference
import com.example.firebaseauth.ui.auth.LoginActivity
import com.example.firebaseauth.ui.editor.EditorActivity
import com.example.firebaseauth.ui.editor.EditorViewModel
import com.example.firebaseauth.ui.editor.EditorViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class MainActivity : AppCompatActivity(), KodeinAware{

    override val kodein by kodein()
    //private val factory: MainViewModelFactory by instance()

    private val database= FirebaseDatabase.getInstance().getReference("NOTE")

    private lateinit var viewModel: MainViewModel
    private lateinit var editViewMode: EditorViewModel
    private lateinit var preference: UserPreference
    private lateinit var mainAdapter: MainListAdapter

    private val dataList= mutableListOf<NoteEntity>()
    private var isLogin = false

    companion object{
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel1"
        const val CHANNEL_NAME = "my channel"

        const val ISLOGIN = "login"
    }

    private lateinit var noteEntity: NoteEntity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preference = UserPreference(this)

        mainAdapter = MainListAdapter(dataList)
        with(rv_note){
            setHasFixedSize(true)
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        val factory = MainViewModelFactory(UserRepository.instance)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        val factory2 = EditorViewModelFactory(NoteRepository.instance)
        editViewMode = ViewModelProvider(this, factory2)[EditorViewModel::class.java]


        FirebaseMessaging.getInstance().subscribeToTopic("information")

        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addNote -> startActivity(Intent(this, EditorActivity::class.java))
            R.id.logout -> {
                viewModel.logout()
                preference.setLogin(false)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(ISLOGIN, isLogin)
    }

    private fun getData(){
        progressBar.visibility = View.VISIBLE
        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "$p0", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    var note: NoteEntity?
                    progressBar.visibility = View.GONE
                    dataList.clear()
                    for (i in p0.children) {
                        note = i.getValue(NoteEntity::class.java)
                        dataList.add(note!!)
                    }
                    mainAdapter.notifyDataSetChanged()
                    Log.d("LOAD_DATA", "$p0")
                }
            }
        })
    }
}
