package com.raantech.awfrlak.store.ui.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.ActivityServiceDetailsBinding
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.home.Service
import com.raantech.awfrlak.store.ui.auth.login.adapters.IndecatorRecyclerAdapter
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.main.MainActivity
import com.raantech.awfrlak.store.ui.service.viewmodels.ServiceViewModel
import com.raantech.awfrlak.store.ui.store.adapters.StoreImagesAdapter
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.invisible
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ServiceDetailsActivity : BaseBindingActivity<ActivityServiceDetailsBinding>() {

    lateinit var indicatorRecyclerAdapter: IndecatorRecyclerAdapter
    private var indicatorPosition = 0
    lateinit var storeImagesAdapter: StoreImagesAdapter

    val viewModel: ServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.serviceToView = intent.getSerializableExtra(Constants.BundleData.SERVICE) as Service?
        setContentView(
                R.layout.activity_service_details,
                hasToolbar = true,
                toolbarView = binding?.layoutToolbar?.toolbar,
                hasBackButton = true,
                showBackArrow = true,
                hasTitle = true,
                titleString = viewModel.serviceToView?.name,
                hasSubTitle = true,
                subTitle = viewModel.serviceToView?.store?.name ?: ""
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
        binding?.layoutServiceSlider?.imgBack?.setOnClickListener {
            binding?.layoutServiceSlider?.vpPictures?.currentItem?.minus(1)?.let { it1 -> binding?.layoutServiceSlider?.vpPictures?.setCurrentItem(it1, true) }
        }
        binding?.layoutServiceSlider?.imgNext?.setOnClickListener {
            binding?.layoutServiceSlider?.vpPictures?.currentItem?.plus(1)?.let { it1 -> binding?.layoutServiceSlider?.vpPictures?.setCurrentItem(it1, true) }
        }
        binding?.btnSubmit?.setOnClickListener {
            if (!intent.getBooleanExtra(Constants.BundleData.UPDATE, false)) {
                viewModel.addService(viewModel.buildService())
                        .observe(this, serviceResultObserver())
            } else {
                viewModel.updateService(viewModel.buildService())
                        .observe(this, serviceResultObserver())
            }
        }
        binding?.btnEdit?.setOnClickListener {
            if (!intent.getBooleanExtra(Constants.BundleData.UPDATE, false))
                finish()
            else {
                AddServiceActivity.start(
                        this,
                        service = viewModel.serviceToView,
                        intent.getBooleanExtra(Constants.BundleData.UPDATE, false)
                )
            }
        }
    }


    private fun serviceResultObserver(): CustomObserverResponse<Service> {
        return CustomObserverResponse(
                this,
                object : CustomObserverResponse.APICallBack<Service> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: Service?
                    ) {
                        MainActivity.start(this@ServiceDetailsActivity)
                    }
                })
    }

    private fun setUpPager() {
        storeImagesAdapter = StoreImagesAdapter(this)
        binding?.layoutServiceSlider?.vpPictures?.adapter =
                storeImagesAdapter.apply {
                    viewModel.serviceToView?.additionalImages?.map {
                        it.url ?: ""
                    }?.let { submitItems(it) }
                }
        showImageNext()
        setUpIndicator()
    }

    private fun setUpIndicator() {
        indicatorRecyclerAdapter = IndecatorRecyclerAdapter(this)
        binding?.layoutServiceSlider?.recyclerViewImagesDot?.adapter = indicatorRecyclerAdapter
        storeImagesAdapter.items.let {
            it.withIndex().forEach {
                indicatorRecyclerAdapter.submitItem(it.index == 0)
            }
        }
        binding?.layoutServiceSlider?.vpPictures?.registerOnPageChangeCallback(
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
        binding?.layoutServiceSlider?.apply {
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
            binding?.layoutServiceSlider?.imgNext?.visible()
        }
    }

    private fun showImageBack() {
        if (storeImagesAdapter.itemCount > 1) {
            binding?.layoutServiceSlider?.imgBack?.visible()
        }
    }

    companion object {
        fun start(
                context: Activity,
                item: Service,
                update: Boolean = false,
                viewSubmit: Boolean = false
        ) {

            val intent = Intent(context, ServiceDetailsActivity::class.java)
            intent.putExtra(Constants.BundleData.SERVICE, item)
            intent.putExtra(Constants.BundleData.UPDATE, update)
            intent.putExtra(Constants.BundleData.VIEW_SUBMIT, viewSubmit)
            context.startActivity(intent)
        }

        fun start(
                context: Activity,
                storeId: String
        ) {
            val intent = Intent(context, ServiceDetailsActivity::class.java)
            intent.putExtra(Constants.BundleData.STORE_ID, storeId)
            context.startActivity(intent)
        }


    }

}