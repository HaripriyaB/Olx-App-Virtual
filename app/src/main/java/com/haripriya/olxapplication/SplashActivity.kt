package com.haripriya.olxapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.haripriya.olxapplication.ui.login.LoginActivity
import com.haripriya.olxapplication.utilities.Constants
import com.haripriya.olxapplication.utilities.SharedPref
import java.io.IOException
import java.util.*

/*
* SUMMARY
* First askforpermissions
* Then permission is requested through onRequestPermission
* it then calls enableGPS()
* then startlocationupdates() is called
* which calls the activityonResult to callback the response from user
* after callback is handled it goes to login activity
* */

class SplashActivity : BaseActivity() {

    private val MY_PERMISSIONS_REQUEST_LOCATION =100
    private var locationRequest:LocationRequest?=null
    private val REQUEST_GPS = 101

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout. activity_splash)
        askForPermissions()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback()


    }

    private fun locationCallback() {
    locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            var location = p0?.lastLocation
            SharedPref(this@SplashActivity).setString(Constants.CITY_NAME,getCityName(location)!!)

            if(SharedPref(this@SplashActivity).getString(Constants.USER_ID)?.isEmpty()!!) {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }
    }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun getCityName(locationSettingResponse: Location?): String?{
        var cityName = ""
        var geocoder = Geocoder(this, Locale.getDefault())
        try{
            val address=geocoder.getFromLocation(
                locationSettingResponse?.latitude!!
            ,locationSettingResponse.longitude,1)
            cityName = address[0].locality
        }catch (e: IOException){
            Log.d("LocationException","Failed")
        }
        return cityName
    }

    private fun askForPermissions() {
        val permission =arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permission,MY_PERMISSIONS_REQUEST_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var granted = false
        if(requestCode == MY_PERMISSIONS_REQUEST_LOCATION){
            for(grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    granted = true
                }
            }
            if(granted){
                enableGPS()
            }
        }
    }

    private fun enableGPS() {
        locationRequest=LocationRequest.create()
        locationRequest?.setExpirationDuration(3000)
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        var builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest!!)

        val task =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())

        task.addOnCompleteListener(object : OnCompleteListener<LocationSettingsResponse> {
            override fun onComplete(p0: Task<LocationSettingsResponse>) {
                try{
                    val response = task.getResult(ApiException::class.java)
                    startLocationUpdates()
                }catch (e : ApiException){
                    when(e.statusCode){
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->{
                            val resolvable = e as ResolvableApiException
                            resolvable.startResolutionForResult(this@SplashActivity,REQUEST_GPS)
                        }
                    }
                }
            }
        })
    }
    private fun startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_GPS){
            startLocationUpdates()
        }
    }
}