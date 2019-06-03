package com.example.app

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.GeofencingEvent


class GeofenceTransition  : IntentService("GeoTrIntentService") {

    companion object {
        private const val LOG_TAG = "GeoTrIntentService"
    }

    override fun onHandleIntent(intent: Intent?) {

        }



}

