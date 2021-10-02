package com.raantech.awfrlak.store.ui.accessory

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.ActivityAccessoryDetailsBinding
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.ui.accessory.viewmodels.AccessoryViewModel
import com.raantech.awfrlak.store.ui.auth.login.adapters.IndecatorRecyclerAdapter
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.main.MainActivity
import com.raantech.awfrlak.store.ui.store.adapters.StoreImagesAdapter
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.invisible
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccessoryDetailsActivity : BaseBindingActivity<ActivityAccessoryDetailsBinding>() {

    lateinit var indicatorRecyclerAdapter: IndecatorRecyclerAdapter
    private var indicatorPosition = 0
    lateinit var storeImagesAdapter: StoreImagesAdapter

    val viewModel: AccessoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.accessoryToView = intent.getSerializableExtra(Constants.BundleData.ACCESSORY) as AccessoriesItem?
        setContentView(
                R.layout.activity_accessory_details,
                hasToolbar = true,
                toolbarView = binding?.layoutToolbar?.toolbar,
                hasBackButton = true,
                showBackArrow = true,
                hasTitle = true,
                titleString = viewModel.accessoryToView?.name,
                hasSubTitle = true,
                subTitle = viewModel.accessoryToView?.store?.name ?: ""
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
        updateFavorite()
    }

    private fun initData() {
//        viewModel.store =
//                intent.getSerializableExtra(Constants.BundleData.STORE) as Store
//        binding?.layoutSpecialistInfo?.data = viewModel.store
    }


    private fun setUpListeners() {
        binding?.layoutAccessoriesSlider?.imgBack?.setOnClickListener {
            binding?.layoutAccessoriesSlider?.vpPictures?.currentItem?.minus(1)?.let { it1 -> binding?.layoutAccessoriesSlider?.vpPictures?.setCurrentItem(it1, true) }
        }
        binding?.layoutAccessoriesSlider?.imgNext?.setOnClickListener {
            binding?.layoutAccessoriesSlider?.vpPictures?.currentItem?.plus(1)?.let { it1 -> binding?.layoutAccessoriesSlider?.vpPictures?.setCurrentItem(it1, true) }
        }
        binding?.btnSubmit?.setOnClickListener {
            if (!intent.getBooleanExtra(Constants.BundleData.UPDATE, false)) {
                viewModel.addAccessory(viewModel.buildAccessory())
                        .observe(this, accessoryResultObserver())
            } else {
                viewModel.updateAccessory(viewModel.buildAccessory())
                        .observe(this, accessoryResultObserver())
            }
        }
        binding?.btnEdit?.setOnClickListener {
            if (!intent.getBooleanExtra(Constants.BundleData.UPDATE, false))
                finish()
            else {
                AddAccessoryActivity.start(
                        this,
                        accessoriesItem = viewModel.accessoryToView,
                        intent.getBooleanExtra(Constants.BundleData.UPDATE, false)
                )
            }
        }
    }


    private fun accessoryResultObserver(): CustomObserverResponse<AccessoriesItem> {
        return CustomObserverResponse(
                this,
                object : CustomObserverResponse.APICallBack<AccessoriesItem> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: AccessoriesItem?
                    ) {
                        MainActivity.start(this@AccessoryDetailsActivity)
                    }
                })
    }

    private fun updateFavorite() {
        binding?.layoutAccessoriesSlider?.favorite = viewModel.accessoryToView?.isWishlist
    }

    private fun setUpPager() {
        storeImagesAdapter = StoreImagesAdapter(this)
        binding?.layoutAccessoriesSlider?.vpPictures?.adapter =
                storeImagesAdapter.apply {
                    viewModel.accessoryToView?.additionalImages?.map {
                        it.url ?: ""
                    }?.let { submitItems(it) }
                }
        showImageNext()
        setUpIndicator()
    }

    private fun setUpIndicator() {
        indicatorRecyclerAdapter = IndecatorRecyclerAdapter(this)
        binding?.layoutAccessoriesSlider?.recyclerViewImagesDot?.adapter = indicatorRecyclerAdapter
        storeImagesAdapter.items.let {
            it.withIndex().forEach {
                indicatorRecyclerAdapter.submitItem(it.index == 0)
            }
        }
        binding?.layoutAccessoriesSlider?.vpPictures?.registerOnPageChangeCallback(
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
        binding?.layoutAccessoriesSlider?.apply {
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
            binding?.layoutAccessoriesSlider?.imgNext?.visible()
        }
    }

    private fun showImageBack() {
        if (storeImagesAdapter.itemCount > 1) {
            binding?.layoutAccessoriesSlider?.imgBack?.visible()
        }
    }

    companion object {
        fun start(
                context: Activity,
                item: AccessoriesItem,
                update: Boolean = false,
                viewSubmit: Boolean = false
        ) {
            val intent = Intent(context, AccessoryDetailsActivity::class.java)
            intent.putExtra(Constants.BundleData.ACCESSORY, item)
            intent.putExtra(Constants.BundleData.UPDATE, update)
            intent.putExtra(Constants.BundleData.VIEW_SUBMIT, viewSubmit)
            context.startActivity(intent)
        }

        fun start(
                context: Activity,
                storeId: String
        ) {
            val intent = Intent(context, AccessoryDetailsActivity::class.java)
            intent.putExtra(Constants.BundleData.STORE_ID, storeId)
            context.startActivity(intent)
        }


    }

}