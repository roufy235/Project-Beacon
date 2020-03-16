package com.beacon.beacon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.beacon.beacon.utilities.*
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

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileBtn.setOnClickListener(this)
        emergencyBtn.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        mRef = database.reference
        currentUser = mAuth.currentUser


        currentUser?.let {
            mRef.roufy235GetProfile(it.uid) { nullData ->
                myProfileData = nullData
            }
        }

    }


    private fun sendEmergencyMessage() {
        val emergencyTextStr = emergencyText.text.toString()
        if (emergencyTextStr.isNotEmpty()) {
            MaterialAlertDialogBuilder(this)
                .setMessage("Are you sure you want to sent this broadcast?")
                .setPositiveButton("Yes") {dialog, which ->
                    dialog.dismiss()
                    if (currentUser != null && myProfileData != null) {
                        val myAlert = this.roufy235SweetAlertDialogProgress("Broadcasting")
                        val emergencyData = HashMap<String, String>()
                        val uid = currentUser!!.uid
                        emergencyData["name"] = myProfileData!!["name"].toString()
                        emergencyData["uui"] = uid
                        emergencyData["emergencyMessage"] = emergencyTextStr
                        emergencyData["timeStamp"] = System.currentTimeMillis().toString()

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
