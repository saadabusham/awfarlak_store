package com.raantech.awfrlak.store.ui.main.statistics

import androidx.fragment.app.viewModels
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.FragmentStatisticsBinding
import com.raantech.awfrlak.store.data.models.home.*
import com.raantech.awfrlak.store.ui.base.fragment.BaseBindingFragment
import com.raantech.awfrlak.store.ui.main.home.adapters.*
import com.raantech.awfrlak.store.ui.main.viewmodels.GeneralViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticsFragment : BaseBindingFragment<FragmentStatisticsBinding>() {

    private val viewModel: GeneralViewModel by viewModels()
    override fun getLayoutId(): Int = R.layout.fragment_statistics

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()

    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }


}