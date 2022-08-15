package com.raantech.awfrlak.store.ui.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.ActivityMobileDetailsBinding
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.store.ui.auth.login.adapters.IndecatorRecyclerAdapter
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.main.MainActivity
import com.raantech.awfrlak.store.ui.mobile.viewmodels.MobileViewModel
import com.raantech.awfrlak.store.ui.main.adapters.StoreImagesAdapter
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.invisible
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MobileDetailsActivity : BaseBindingActivity<ActivityMobileDetailsBinding>() {

    lateinit var indicatorRecyclerAdapter: IndecatorRecyclerAdapter
    private var indicatorPosition = 0
    lateinit var storeImagesAdapter: StoreImagesAdapter

    val viewModel: MobileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.mobileToView =
            intent.getSerializableExtra(Constants.BundleData.MOBILE) as MobilesItem?
        setContentView(
            R.layout.activity_mobile_details,
            hasToolbar = true,
            toolbarView = binding?.layoutToolbar?.toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            titleString = viewModel.mobileToView?.name
        )
        if (!intent.getBooleanExtra(Constants.BundleData.VIEW_SUBMIT, false))
            binding?.btnSubmit?.gone()
        setUpBinding()
        initData()
        setUpListeners()
        setUpPager()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun initData() {

    }

    private fun setUpListeners() {
        binding?.layoutMobileSlider?.imgBack?.setOnClickListener {
            binding?.layoutMobileSlider?.vpPictures?.currentItem?.minus(1)
                ?.let { it1 -> binding?.layoutMobileSlider?.vpPictures?.setCurrentItem(it1, true) }
        }
        binding?.layoutMobileSlider?.imgNext?.setOnClickListener {
            binding?.layoutMobileSlider?.vpPictures?.currentItem?.plus(1)
                ?.let { it1 -> binding?.layoutMobileSlider?.vpPictures?.setCurrentItem(it1, true) }
        }
        binding?.btnSubmit?.setOnClickListener {
            if (!intent.getBooleanExtra(Constants.BundleData.UPDATE, false)) {
                viewModel.addMobile(viewModel.buildMobile())
                    .observe(this, mobileTypeResultObserver())
            } else {
                viewModel.updateMobile(viewModel.buildMobile())
                    .observe(this, mobileTypeResultObserver())
            }
        }
        binding?.btnEdit?.setOnClickListener {
            if (!intent.getBooleanExtra(Constants.BundleData.UPDATE, false))
                finish()
            else {
                AddMobileActivity.start(
                    this,
                    mobilesItem = viewModel.mobileToView,
                    intent.getBooleanExtra(Constants.BundleData.UPDATE,false)
                )
            }
        }
    }

    private fun mobileTypeResultObserver(): CustomObserverResponse<MobilesItem> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<MobilesItem> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: MobilesItem?
                ) {
                    MainActivity.start(this@MobileDetailsActivity)
                }
            })
    }

    private fun setUpPager() {
        storeImagesAdapter = StoreImagesAdapter(this)
        binding?.layoutMobileSlider?.vpPictures?.adapter =
            storeImagesAdapter.apply {
                viewModel.mobileToView?.additionalImages?.map {
                    it.url ?: ""
                }?.let { submitItems(it) }
            }
        showImageNext()
        setUpIndicator()
    }

    private fun setUpIndicator() {
        indicatorRecyclerAdapter = IndecatorRecyclerAdapter(this)
        binding?.layoutMobileSlider?.recyclerViewImagesDot?.adapter = indicatorRecyclerAdapter
        storeImagesAdapter.items.let {
            it.withIndex().forEach {
                indicatorRecyclerAdapter.submitItem(it.index == 0)
            }
        }
        binding?.layoutMobileSlider?.vpPictures?.registerOnPageChangeCallback(
            pagerCallback
        )
    }

    private var pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            updateIndicator(position)
        }
    }

    private fun updateIndicator(position: Int) {
        indicatorRecyclerAdapter.items[indicatorPosition] = false
        indicatorRecyclerAdapter.items[position] = true
        indicatorRecyclerAdapter.notifyDataSetChanged()
        indicatorPosition = position
        binding?.layoutMobileSlider?.apply {
            when (position) {
                0 -> {
                    this.imgBack.invisible()
                    showImageNext()
                }
                storeImagesAdapter.itemCount - 1 -> {
                    this.imgNext.invisible()
                    showImageBack()
                }
                else -> {
                    this.imgBack.visible()
                    this.imgNext.visible()
                }
            }
        }
    }

    private fun showImageNext() {
        if (storeImagesAdapter.itemCount > 1) {
            binding?.layoutMobileSlider?.imgNext?.visible()
        }
    }

    private fun showImageBack() {
        if (storeImagesAdapter.itemCount > 1) {
            binding?.layoutMobileSlider?.imgBack?.visible()
        }
    }

    private fun wishListObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ResponseWrapper<Any>?
                ) {

                }
            }, false, showError = false
        )
    }


    companion object {
        fun start(
            context: Activity,
            item: MobilesItem,
            update: Boolean = false,
            viewSubmit: Boolean = false
        ) {
            val intent = Intent(context, MobileDetailsActivity::class.java)
            intent.putExtra(Constants.BundleData.MOBILE, item)
            intent.putExtra(Constants.BundleData.UPDATE, update)
            intent.putExtra(Constants.BundleData.VIEW_SUBMIT, viewSubmit)
            context.startActivity(intent)
        }
    }

}