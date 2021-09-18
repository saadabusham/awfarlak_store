package com.raantech.awfrlak.store.ui.service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.enums.CategoriesEnum
import com.raantech.awfrlak.store.data.models.home.Service
import com.raantech.awfrlak.databinding.ActivityServiceDetailsBinding
import com.raantech.awfrlak.store.ui.auth.login.adapters.IndecatorRecyclerAdapter
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.cart.CartActivity
import com.raantech.awfrlak.store.ui.main.viewmodels.GeneralViewModel
import com.raantech.awfrlak.store.ui.store.adapters.StoreImagesAdapter
import com.raantech.awfrlak.store.utils.extensions.invisible
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ServiceDetailsActivity : BaseBindingActivity<ActivityServiceDetailsBinding>() {

    lateinit var indicatorRecyclerAdapter: IndecatorRecyclerAdapter
    private var indicatorPosition = 0
    lateinit var storeImagesAdapter: StoreImagesAdapter

    val viewModel: GeneralViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.serviceToView = intent.getSerializableExtra(Constants.BundleData.SERVICE) as Service?
        setContentView(
                R.layout.activity_service_details,
                hasToolbar = true,
                toolbarView = binding?.toolbar?.toolbar,
                hasBackButton = true,
                showBackArrow = true,
                hasTitle = true,
                titleString = viewModel.serviceToView?.name,
                hasSubTitle = true,
                subTitle = viewModel.serviceToView?.store?.name ?: ""
        )
        setUpBinding()
        initData()
        setUpListeners()
        setUpPager()
    }

    private fun setUpBinding() {
        binding?.toolbar?.viewModel = viewModel
        binding?.viewModel = viewModel
        viewModel.getCartsCount()
        updateFavorite()
    }

    private fun initData() {
//        viewModel.store =
//                intent.getSerializableExtra(Constants.BundleData.STORE) as Store
//        binding?.layoutSpecialistInfo?.data = viewModel.store
    }


    private fun setUpListeners() {
        binding?.toolbar?.imgCart?.setOnClickListener {
            if (!viewModel.cartCount.value.equals("0"))
                CartActivity.start(this)
        }
        binding?.layoutServiceSlider?.imgFavorite?.setOnClickListener {
            viewModel.serviceToView?.isWishlist = viewModel.serviceToView?.isWishlist == false
            updateFavorite()
            if (viewModel.serviceToView?.isWishlist == true) {
                viewModel.addToWishList(CategoriesEnum.SERVICES.value,viewModel.serviceToView?.id
                        ?: 0).observe(this, wishListObserver())
            } else {
                viewModel.removeFromWishList(CategoriesEnum.SERVICES.value,viewModel.serviceToView?.id
                        ?: 0).observe(this, wishListObserver())
            }
        }
        binding?.layoutServiceSlider?.imgBack?.setOnClickListener {
            binding?.layoutServiceSlider?.vpPictures?.currentItem?.minus(1)?.let { it1 -> binding?.layoutServiceSlider?.vpPictures?.setCurrentItem(it1, true) }
        }
        binding?.layoutServiceSlider?.imgNext?.setOnClickListener {
            binding?.layoutServiceSlider?.vpPictures?.currentItem?.plus(1)?.let { it1 -> binding?.layoutServiceSlider?.vpPictures?.setCurrentItem(it1, true) }
        }
    }

    private fun updateFavorite() {
        binding?.layoutServiceSlider?.favorite = viewModel.serviceToView?.isWishlist
    }

    private fun setUpPager() {
        storeImagesAdapter = StoreImagesAdapter(this)
        binding?.layoutServiceSlider?.vpPictures?.adapter =
                storeImagesAdapter.apply {
                    submitItem(viewModel.serviceToView?.logo?.url ?: "") }
//        binding?.layoutServiceSlider?.vpPictures?.isUserInputEnabled = false
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
                item: Service,
//                profileImage: View,
//                profileName: View,
//                profileRate: View,
//                profilePriceRange: View
        ) {

//            val p1: androidx.core.util.Pair<View, String> = androidx.core.util.Pair(
//                    profileImage,
//                    profileImage.transitionName
//            )
//            val p2: androidx.core.util.Pair<View, String> = androidx.core.util.Pair(
//                    profileName,
//                    profileName.transitionName
//            )
//            val p3: androidx.core.util.Pair<View, String> = androidx.core.util.Pair(
//                    profileRate,
//                    profileRate.transitionName
//            )
//            val p4: androidx.core.util.Pair<View, String> = androidx.core.util.Pair(
//                    profilePriceRange,
//                    profilePriceRange.transitionName
//            )
//            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                    context,
//                    p1,
//                    p2,
//                    p3,
//                    p4
//            )
            val intent = Intent(context, ServiceDetailsActivity::class.java)
            intent.putExtra(Constants.BundleData.SERVICE, item)
            context.startActivity(intent /*options.toBundle()*/)
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