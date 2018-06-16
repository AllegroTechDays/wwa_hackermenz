package home.antonyaskiv.hackaton.View

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.FusedLocationApi
import home.antonyaskiv.hackaton.Model.Request
import home.antonyaskiv.hackaton.Presenters.MainPresenter
import home.antonyaskiv.hackaton.R
import kotlinx.android.synthetic.main.activity_category.*
import java.security.AccessController.getContext


class CategoryActivity : AppCompatActivity() {

    val REQUEST_CHECK_LOCATION_SETTINGS = 2
    val PERMISSION_REQUEST_LOCATION = 8
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var lastLocation: Location? = null


    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            FusedLocationApi.removeLocationUpdates(googleApiClient, this)
            lastLocation = location
        }
    }

    public override fun onStart() {
        super.onStart()
        if (googleApiClient != null && !googleApiClient!!.isConnected() && !googleApiClient!!.isConnecting()) {
            googleApiClient!!.connect()
        }
    }

    public override fun onStop() {
        if (googleApiClient != null && googleApiClient!!.isConnected()) {
            googleApiClient!!.disconnect()
        }
        super.onStop()
    }

    private var distance: String = ""

    private var withReturn: String = ""

    private var sex: String = ""

    private var lat: String = ""

    private var lng: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        createGoogleApiClientIfNeeded()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startLocationUpdates()
        }

        if (intent.hasExtra("distance")) {
            distance = intent.getStringExtra("distance")
            withReturn = intent.getStringExtra("withReturn")
            sex = intent.getStringExtra("sex")
        } else if (intent.hasExtra("lng")) {
            lng = intent.getStringExtra("lng")
            lat = intent.getStringExtra("lat")
            sex = intent.getStringExtra("sex")

        }

        cv_category_filtered.setOnClickListener { v ->
            Log.d("bla", lastLocation.toString())
        }

        cv_category_random.setOnClickListener { v ->
            if (!lng.isEmpty() && lastLocation != null) {
                var intent = Intent(this, MapActivity::class.java)
                intent.putExtra("type", "random")
                intent.putExtra("lng_to", lng)
                intent.putExtra("lat_to", lat)
                intent.putExtra("lng_from", lastLocation!!.longitude.toString())
                intent.putExtra("lat_from", lastLocation!!.latitude.toString())
                startActivity(intent)
            }
            Log.d("bla", lastLocation.toString())
        }

        cv_category_speed.setOnClickListener { v ->
            Log.d("bla", lastLocation.toString())
        }

    }

    private fun createGoogleApiClientIfNeeded() {
        if (googleApiClient == null) {
            googleApiClient = GoogleApiClient.Builder(applicationContext)
                    .addApi(LocationServices.API).build()
        }
    }




    @SuppressLint("RestrictedApi")
    private fun createLocationRequestIfNeeded() {
        if (locationRequest == null) {
            locationRequest = LocationRequest()
            locationRequest!!.interval = 5000
            locationRequest!!.fastestInterval = 5000
            locationRequest!!.numUpdates = 1
            locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    PERMISSION_REQUEST_LOCATION)
        }
    }

    @SuppressLint("RestrictedApi")
    fun startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission()
                return
            }
        }
        createLocationRequestIfNeeded()
        val builder = locationRequest?.let {
            LocationSettingsRequest.Builder()
                    .addLocationRequest(it)
        }

        val locationSettingsResult = LocationServices.SettingsApi.checkLocationSettings(
                googleApiClient,
                builder!!.build())

        locationSettingsResult.setResultCallback { locationSettingsResult1 ->
            val status = locationSettingsResult1.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> if (getContext() != null) {
                    if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) === PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) === PackageManager.PERMISSION_GRANTED) {
                        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient)
                    }
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            this.startIntentSenderForResult(status.resolution.intentSender, REQUEST_CHECK_LOCATION_SETTINGS, null, 0, 0, 0, null)
                        }
                    } catch (ignored: IntentSender.SendIntentException) {
                    }

                }
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CHECK_LOCATION_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                startLocationUpdates()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}