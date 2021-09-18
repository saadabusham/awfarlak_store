package com.raantech.awfrlak.store.ui.base.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable


abstract class BaseViewModel : ViewModel(){


    var compositeDisposable: CompositeDisposable? = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable?.dispose()
        compositeDisposable = null
    }

}