package com.beacon.beacon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.beacon.beacon.utilities.roufy235ActivityTransition

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        this.roufy235ActivityTransition()
    }
}
