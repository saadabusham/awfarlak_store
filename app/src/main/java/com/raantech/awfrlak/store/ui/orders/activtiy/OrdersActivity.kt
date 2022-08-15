package com.raantech.awfrlak.store.ui.orders.activtiy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.ActivityOrdersItemProductsBinding
import com.raantech.awfrlak.store.data.api.response.GeneralError
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.orders.OrdersItem
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.orders.viewmodels.OrdersViewModel
import com.raantech.awfrlak.store.ui.orders.adapter.OrderItemProductsRecyclerAdapter
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.openUrl
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class OrdersActivity : BaseBindingActivity<ActivityOrdersItemProductsBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    lateinit var adapter: OrderItemProductsRecyclerAdapter
    private val viewModel: OrdersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntentData()
        setContentView(
            R.layout.activity_orders_item_products,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            titleString = "#${viewModel.orderIdToView}"
        )
        binding?.viewModel = viewModel
        setUpListeners()
        setUpAdapter()
    }

    private fun handleIntentData() {
        viewModel.orderIdToView = intent.getStringExtra(Constants.BundleData.ORDER_ID)
    }

    private fun setUpListeners() {
        binding?.btnParcel?.setOnClickListener {
            viewModel.orderIdToView?.let {
                viewModel.parcelOrder(it).observe(this, parcelOrdersObserver())
            }
        }
        binding?.btnTrace?.setOnClickListener {
            viewModel.orderItemToView.value?.trackingUrl?.let { it1 -> openUrl(it1) }
        }
        binding?.btnBill?.setOnClickListener {
            viewModel.orderItemToView.value?.waybillUrl?.let { it1 -> openUrl(it1) }
        }
    }

    private fun parcelOrdersObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {

                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    loadData()
                }
            }
        )
    }

    private fun loadData() {
        viewModel.orderIdToView?.let {
            viewModel.getOrderDetails(it).observe(this, ordersObserver())
        }
    }

    private fun ordersObserver(): CustomObserverResponse<OrdersItem> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<OrdersItem> {

                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: OrdersItem?
                ) {
                    data?.let {
                        viewModel.orderItemToView.value = it
                        it.prodcuts?.let {
                            adapter.clear()
                            adapter.submitItems(it)
                        }
                    }
                    hideShowNoData()
                }

                override fun onError(
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    message: String,
                    errors: List<GeneralError>?
                ) {
                    hideShowNoData()
                }
            }
        )
    }

    private fun setUpAdapter() {
        adapter = OrderItemProductsRecyclerAdapter(this)
        binding?.recyclerView?.adapter = adapter
        binding?.recyclerView.setOnItemClickListener(this)
        loadData()
    }

    private fun hideShowNoData() {
        if (adapter.itemCount == 0) {
            binding?.recyclerView?.gone()
            binding?.layoutNoData?.linearNoData?.visible()
        } else {
            binding?.layoutNoData?.linearNoData?.gone()
            binding?.recyclerView?.visible()
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        when (item) {

        }
    }

    companion object {
        fun start(
            context: Context?,
            orderId: String
        ) {
            val intent = Intent(context, OrdersActivity::class.java).apply {
                putExtra(Constants.BundleData.ORDER_ID, orderId)
            }
            context?.startActivity(intent)
        }
    }

}