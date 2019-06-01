package com.example.app

import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import com.example.app.ReminderApp
import com.google.android.gms.maps.GoogleMap

abstract class BaseActivity : AppCompatActivity() {
  fun getRepository() = (application as ReminderApp).getRepository()

}