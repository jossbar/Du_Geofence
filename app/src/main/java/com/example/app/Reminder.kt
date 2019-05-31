package com.example.app

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class Reminder(val id: String = UUID.randomUUID().toString(),  //id부여시 고유성 확보
                    var latLng: LatLng?,
                    var radius: Double?,
                    var message: String?)