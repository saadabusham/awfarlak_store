package com.raantech.awfrlak.store.ui.main.statistics

import android.transition.TransitionManager
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.FragmentStatisticsBinding
import com.raantech.awfrlak.store.ui.accessory.AddAccessoryActivity
import com.raantech.awfrlak.store.ui.base.fragment.BaseBindingFragment
import com.raantech.awfrlak.store.ui.main.viewmodels.GeneralViewModel
import com.raantech.awfrlak.store.ui.mobile.AddMobileActivity
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BaseBindingFragment<FragmentStatisticsBinding>() {

    private val viewModel: GeneralViewModel by viewModels()
    override fun getLayoutId(): Int = R.layout.fragment_statistics

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
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
            AddMobileActivity.start(requireContext(),null,false)
        }
        binding?.tvAddServices?.setOnClickListener {
            binding?.cvAdd?.callOnClick()
        }
        binding?.tvAddAccessories?.setOnClickListener {
            binding?.cvAdd?.callOnClick()
            AddAccessoryActivity.start(requireContext(),null,false)
        }
    }

}