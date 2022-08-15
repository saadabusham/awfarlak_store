package com.raantech.awfrlak.store.ui.base.bindingadapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.enums.InputFieldValidStateEnums
import com.raantech.awfrlak.store.utils.extensions.hideKeyboard


@SuppressLint("ClickableViewAccessibility")
fun EditText.setupClearButtonWithAction() {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val clearIcon = if (editable?.isNotEmpty() == true) R.drawable.ic_close_keyboard else 0
            setCompoundDrawablesWithIntrinsicBounds(
                compoundDrawables[0] ?: null,
                compoundDrawables[1] ?: null,
                if (clearIcon != 0) context.getDrawable(clearIcon) else null,
                compoundDrawables[3] ?: null
            )
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    })
    setOnTouchListener(View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (this.right - this.compoundPaddingRight)) {
                this.setText("")
                hideKeyboard(this.context)
                this.clearFocus()
                return@OnTouchListener true
            }
        }
        return@OnTouchListener false
    })
}

@BindingAdapter("etOnEditorActionListener")
fun EditText?.setOnEditorActionListener(
    listener: OnOkInSoftKeyboardListener
) {
    this?.setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
        listener.onOkInSoftKeyboard(actionId)
        false
    }
}

@BindingAdapter(
    value = ["etChangeTVFocus", "etChangeTVFocusColor", "etChangeTVNotFocusColor"],
    requireAll = false
)
fun EditText?.setOnFocustChangeTitleFocus(
    @IdRes tvId: Int,
    @ColorRes focusColor: Int? = null,
    @ColorRes notFocusColor: Int? = null
) {

    val textView: TextView? = if ((this?.parentForAccessibility is View)) {
        (this.parentForAccessibility as View).findViewById(tvId)
    } else {
        (this?.parent as View).findViewById(tvId)
    }


    this.setOnFocusChangeListener { _, isFocus ->
        if (isFocus) {
            textView?.setTextColor(resources.getColor(focusColor ?: R.color.text_dark_color))
        } else {
            textView?.setTextColor(resources.getColor(notFocusColor ?: R.color.text_gray_color))
        }
    }
}


fun View.updateStrokeColor(inputFieldValidStateEnums: InputFieldValidStateEnums) {
    when(inputFieldValidStateEnums){
        InputFieldValidStateEnums.VALID -> {
            setBackgroundColor(R.color.border_color)
        }
        InputFieldValidStateEnums.ERROR -> {
            setBackgroundColor(R.color.input_error_color)
        }
    }
}

fun View.updateStrokeLightColor(inputFieldValidStateEnums: InputFieldValidStateEnums) {
    when(inputFieldValidStateEnums){
        InputFieldValidStateEnums.VALID -> {
            setBackgroundColor(R.color.border_color)
        }
        InputFieldValidStateEnums.ERROR -> {
            setBackgroundColor(R.color.input_error_color)
        }
    }
}

interface OnOkInSoftKeyboardListener {
    fun onOkInSoftKeyboard(actionId: Int)
}