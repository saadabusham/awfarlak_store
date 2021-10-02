package com.raantech.awfrlak.store.ui.main.orders.fragment

import android.view.View
import androidx.fragment.app.viewModels
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.FragmentOrderBinding
import com.raantech.awfrlak.store.data.enums.PurchaseStatusEnum
import com.raantech.awfrlak.store.data.models.Order
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.fragment.BaseBindingFragment
import com.raantech.awfrlak.store.ui.main.orders.adapter.OrdersRecyclerAdapter
import com.raantech.awfrlak.store.ui.main.orders.viewmodels.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseBindingFragment<FragmentOrderBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_order

    private val viewModel: OrdersViewModel by viewModels()
    lateinit var adapter: OrdersRecyclerAdapter

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
        setUpAdapter()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {

    }

    private fun setUpAdapter() {
        adapter = OrdersRecyclerAdapter(requireContext())
        binding?.recyclerView?.adapter = adapter
        adapter.submitItems(
                arrayListOf(
                        Order(
                                status = PurchaseStatusEnum.IN_THE_WAY.value
                        ),Order(
                                status = PurchaseStatusEnum.IN_THE_WAY.value
                        ),Order(
                                status = PurchaseStatusEnum.IN_THE_WAY.value
                        ),Order(
                                status = PurchaseStatusEnum.IN_THE_WAY.value
                        ),Order(
                                status = PurchaseStatusEnum.IN_THE_WAY.value
                        )
                )
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        when (item) {

        }
    }

}