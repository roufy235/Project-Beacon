package com.beacon.beacon.utilities

import android.app.Activity
import android.graphics.Color
import android.widget.EditText
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

fun Activity.roufy235ActivityTransition(isFinish : Boolean = false) {
    this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    if (isFinish) this.finish()
}

fun DatabaseReference.roufy235SetUserInfo(uid : String, data : HashMap<String, String>) : Task<Void> {
    return this.child("userInfo").child(uid).setValue(data)
}

fun DatabaseReference.roufy235GetProfile(uid : String, complete : (HashMap<*, *>?) -> Unit) {
    this.child("userInfo").child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
        override fun onCancelled(p0: DatabaseError) {
            complete(null)
        }

        override fun onDataChange(value: DataSnapshot) {
            if (value.value != null) {
                val data = value.value as HashMap<*, *>
                complete(data)
            } else {
                complete(null)
            }
        }

    })
}

fun Activity.roufy235SweetAlertDialogSuccess(title : String, text : String) {
    SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        .setTitleText(title)
        .setContentText(text)
        .show()
}
fun Activity.roufy235SweetAlertDialogError(title : String, text : String) {
    SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
        .setTitleText(title)
        .setContentText(text)
        .show()
}

fun Activity.roufy235SweetAlertDialogProgress(message : String) : SweetAlertDialog {
    val myAlert = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
    myAlert.progressHelper.barColor = Color.parseColor("#A5DC86")
    myAlert.titleText = message
    myAlert.setCancelable(false)
    myAlert.show()
    return myAlert
}

fun DatabaseReference.roufy235EmergencyInfo(userId : String, data : HashMap<String, String>) : Task<Void> {
    return this.child("emergency").child(userId).push().setValue(data)
}

fun EditText.roufy235ValidateEditText() : Boolean {
    return if (this.text.toString().isNotEmpty()) {
        true
    } else {
        this.error = "required"
        false
    }
}