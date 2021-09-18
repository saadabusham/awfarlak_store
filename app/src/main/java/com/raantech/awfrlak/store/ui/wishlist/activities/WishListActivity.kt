package com.raantech.awfrlak.store.ui.wishlist.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.paginate.Paginate
import com.paginate.recycler.LoadingListItemCreator
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.api.response.GeneralError
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.enums.CategoriesEnum
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.store.data.models.home.Service
import com.raantech.awfrlak.store.data.models.home.Store
import com.raantech.awfrlak.store.data.models.wishlist.WishList
import com.raantech.awfrlak.databinding.ActivityWishlistBinding
import com.raantech.awfrlak.store.ui.accessory.AccessoryDetailsActivity
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.mobile.MobileDetailsActivity
import com.raantech.awfrlak.store.ui.service.ServiceDetailsActivity
import com.raantech.awfrlak.store.ui.store.StoreActivity
import com.raantech.awfrlak.store.ui.wishlist.adapter.WishListRecyclerAdapter
import com.raantech.awfrlak.store.ui.wishlist.viewmodels.WishListViewModel
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class WishListActivity : BaseBindingActivity<ActivityWishlistBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: WishListViewModel by viewModels()
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isFinished = false

    var positionToUpdate: Int = -1
    lateinit var wishListRecyclerAdapter: WishListRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                layoutResID = R.layout.activity_wishlist,
                hasToolbar = true,
                toolbarView = toolbar,
                hasTitle = true,
                title = R.string.menu_favorites,
                hasBackButton = true,
                showBackArrow = true
        )
        setUpBinding()
        setUpListeners()
        setUpRecyclerView()
        loadData()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpRecyclerView() {
        wishListRecyclerAdapter = WishListRecyclerAdapter(this)
        binding?.recyclerView?.adapter = wishListRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        Paginate.with(binding?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loading.value == false && wishListRecyclerAdapter.itemCount > 0 && !isFinished) {
                    loadData()
                }
            }

            override fun isLoading(): Boolean {
                return loading.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isFinished
            }

        })
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(false)
                .setLoadingListItemCreator(object : LoadingListItemCreator {
                    override fun onCreateViewHolder(
                            parent: ViewGroup?,
                            viewType: Int
                    ): RecyclerView.ViewHolder {
                        val view = LayoutInflater.from(parent!!.context)
                                .inflate(R.layout.loading_row, parent, false)
                        return object : RecyclerView.ViewHolder(view) {}
                    }

                    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

                    }

                })
                .build()
    }

    private fun setUpListeners() {
    }

    private fun loadData() {
        viewModel.getWishList(
                wishListRecyclerAdapter.itemCount
        ).observe(this, wishlistObserver())
    }

    private fun wishlistObserver(): CustomObserverResponse<List<WishList>> {
        return CustomObserverResponse(
                this,
                object : CustomObserverResponse.APICallBack<List<WishList>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: ResponseWrapper<List<WishList>>?
                    ) {
                        isFinished = data?.body?.isNullOrEmpty() == true
                        data?.body?.let {
                            wishListRecyclerAdapter.addItems(it)
                        }
                        loading.postValue(false)
                        hideShowNoData()
                    }

                    override fun onError(
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            message: String,
                            errors: List<GeneralError>?
                    ) {
                        super.onError(subErrorCode, message, errors)
                        loading.postValue(false)
                        hideShowNoData()
                    }

                    override fun onLoading() {
                        loading.postValue(true)
                    }
                }, true, showError = false
        )
    }


    private fun hideShowNoData() {
        if (wishListRecyclerAdapter.itemCount == 0) {
            binding?.recyclerView?.gone()
            binding?.layoutNoData?.linearNoData?.visible()
        } else {
            binding?.layoutNoData?.linearNoData?.gone()
            binding?.recyclerView?.visible()
        }
    }

    private fun wishListActionObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
                this,
                object : CustomObserverResponse.APICallBack<Any> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: ResponseWrapper<Any>?
                    ) {
//                    wishListRecyclerAdapter.items[positionToUpdate].isWishlist =
//                        wishListRecyclerAdapter.items[positionToUpdate].isWishlist != true
//                    wishListRecyclerAdapter.notifyItemChanged(positionToUpdate)
//                    positionToUpdate = -1
                    }
                }, false
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        var type: String = CategoriesEnum.MOBILES.value
        var isWishList = false
        var id = 0
        when (item) {
            is MobilesItem -> {
                type = CategoriesEnum.MOBILES.value
                isWishList = item.isWishlist == false
                id = item.id ?: 0
            }
            is AccessoriesItem -> {
                type = CategoriesEnum.ACCESSORIES.value
                isWishList = item.isWishlist == false
                id = item.id ?: 0
            }
            is Service -> {
                type = CategoriesEnum.SERVICES.value
                isWishList = item.isWishlist == false
                id = item.id ?: 0
            }
            is Store -> {
                type = CategoriesEnum.STORES.value
                isWishList = item.isWishlist == false
                id = item.id ?: 0
            }
        }
        if (view?.id == R.id.imgFavorite) {
            positionToUpdate = position
            if (isWishList) {
                viewModel.removeFromWishList(type, id).observe(this, wishListActionObserver())
            } else {
                viewModel.addToWishList(
                        type,
                        id
                ).observe(this, wishListActionObserver())
            }
        } else {
            when (item) {
                is MobilesItem -> {
                    MobileDetailsActivity.start(this, item)
                }
                is AccessoriesItem -> {
                    AccessoryDetailsActivity.start(this, item)
                }
                is Store -> {
                    StoreActivity.start(this, item)
                }
                is Service -> {
                    ServiceDetailsActivity.start(this, item)
                }
            }
        }
    }

    companion object {
        fun start(
                context: Context?
        ) {
            val intent = Intent(context, WishListActivity::class.java)
            context?.startActivity(intent)
        }
    }

}