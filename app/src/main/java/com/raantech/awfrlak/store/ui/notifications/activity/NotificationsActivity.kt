package com.raantech.awfrlak.store.ui.notifications.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.notification.Notification
import com.raantech.awfrlak.databinding.ActivityNotificationsBinding
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.notifications.adapters.NotificationsRecyclerAdapter
import com.raantech.awfrlak.store.ui.notifications.viewmodel.NotificationsViewModel
import com.raantech.awfrlak.store.utils.recycleviewutils.DividerItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class NotificationsActivity : BaseBindingActivity<ActivityNotificationsBinding>() {

    private val viewModel: NotificationsViewModel by viewModels()
    lateinit var adapter: NotificationsRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                layoutResID = R.layout.activity_notifications,
                hasToolbar = true,
                toolbarView = toolbar,
                hasBackButton = true,
                showBackArrow = true,
                hasTitle = true,
                titleString = resources.getString(R.string.menu_notifications)
        )
        init()
    }

    private fun init() {
        setUpListeners()
        setUpAdapter()
//        viewModel.getFAQs().observe(this, faqsObserver())
    }

//    private fun faqsObserver(): CustomObserverResponse<List<Purchase>> {
//        return CustomObserverResponse(
//                this,
//                object : CustomObserverResponse.APICallBack<List<Purchase>> {
//
//                    override fun onSuccess(
//                            statusCode: Int,
//                            subErrorCode: ResponseSubErrorsCodeEnum,
//                            data: List<Purchase>?
//                    ) {
////                        data?.let { adapter.submitItems(it) }
//                    }
//                }, showError = false
//        )
//    }


    private fun setUpAdapter() {
        adapter = NotificationsRecyclerAdapter(this)
        binding?.recyclerView?.adapter = adapter
        adapter.submitItems(
                arrayListOf(
                        Notification(
                                title = "متجرك المفضل (جوالك) اضاف خدمة جديدة",
                                read = true
                        ),
                        Notification(
                                title = "طلبك الان في الطريق",
                                read = true
                        ),
                        Notification(
                                title = "اعلان مميز : خصم ٣٠٪ على جميع منتجات ….",
                                read = true
                        ),
                        Notification(
                                title = "متجرك المفضل (هايبر نت) اضاف خدمة جديدة"
                        ),
                        Notification(
                                title = "اعلان مميز : خصم ٣٠٪ على جميع منتجات …."
                        )
                )
        )
        binding?.recyclerView?.addItemDecoration(
                DividerItemDecorator(resources.getDrawable(R.drawable.divider), 0, 0)
        )
    }

    private fun setUpListeners() {

    }

    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, NotificationsActivity::class.java)
            context?.startActivity(intent)
        }

    }

}