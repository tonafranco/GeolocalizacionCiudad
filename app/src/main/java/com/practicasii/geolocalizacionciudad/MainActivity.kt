package com.practicasii.geolocalizacionciudad

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.practicasii.geolocalizacionciudad.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        updateLocation()
        binding.btnUpdateLocalitation.setOnClickListener{
            updateLocation()
        }
    }

    fun updateLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Doesn't have permission",Toast.LENGTH_SHORT).show()
            Log.d("LocationPermissions", "Doesn't have permission")
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),1)
            }
            return
        }else{
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if(location != null) {
                    Log.d("LocationPermissions","Success ${location?.latitude}, ${location?.longitude}")
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses: List<Address> = geocoder.getFromLocation(location?.latitude, location?.longitude, 1)
                    Log.d("LocationPermissions", "${addresses[0]}")
                    binding.tvCordinates.setText("${addresses[0].locality}, ${addresses[0].adminArea}, ${addresses[0].countryName}")
                }
            }
            Log.d("LocationPermissions", "Has permission ")
        }
    }
}
