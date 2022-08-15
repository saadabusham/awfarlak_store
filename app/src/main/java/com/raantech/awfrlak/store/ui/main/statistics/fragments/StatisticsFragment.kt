package com.raantech.awfrlak.store.ui.main.statistics.fragments

import android.transition.TransitionManager
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.FragmentStatisticsBinding
import com.raantech.awfrlak.store.data.api.response.GeneralError
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.StoreStatistics
import com.raantech.awfrlak.store.ui.accessory.AddAccessoryActivity
import com.raantech.awfrlak.store.ui.base.fragment.BaseBindingFragment
import com.raantech.awfrlak.store.ui.main.viewmodels.GeneralViewModel
import com.raantech.awfrlak.store.ui.mobile.AddMobileActivity
import com.raantech.awfrlak.store.ui.service.AddServiceActivity
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BaseBindingFragment<FragmentStatisticsBinding>() {

    private val viewModel: GeneralViewModel by viewModels()
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun getLayoutId(): Int = R.layout.fragment_statistics

    override fun onResume() {
        super.onResume()
        loadStatistics()
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        loadingObserver()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.cvAdd?.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding?.root as ViewGroup)
            viewModel.addSelected.value = viewModel.addSelected.value == false
            if (viewModel.addSelected.value == true) {
                binding?.cvAddOptions?.visible()
            } else {
                binding?.cvAddOptions?.gone()
            }
        }
        binding?.tvAddMobile?.setOnClickListener {
            binding?.cvAdd?.callOnClick()
            AddMobileActivity.start(requireContext(), null, false)
        }
        binding?.tvAddServices?.setOnClickListener {
            binding?.cvAdd?.callOnClick()
            AddServiceActivity.start(requireContext(), null, false)
        }
        binding?.tvAddAccessories?.setOnClickListener {
            binding?.cvAdd?.callOnClick()
            AddAccessoryActivity.start(requireContext(), null, false)
        }
    }

    private fun loadStatistics() {
        viewModel.getStatistics().observe(this, statisticsResultObserver())
    }

    private fun statisticsResultObserver(): CustomObserverResponse<StoreStatistics> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<StoreStatistics> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: StoreStatistics?
                ) {
                    data?.let {
                        viewModel.storeStatistics.value = it
                        viewModel.isStatisticsEmpty.value = it.isTheDataEmpty()
                    }
                    loading.value = false
                }

                override fun onError(
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    message: String,
                    errors: List<GeneralError>?
                ) {
                    loading.value = false
                }

                override fun onLoading() {
                    super.onLoading()
                    loading.value = true
                }
            }, withProgress = false
        )
    }

    private fun loadingObserver() {
        loading.observe(this) {
            if (it) {
                binding?.layoutShimmer?.shimmerViewContainer?.visible()
            } else {
                binding?.layoutShimmer?.shimmerViewContainer?.gone()
            }
        }
    }

}