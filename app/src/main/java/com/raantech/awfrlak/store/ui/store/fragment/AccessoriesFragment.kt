package com.raantech.awfrlak.store.ui.store.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.paginate.Paginate
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.api.response.GeneralError
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.databinding.LayoutAccessoriesGridBinding
import com.raantech.awfrlak.store.ui.accessory.AccessoryDetailsActivity
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.base.fragment.BaseBindingFragment
import com.raantech.awfrlak.store.ui.main.home.adapters.AccessoriesGridRecyclerAdapter
import com.raantech.awfrlak.store.ui.main.viewmodels.GeneralViewModel
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccessoriesFragment : BaseBindingFragment<LayoutAccessoriesGridBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: GeneralViewModel by activityViewModels()

    private val loadingAccessories: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isAccessoriesFinished = false
    lateinit var accessoriesGridRecyclerAdapter: AccessoriesGridRecyclerAdapter
    override fun getLayoutId(): Int {
        return R.layout.layout_accessories_grid
    }

    override fun onViewVisible() {
        super.onViewVisible()
        binding?.linearAccessoriesGrid?.visible()
        loadingAccessoriesObserver()
        setUpRvAccessoriesGrid()
        loadAccessories()
    }

    private fun loadingAccessoriesObserver() {
        loadingAccessories.observe(this, {
            if (it) {
                binding?.layoutShimmer?.shimmerViewContainer?.visible()
            } else {
                binding?.layoutShimmer?.shimmerViewContainer?.gone()
            }
        })
    }

    private fun setUpRvAccessoriesGrid() {
        accessoriesGridRecyclerAdapter = AccessoriesGridRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = accessoriesGridRecyclerAdapter
        binding?.recyclerView.setOnItemClickListener(this)
        Paginate.with(binding?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loadingAccessories.value == false && accessoriesGridRecyclerAdapter.itemCount > 0 && !isAccessoriesFinished) {
                    loadAccessories()
                }
            }

            override fun isLoading(): Boolean {
                return loadingAccessories.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isAccessoriesFinished
            }

        })
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(false)
                .build()
    }

    private fun loadAccessories() {
        viewModel.getAccessories(accessoriesGridRecyclerAdapter.itemCount)
                .observe(this, accessoriesObserver())
    }

    private fun accessoriesObserver(): CustomObserverResponse<List<AccessoriesItem>> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<List<AccessoriesItem>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: List<AccessoriesItem>?
                    ) {
                        data?.let {
                            accessoriesGridRecyclerAdapter.submitItems(it)
                        }
                        if (data.isNullOrEmpty())
                            isAccessoriesFinished = true
                        loadingAccessories.postValue(false)
                    }

                    override fun onError(
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            message: String,
                            errors: List<GeneralError>?
                    ) {
                        super.onError(subErrorCode, message, errors)
                        loadingAccessories.postValue(false)
                        hideShowNoData()
                    }

                    override fun onLoading() {
                        loadingAccessories.postValue(true)
                    }
                }, false, showError = false
        )
    }

    private fun hideShowNoData() {
//        if (accessoriesRecyclerAdapter.itemCount == 0) {
//            binding?.recyclerView?.gone()
//            binding?.layoutNoData?.linearNoData?.visible()
//        } else {
//            binding?.layoutNoData?.linearNoData?.gone()
//            binding?.recyclerView?.visible()
//        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        AccessoryDetailsActivity.start(requireActivity(), item as AccessoriesItem)
    }


}