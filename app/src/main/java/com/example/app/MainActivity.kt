package com.example.app

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*


class MainActivity : BaseActivity(),  OnMapReadyCallback {
    companion object {
        private const val EXTRA_LAT_LNG = "EXTRA_LAT_LNG"
        private const val MY_LOCATION_REQUEST_CODE = 329
        private const val NEW_REMINDER_REQUEST_CODE = 330

        fun newIntent(context: Context, latLng: LatLng): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_LAT_LNG, latLng)
            return intent
        }
    }
    private var map: GoogleMap? = null
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        newReminder.visibility = View.GONE
        currentLocation.visibility = View.GONE
        newReminder.setOnClickListener {
            map?.run {
                val intent = NewReminderActivity.newIntent(
                        this@MainActivity,
                        cameraPosition.target,
                        cameraPosition.zoom)
                startActivityForResult(intent, NEW_REMINDER_REQUEST_CODE)
            }
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager //location api

        if (ContextCompat.checkSelfPermission(                      //permision checking
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_LOCATION_REQUEST_CODE)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == NEW_REMINDER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            showReminders()

            val reminder = getRepository().getLast()
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(reminder?.latLng, 15f))

            Snackbar.make(main, R.string.reminder_added_success, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,               //get permision result code
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            onMapAndPermissionReady()
        }
    }

    private fun onMapAndPermissionReady() {
        if (map != null
                && ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map?.isMyLocationEnabled = true
            newReminder.visibility = View.VISIBLE
            currentLocation.visibility = View.VISIBLE

            currentLocation.setOnClickListener {
                val bestProvider = locationManager.getBestProvider(Criteria(), false)
                val location = locationManager.getLastKnownLocation(bestProvider)
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
            showReminders()

            centerCamera()
        }
    }

    private fun centerCamera() {
        if (intent.extras != null && intent.extras.containsKey(EXTRA_LAT_LNG)) {
            val latLng = intent.extras.get(EXTRA_LAT_LNG) as LatLng
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))             //get focus center
        }
    }

    private fun showReminders() {
        map?.run {
            clear()
            for (reminder in getRepository().getAll()) {
                showReminderInMap(this@MainActivity, this, reminder)
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)

    }
}
