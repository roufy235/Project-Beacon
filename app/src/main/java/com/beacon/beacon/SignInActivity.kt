package com.beacon.beacon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.beacon.beacon.utilities.roufy235ActivityTransition
import com.beacon.beacon.utilities.validateEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAuth : FirebaseAuth
    private  var currentUser : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signInBtn.setOnClickListener(this)

        mAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        currentUser = mAuth.currentUser
    }

    override fun onClick(v: View?) {
        v?.let {
            when(it.id) {
                R.id.signInBtn -> {
                    signInFunc()
                }
                else -> {}
            }
        }
    }

    private fun signInFunc() {
        val email = editTextName
        val password = editTextPassword
        if (email.validateEditText()) {
            if (password.validateEditText()) {
                val dialog = SpotsDialog.Builder()
                    .setContext(this)
                    .setMessage("Logging")
                    .setCancelable(false)
                    .build()
                    .apply {
                        show()
                    }
                mAuth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener {
                        dialog.dismiss()
                        if (it.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            this.roufy235ActivityTransition(true)
                        } else {
                            Toast.makeText(this, "Invalid email and password", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener {
                        dialog.dismiss()
                        Toast.makeText(this, "Invalid email and password", Toast.LENGTH_LONG).show()
                    }
            }
        }

    }
}
