package com.example.app

import android.app.Application

class ReminderApp : Application() {

    private lateinit var repository: ReminderRepository
    private  lateinit var mainActivity: MainActivity

    override fun onCreate() {
        super.onCreate()
        repository = ReminderRepository(this)
        mainActivity = MainActivity()

    }

    fun getRepository() = repository
    fun getMainActivity() = mainActivity
}