package com.raantech.awfrlak.store.utils.extensions

import android.app.Activity
import com.tapadoo.alerter.Alerter
import com.raantech.awfrlak.R

fun Activity?.showErrorAlert(title: String = "", message: String) {
    Alerter.create(this)
        .setTitle(title)
        .setTitleAppearance(R.style.AlertTextAppearance)
        .setText(message)
        .setTextAppearance(R.style.AlertTextAppearance)
        .setBackgroundColorRes(R.color.alert_red) // or setBackgroundColorInt(Color.CYAN)
        .show()
}

fun Activity.showValidationErrorAlert(title: String = this.resources.getString(R.string.app_name), message: String) {
    Alerter.create(this)
        .setTitle(title)
        .setTitleAppearance(R.style.AlertTextAppearance)
        .setText(message)
        .setTextAppearance(R.style.AlertTextAppearance)
        .setBackgroundColorRes(R.color.alert_red) // or setBackgroundColorInt(Color.CYAN)
        .show()
}
//fun Activity.showUpdateDialog(updateStatus: UpdateStatus) {
//    UpdateAppDialog(this,updateStatus)
//        .show()
//}