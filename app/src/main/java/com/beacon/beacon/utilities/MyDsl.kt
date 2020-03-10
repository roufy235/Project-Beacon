package com.beacon.beacon.utilities

import android.app.Activity

fun Activity.roufy235ActivityTransition(isFinish : Boolean = false) {
    this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    if (isFinish) this.finish()
}