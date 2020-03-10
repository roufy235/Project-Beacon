package com.beacon.beacon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.beacon.beacon.utilities.roufy235ActivityTransition
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)


        Handler().postDelayed({
            progressBar.visibility = View.VISIBLE
        }, 1000)

        Handler().postDelayed({
            startActivity(Intent(this, SignUpActivity::class.java))
            this.roufy235ActivityTransition(true)
        }, 2000)
    }
}
