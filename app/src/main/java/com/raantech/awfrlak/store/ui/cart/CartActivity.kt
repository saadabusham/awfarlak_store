package com.raantech.awfrlak.store.ui.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.models.Price
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.databinding.ActivityCartBinding
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.cart.adapters.CartRecyclerAdapter
import com.raantech.awfrlak.store.ui.cart.viewmodels.CartViewModel
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.round
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class CartActivity : BaseBindingActivity<ActivityCartBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: CartViewModel by viewModels()
    lateinit var cartRecyclerAdapter: CartRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                layoutResID = R.layout.activity_cart,
                hasToolbar = true,
                toolbarView = toolbar,
                hasTitle = true,
                title = R.string.cart,
                hasBackButton = true,
                showBackArrow = true
        )
        setUpBinding()
        setUpListeners()
        setUpRecyclerView()
    }

    private fun setUpListeners() {
        binding?.btnPay?.setOnClickListener {
            PayedSuccessActivity.start(this)
        }
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpRecyclerView() {
        cartRecyclerAdapter = CartRecyclerAdapter(this)
        binding?.recyclerView?.adapter = cartRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        loadData()
    }

    private fun loadData() {
        viewModel.getCarts(
        ).observe(this, {
            cartRecyclerAdapter.submitItems(it)
            calculattion()
            hideShowNoData()
        })
    }

    private fun calculattion() {
        binding?.count = cartRecyclerAdapter.itemCount
        var subtotal = 0.0
        cartRecyclerAdapter.items.forEach {
            if (it is MobilesItem)
                subtotal += it.price?.amount?.toDouble()?.times(it.count ?: 1) ?: 0.0
            else if (it is AccessoriesItem)
                subtotal += it.price?.amount?.toDouble()?.times(it.count ?: 1) ?: 0.0
        }
        viewModel.subTotal.postValue(
                Price(
                        amount = subtotal.toString(),
                        formatted = "$subtotal ${resources.getString(R.string.sar)}"
                )
        )
        viewModel.TAX_CONST?.times(subtotal)?.let {
            viewModel.tax.postValue(
                    Price(
                            amount = it.toString(),
                            formatted = "${it.round(2)} ${resources.getString(R.string.sar)}"
                    )
            )
            viewModel.total.postValue(
                    Price(
                            amount = (subtotal + it).toString(),
                            formatted = "${(subtotal + it).round(2)} ${resources.getString(R.string.sar)}"
                    )
            )
        }
    }

    private fun hideShowNoData() {
        if (cartRecyclerAdapter.itemCount == 0) {
            binding?.recyclerView?.gone()
            binding?.layoutNoData?.linearNoData?.visible()
            binding?.btnPay?.gone()
        } else {
            binding?.layoutNoData?.linearNoData?.gone()
            binding?.recyclerView?.visible()
            binding?.btnPay?.visible()
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        when(item){
            is AccessoriesItem -> {
                if (view?.id == R.id.imgPlus || view?.id == R.id.imgMinus) {
                    viewModel.updateAccessoryCartItem(item)
                    calculattion()
                } else if (view?.id == R.id.cvDelete) {
                    item.id?.let {
                        viewModel.deleteAccessoryCart(it)
                        cartRecyclerAdapter.items.remove(item)
                        cartRecyclerAdapter.notifyItemRemoved(position)
                        calculattion()
                        hideShowNoData()
                    }
                }
            }
            is MobilesItem -> {
                if (view?.id == R.id.imgPlus || view?.id == R.id.imgMinus) {
                    viewModel.updateMobileCartItem(item)
                    calculattion()
                } else if (view?.id == R.id.cvDelete) {
                    item.id?.let {
                        viewModel.deleteMobileCart(it)
                        cartRecyclerAdapter.items.remove(item)
                        cartRecyclerAdapter.notifyItemRemoved(position)
                        calculattion()
                        hideShowNoData()
                    }
                }
            }
        }
    }

    companion object {
        fun start(
                context: Context?
        ) {
            val intent = Intent(context, CartActivity::class.java)
            context?.startActivity(intent)
        }
    }

}