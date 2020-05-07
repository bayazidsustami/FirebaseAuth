package com.example.firebaseauth.ui.auth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import com.example.firebaseauth.R
import com.example.firebaseauth.data.repositories.UserRepository
import com.example.firebaseauth.ui.main.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AuthViewModel(private val repository: UserRepository): ViewModel() {
    var authListener: AuthListener? = null
    private val disposables = CompositeDisposable()

    fun login(email: String?, password: String?, context: Context){
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
                showNotification(email, context)
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

    private fun showNotification(email: String, context: Context){
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val mBuilder = NotificationCompat.Builder(context,
            MainActivity.CHANNEL_ID
        )
            .setContentTitle("New Login")
            .setContentText("$email was logged from firebase auth")
            .setLargeIcon(
                BitmapFactory.decodeResource(context.resources,
                R.drawable.firebase_logo
            ))
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000))
            .setSound(sound)
            .setFullScreenIntent(pendingIntent, true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                MainActivity.CHANNEL_ID,
                MainActivity.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channel.description =
                MainActivity.CHANNEL_NAME
            channel.vibrationPattern = longArrayOf(1000)
            mBuilder.setChannelId(MainActivity.CHANNEL_ID)

            notificationManager.createNotificationChannel(channel)
        }
        val notification = mBuilder.build()
        notificationManager.notify(MainActivity.NOTIFICATION_ID, notification)
    }
}