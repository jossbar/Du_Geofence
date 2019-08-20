package com.example.app


import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Button
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import kotlinx.android.synthetic.main.activity_maps.*


class GeofenceTransitionService  : IntentService("GeoTrIntentService") {
    interface ChangeViewState {
        fun changeButtonState() : Button
    }

    companion object {
        private const val LOG_TAG = "GeoTrIntentService"
    }


    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.errorCode)
            Log.e(LOG_TAG, errorMessage)
            return
        }

        handleEvent(geofencingEvent)
    }
    private val changeViewState : ChangeViewState? = null

    private fun handleEvent(event: GeofencingEvent) {
        if (event.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {

            val reminder = getFirstReminder(event.triggeringGeofences)
            val message = reminder?.message
            val latLng = reminder?.latLng
            //geofence 영역 들어올 시 버튼 활성화 코드 추가
            changeViewState?.changeButtonState()?.isEnabled = true
            if (message != null && latLng != null) {
                sendNotification(this, message, latLng)

            }
        }
    }

    private fun getFirstReminder(triggeringGeofences: List<Geofence>): Reminder? {
        val firstGeofence = triggeringGeofences[0]
        return (application as ReminderApp).getRepository().get(firstGeofence.requestId)
    }


}




