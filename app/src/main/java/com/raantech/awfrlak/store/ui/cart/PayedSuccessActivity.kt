package com.raantech.awfrlak.store.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.ActivityPayedSuccessBinding
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.cart.viewmodels.CartViewModel
import com.raantech.awfrlak.store.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class PayedSuccessActivity : BaseBindingActivity<ActivityPayedSuccessBinding>() {

    private val viewModel: CartViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            layoutResID = R.layout.activity_payed_success,
            hasToolbar = true,
            toolbarView = toolbar,
            hasTitle = true,
            title = R.string.purchas_completed,
            hasBackButton = true,
            showBackArrow = true
        )
        setUpBinding()
        setUpViewsListeners()
    }

    private fun setUpBinding() {
//        binding?.viewModel = viewModel
    }

    private fun setUpViewsListeners() {
        binding?.btnDone?.setOnClickListener {
            MainActivity.start(this)
        }
    }

    companion object {
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, PayedSuccessActivity::class.java)
            context?.startActivity(intent)
        }
    }

}