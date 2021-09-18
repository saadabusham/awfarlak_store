package com.raantech.awfrlak.store.ui.store

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.enums.CategoriesEnum
import com.raantech.awfrlak.store.data.models.home.Store
import com.raantech.awfrlak.databinding.ActivityStoreBinding
import com.raantech.awfrlak.store.ui.auth.login.adapters.IndecatorRecyclerAdapter
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.cart.CartActivity
import com.raantech.awfrlak.store.ui.main.viewmodels.GeneralViewModel
import com.raantech.awfrlak.store.ui.store.adapters.StoreImagesAdapter
import com.raantech.awfrlak.store.ui.store.fragment.AccessoriesFragment
import com.raantech.awfrlak.store.ui.store.fragment.MobilesFragment
import com.raantech.awfrlak.store.ui.store.fragment.ServicesFragment
import com.raantech.awfrlak.store.utils.extensions.getStatusBarHeight
import com.raantech.awfrlak.store.utils.extensions.invisible
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_store.*


@AndroidEntryPoint
class StoreActivity : BaseBindingActivity<ActivityStoreBinding>() {

    lateinit var indicatorRecyclerAdapter: IndecatorRecyclerAdapter
    private var indicatorPosition = 0
    lateinit var storeImagesAdapter: StoreImagesAdapter

    val viewModel: GeneralViewModel by viewModels()
    private val mFragmentList = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.storeToView = intent.getSerializableExtra(Constants.BundleData.STORE) as Store?
        setContentView(
                R.layout.activity_store,
                hasToolbar = true,
                toolbarView = binding?.toolbar?.toolbar,
                hasBackButton = true,
                showBackArrow = true,
                hasTitle = true,
                titleString = viewModel.storeToView?.name
        )
//        binding?.viewModel = viewModel
        coordinator.setPadding(0, getStatusBarHeight() * -1, 0, 0)
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0)
        setUpBinding()
        initData()
        setUpListeners()
        setUpPager()
    }

    private fun setUpBinding() {
        binding?.toolbar?.viewModel = viewModel
        updateFavorite()
    }

    private fun initData() {
//        viewModel.store =
//                intent.getSerializableExtra(Constants.BundleData.STORE) as Store
//        binding?.layoutSpecialistInfo?.data = viewModel.store
        setUpTabLayout()
    }


    private fun setUpListeners() {
        binding?.toolbar?.imgCart?.setOnClickListener {
            if (!viewModel.cartCount.value.equals("0"))
                CartActivity.start(this)
        }
        binding?.layoutStoreSlider?.imgFavorite?.setOnClickListener {
            viewModel.storeToView?.isWishlist = viewModel.storeToView?.isWishlist == false
            updateFavorite()
            if (viewModel.storeToView?.isWishlist == true) {
                viewModel.addToWishList(CategoriesEnum.STORES.value,viewModel.storeToView?.id
                        ?: 0).observe(this, wishListObserver())
            } else {
                viewModel.removeFromWishList(CategoriesEnum.STORES.value,viewModel.storeToView?.id
                        ?: 0).observe(this, wishListObserver())
            }
        }

        binding?.layoutStoreSlider?.imgBack?.setOnClickListener {
            binding?.layoutStoreSlider?.vpPictures?.currentItem?.minus(1)?.let { it1 -> binding?.layoutStoreSlider?.vpPictures?.setCurrentItem(it1, true) }
        }
        binding?.layoutStoreSlider?.imgNext?.setOnClickListener {
            binding?.layoutStoreSlider?.vpPictures?.currentItem?.plus(1)?.let { it1 -> binding?.layoutStoreSlider?.vpPictures?.setCurrentItem(it1, true) }
        }
    }

    private fun updateFavorite() {
        binding?.layoutStoreSlider?.favorite = viewModel.storeToView?.isWishlist
    }

    private fun setUpPager() {
        storeImagesAdapter = StoreImagesAdapter(this)
        binding?.layoutStoreSlider?.vpPictures?.adapter =
                storeImagesAdapter.apply {
                    viewModel.storeToView?.additionalImages?.map { it.url?: "" }?.let { submitItems(it) }
                }
//        binding?.layoutStoreSlider?.vpPictures?.isUserInputEnabled = false
        showImageNext()
        setUpIndicator()
    }

    private fun setUpIndicator() {
        indicatorRecyclerAdapter = IndecatorRecyclerAdapter(this)
        binding?.layoutStoreSlider?.recyclerViewImagesDot?.adapter = indicatorRecyclerAdapter
        storeImagesAdapter.items.let {
            it.withIndex().forEach {
                indicatorRecyclerAdapter.submitItem(it.index == 0)
            }
        }
        binding?.layoutStoreSlider?.vpPictures?.registerOnPageChangeCallback(
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
        binding?.layoutStoreSlider?.apply {
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
            binding?.layoutStoreSlider?.imgNext?.visible()
        }
    }

    private fun showImageBack() {
        if (storeImagesAdapter.itemCount > 1) {
            binding?.layoutStoreSlider?.imgBack?.visible()
        }
    }

    fun setUpTabLayout() {
        setUpViewPager()
        tab_layout.addTab(tab_layout.newTab(), 0, true)
        tab_layout.addTab(tab_layout.newTab(), 1, false)
        tab_layout.addTab(tab_layout.newTab(), 2, false)
        tab_layout.setSelectedTabIndicatorColor(Color.parseColor("#00000000"))
        tab_layout.setSelectedTabIndicatorHeight(0)
        tab_layout.setTabTextColors(
                Color.parseColor("#000000"),
                Color.parseColor("#ffffff")
        )
        tab_layout.getTabAt(0)?.text = resources.getString(R.string.mobiles)
        tab_layout.getTabAt(1)?.text = resources.getString(R.string.accessories)
        tab_layout.getTabAt(2)?.text = resources.getString(R.string.services)

        tab_layout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }

    private fun setUpViewPager() {
        mFragmentList.add(MobilesFragment())
        mFragmentList.add(AccessoriesFragment())
        mFragmentList.add(ServicesFragment())
        val adapter: FragmentStatePagerAdapter =
                object : FragmentStatePagerAdapter(supportFragmentManager) {
                    override fun getItem(position: Int): Fragment {
                        return mFragmentList[position]
                    }

                    override fun getCount(): Int {
                        return mFragmentList.size
                    }
                }
        viewPager.adapter = adapter
//        val viewPagerFragmentAdapter = ViewPagerFragmentAdapter(supportFragmentManager,lifecycle)
//        viewPagerFragmentAdapter.addFragment(MobilesFragment())
//        viewPagerFragmentAdapter.addFragment(AccessoriesFragment())
//        viewPagerFragmentAdapter.addFragment(ServicesFragment())
//        viewPager.adapter = viewPagerFragmentAdapter
        viewPager.offscreenPageLimit = 5
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
                item: Store,
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
            val intent = Intent(context, StoreActivity::class.java)
            intent.putExtra(Constants.BundleData.STORE, item)
            context.startActivity(intent /*options.toBundle()*/)
        }

        fun start(
                context: Activity,
                storeId: String
        ) {
            val intent = Intent(context, StoreActivity::class.java)
            intent.putExtra(Constants.BundleData.STORE_ID, storeId)
            context.startActivity(intent)
        }


    }

}