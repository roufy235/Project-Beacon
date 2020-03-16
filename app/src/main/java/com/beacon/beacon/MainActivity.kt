package com.beacon.beacon

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.beacon.beacon.utilities.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAuth : FirebaseAuth
    private  var currentUser : FirebaseUser? = null
    private lateinit var database : FirebaseDatabase
    private lateinit var mRef : DatabaseReference
    private var myProfileData : HashMap<*, *>? = null
    private var latitude : Double = 0.0
    private var logitude : Double = 0.0

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileBtn.setOnClickListener(this)
        emergencyBtn.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        mRef = database.reference
        currentUser = mAuth.currentUser

        checkLocationPermission()
        currentUser?.let {
            mRef.roufy235GetProfile(it.uid) { nullData ->
                myProfileData = nullData
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            2000 -> {
                if (grantResults.isNotEmpty()) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        checkLocationPermission()
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2000)
                return
            }
        }
        getPhoneLocation()
    }



    private fun getPhoneLocation() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(locationRequest, object : LocationCallback(){
                override fun onLocationResult(p0: LocationResult?) {
                    super.onLocationResult(p0)
                    LocationServices.getFusedLocationProviderClient(this@MainActivity)
                        .removeLocationUpdates(this)
                    if (p0 != null && p0.locations.size > 0) {
                        val lastLocationIndex = p0.locations.size - 1
                        latitude = p0.locations[lastLocationIndex].latitude
                        logitude = p0.locations[lastLocationIndex].longitude

                    }
                }
            }, Looper.getMainLooper())
    }


    private fun sendEmergencyMessage() {
        val emergencyTextStr = emergencyText.text.toString()
        if (emergencyTextStr.isNotEmpty()) {
            MaterialAlertDialogBuilder(this)
                .setMessage("Are you sure you want to sent this broadcast?")
                .setPositiveButton("Yes") {dialog, which ->
                    dialog.dismiss()
                    if (currentUser != null && myProfileData != null) {
                        checkLocationPermission()
                        val myAlert = this.roufy235SweetAlertDialogProgress("Broadcasting")
                        val emergencyData = HashMap<String, Any>()
                        val uid = currentUser!!.uid
                        emergencyData["name"] = myProfileData!!["name"].toString()
                        emergencyData["uui"] = uid
                        emergencyData["emergencyMessage"] = emergencyTextStr
                        emergencyData["timeStamp"] = System.currentTimeMillis().toString()
                        emergencyData["lat"] = latitude
                        emergencyData["lng"] = logitude

                        mRef.roufy235EmergencyInfo(uid, emergencyData)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    myAlert.dismiss()
                                    emergencyText.setText("")
                                    this.roufy235SweetAlertDialogSuccess("Good job!", "Broadcasted to appropriate agencies")
                                }
                            }
                            .addOnFailureListener {
                                myAlert.dismiss()
                                this.roufy235SweetAlertDialogError("Oops!", "Unable to send broadcast")
                            }
                    } else {
                        Toast.makeText(this, "Invalid account", Toast.LENGTH_LONG).show()
                    }
                }
                .setNegativeButton("No", null)
                .setCancelable(false)
                .show()
        } else {
            emergencyText.roufy235ValidateEditText()
        }

    }

    override fun onClick(v: View?) {
        v?.let {
            when(it.id) {
                R.id.profileBtn -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    this.roufy235ActivityTransition()
                }
                R.id.emergencyBtn -> {
                    sendEmergencyMessage();
                }
                else -> {}
            }
        }
    }
}
