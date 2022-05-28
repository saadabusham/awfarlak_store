package com.raantech.awfrlak.store.ui.store.fragment

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import com.paginate.Paginate
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.LayoutServicesGridBinding
import com.raantech.awfrlak.store.data.api.response.GeneralError
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.home.Service
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.base.fragment.BaseBindingFragment
import com.raantech.awfrlak.store.ui.main.home.adapters.ServicesGridRecyclerAdapter
import com.raantech.awfrlak.store.ui.main.viewmodels.GeneralViewModel
import com.raantech.awfrlak.store.ui.service.ServiceDetailsActivity
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ServicesFragment : BaseBindingFragment<LayoutServicesGridBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: GeneralViewModel by activityViewModels()

    private val loadingServices: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isServicesFinished = false
    lateinit var servicesGridRecyclerAdapter: ServicesGridRecyclerAdapter

    override fun getLayoutId(): Int {
        return R.layout.layout_services_grid
    }

    override fun onViewVisible() {
        super.onViewVisible()
        loadingServicesObserver()
        setUpRvServicesGrid()
        loadServices()
    }

    private fun loadingServicesObserver() {
        loadingServices.observe(this, {
            if (it) {
                binding?.layoutShimmer?.shimmerViewContainer?.visible()
            } else {
                binding?.layoutShimmer?.shimmerViewContainer?.gone()
            }
        })
    }

    private fun setUpRvServicesGrid() {
        servicesGridRecyclerAdapter = ServicesGridRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = servicesGridRecyclerAdapter
        binding?.recyclerView.setOnItemClickListener(this)
        Paginate.with(binding?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loadingServices.value == false && servicesGridRecyclerAdapter.itemCount > 0 && !isServicesFinished) {
                    loadServices()
                }
            }

            override fun isLoading(): Boolean {
                return loadingServices.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isServicesFinished
            }

        })
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(false)
                .build()
    }

    private fun loadServices() {
        viewModel.getServices(servicesGridRecyclerAdapter.itemCount)
                .observe(this, servicesObserver())
    }

    private fun servicesObserver(): CustomObserverResponse<List<Service>> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<List<Service>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: List<Service>?
                    ) {
                        data?.let {
                            servicesGridRecyclerAdapter.submitItems(it)
                        }
                        if (data.isNullOrEmpty())
                            isServicesFinished = true
                        loadingServices.postValue(false)
                    }

                    override fun onError(
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            message: String,
                            errors: List<GeneralError>?
                    ) {
                        super.onError(subErrorCode, message, errors)
                        loadingServices.postValue(false)
                        hideShowNoData()
                    }

                    override fun onLoading() {
                        loadingServices.value = true
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
        ServiceDetailsActivity.start(
                requireActivity(), item as Service,
                update = true,
                viewSubmit = false
        )
    }


}