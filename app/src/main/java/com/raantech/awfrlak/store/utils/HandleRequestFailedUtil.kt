package com.raantech.awfrlak.store.utils

import android.app.Activity
import android.content.Context
import com.raantech.awfrlak.store.utils.extensions.shortToast
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.utils.extensions.showErrorAlert

object HandleRequestFailedUtil {

    fun handleRequestFailedMessages(
        context: Activity,
        serverErrorCode: ResponseSubErrorsCodeEnum?,
        requestMessage: String?
    ) {
        when (serverErrorCode) {
            ResponseSubErrorsCodeEnum.GENERAL_FAILED ->
                showDialogMessage(
                    requestMessage, context
                )
            ResponseSubErrorsCodeEnum.InvalidModel -> showDialogMessage(
                requestMessage, context
            )
            ResponseSubErrorsCodeEnum.Unauthorized -> showDialogMessage(
                requestMessage, context
            )
            ResponseSubErrorsCodeEnum.Forbidden -> showDialogMessage(
                requestMessage, context
            )
            ResponseSubErrorsCodeEnum.NotFound -> showDialogMessage(
                requestMessage, context
            )
            else -> showDialogMessage(
                requestMessage, context
            )
        }
    }

    fun showDialogMessage(
        requestMessage: String?,
        context: Activity
    ) {
        context.showErrorAlert(
            title = context.getString(R.string.alert_dialog_title),
            message = requestMessage!!
        )
    }

    private fun showToastMessage(message: String?, context: Context) {
        message?.let {
            context.shortToast(it)
        }
    }

}