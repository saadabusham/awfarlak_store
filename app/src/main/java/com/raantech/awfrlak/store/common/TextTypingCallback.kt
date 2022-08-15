package com.raantech.awfrlak.store.common

import android.text.Editable
import android.text.TextWatcher

interface TextTypingCallback : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        textChanging(s.toString())
    }

    override fun afterTextChanged(s: Editable?) {
        textChanged(s.toString())
    }

    fun textChanging(text :String){}
    fun textChanged(text :String){}
}