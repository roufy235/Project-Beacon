package com.beacon.beacon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.beacon.beacon.utilities.roufy235ActivityTransition
import com.beacon.beacon.utilities.roufy235SetUserInfo
import com.beacon.beacon.utilities.roufy235ValidateEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mAuth : FirebaseAuth
    private  var currentUser : FirebaseUser? = null
    private lateinit var database : FirebaseDatabase
    private lateinit var mRef : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signInClicked.setOnClickListener(this)
        signUpBtnClicked.setOnClickListener(this)

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
                R.id.signUpBtnClicked -> {
                    startRegistration()
                }
                R.id.signInClicked -> {
                    startActivity(Intent(this, SignInActivity::class.java))
                    this.roufy235ActivityTransition(true)
                }
                else -> {}
            }
        }
    }


    private fun startRegistration() {
        val name = editTextName
        val email = editText
        val phone = editTextPhone
        val password = editTextPassword
        val rPassword = editTextRepeatPassword
        if (name.roufy235ValidateEditText()) {
            if (email.roufy235ValidateEditText()) {
                if (phone.roufy235ValidateEditText()) {
                    if (password.roufy235ValidateEditText()) {
                        if (rPassword.roufy235ValidateEditText()) {
                            if (password.text.toString() == rPassword.text.toString()) {
                                val dialog = SpotsDialog.Builder()
                                    .setContext(this)
                                    .setMessage("Processing")
                                    .setCancelable(false)
                                    .build()
                                    .apply {
                                        show()
                                    }
                                mAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                                    .addOnCompleteListener { task ->
                                        if (task.isComplete) {
                                            val data = HashMap<String, String>()
                                            data["name"] = name.text.toString()
                                            data["email"] = email.text.toString()
                                            data["phone"] = phone.text.toString()
                                            mRef.roufy235SetUserInfo(data).addOnCompleteListener {
                                                    dialog.dismiss()
                                                    startActivity(Intent(this, MainActivity::class.java))
                                                    this.roufy235ActivityTransition(true)
                                            }
                                                .addOnFailureListener {
                                                    dialog.dismiss()
                                                    Toast.makeText(this, "Unable to create account", Toast.LENGTH_LONG).show()
                                                }
                                        } else {
                                            dialog.dismiss()
                                            Toast.makeText(this, "Invalid email and password", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(this, "Password mismatch", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }
}
