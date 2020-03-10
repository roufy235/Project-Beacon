package com.beacon.beacon

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.beacon.beacon.utilities.roufy235ActivityTransition
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

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileBtn.setOnClickListener(this)
        emergencyBtn.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        mRef = database.reference
    }

    override fun onStart() {
        super.onStart()
        currentUser = mAuth.currentUser
    }

    override fun onClick(v: View?) {
        v?.let {
            when(it.id) {
                R.id.profileBtn -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    this.roufy235ActivityTransition()
                }
                R.id.emergencyBtn -> {
                    MaterialAlertDialogBuilder(this)
                        .setMessage("Are you sure you want to sent this broadcast?")
                        .setPositiveButton("Yes") {dialog, which ->
                            dialog.dismiss()
                            val myAlert = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                            myAlert.progressHelper.barColor = Color.parseColor("#A5DC86")
                            myAlert.titleText = "Broadcasting"
                            myAlert.setCancelable(false)
                            myAlert.show()

                            Handler().postDelayed({
                                myAlert.dismiss()
                                SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Good job!")
                                    .setContentText("Broadcasted to appropriate agencies")
                                    .show();
                            }, 2000)

                        }
                        .setNegativeButton("No", null)
                        .setCancelable(false)
                        .show()
                }
                else -> {}
            }
        }
    }
}
