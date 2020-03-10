package com.beacon.beacon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.beacon.beacon.utilities.roufy235ActivityTransition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    private lateinit var mAuth : FirebaseAuth
    private  var currentUser : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        mAuth = FirebaseAuth.getInstance()


        Handler().postDelayed({
            progressBar.visibility = View.VISIBLE
        }, 1000)
    }

    override fun onStart() {
        super.onStart()
        currentUser = mAuth.currentUser

        Handler().postDelayed({
            if (currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, SignUpActivity::class.java))
            }
            this.roufy235ActivityTransition(true)
        }, 2000)
    }
}
