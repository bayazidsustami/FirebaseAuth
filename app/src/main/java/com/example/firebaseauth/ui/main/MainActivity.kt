package com.example.firebaseauth.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.example.firebaseauth.R
import com.example.firebaseauth.data.repositories.UserRepository
import com.example.firebaseauth.ui.UserPreference
import com.example.firebaseauth.ui.auth.LoginActivity
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    //private val factory: MainViewModelFactory by instance()

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var viewModel: MainViewModel

    private var isLogin = false

    companion object{
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel1"
        private const val CHANNEL_NAME = "my channel"

        private const val ISLOGIN = "login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = MainViewModelFactory(UserRepository.instance)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        analytics = FirebaseAnalytics.getInstance(this)
        val preference = UserPreference(this)
        val user = viewModel.user

        val bundle = Bundle()
        bundle.putString("EMAIL", user?.email)
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)

        textView.text = user?.email

        signout_btn.setOnClickListener {
            viewModel.logout()
            preference.setLogin(false)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        Log.d("STATE", savedInstanceState?.getBoolean(ISLOGIN).toString())

        if (savedInstanceState?.getBoolean(ISLOGIN) != true ){
            user?.email?.let { showNotification(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(ISLOGIN, isLogin)
    }

    private fun showNotification(email: String){
        isLogin = true
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val mBuilder = NotificationCompat.Builder(this,
            CHANNEL_ID
        )
            .setContentTitle("New Login")
            .setContentText("$email was logged from firebase auth")
            .setLargeIcon(BitmapFactory.decodeResource(resources,
                R.drawable.firebase_logo
            ))
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000))
            .setSound(sound)
            .setFullScreenIntent(pendingIntent, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description =
                CHANNEL_NAME
            channel.vibrationPattern = longArrayOf(1000)
            mBuilder.setChannelId(CHANNEL_ID)

            notificationManager.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
