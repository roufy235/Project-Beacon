package com.beacon.beacon.utilities

import android.app.Activity
import android.widget.EditText
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference

fun Activity.roufy235ActivityTransition(isFinish : Boolean = false) {
    this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    if (isFinish) this.finish()
}

fun DatabaseReference.setUserInfo(data : HashMap<String, String>) : Task<Void> {
    return this.child("userInfo").setValue(data)
}

fun EditText.validateEditText() : Boolean {
    return if (this.text.toString().isNotEmpty()) {
        true;
    } else {
        this.error = "required"
        false;
    }
}