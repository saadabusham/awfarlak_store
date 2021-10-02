package com.raantech.awfrlak.store.ui.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.ActivityAddAccessoryBinding
import com.raantech.awfrlak.databinding.ActivityAddServiceBinding
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.GeneralLookup
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.home.Service
import com.raantech.awfrlak.store.data.models.media.Media
import com.raantech.awfrlak.store.ui.accessory.AccessoryDetailsActivity
import com.raantech.awfrlak.store.ui.accessory.viewmodels.AccessoryViewModel
import com.raantech.awfrlak.store.ui.auth.login.adapters.SmallMediaRecyclerAdapter
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.media.MediaActivity
import com.raantech.awfrlak.store.ui.mobile.adapters.GeneralLookupSpinnerAdapter
import com.raantech.awfrlak.store.ui.service.viewmodels.ServiceViewModel
import com.raantech.awfrlak.store.utils.extensions.showValidationErrorAlert
import com.raantech.awfrlak.store.utils.extensions.validate
import com.raantech.awfrlak.store.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddServiceActivity : BaseBindingActivity<ActivityAddServiceBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    lateinit var smallMediaRecyclerAdapter: SmallMediaRecyclerAdapter

    val viewModel: ServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.serviceToView =
                intent.getSerializableExtra(Constants.BundleData.SERVICE) as Service?
        viewModel.fillData()
        setContentView(
                R.layout.activity_add_service,
                hasToolbar = true,
                toolbarView = binding?.layoutToolbar?.toolbar,
                hasBackButton = true,
                showBackArrow = true,
                hasTitle = true,
                title = R.string.add_new_service
        )
        setUpBinding()
        initData()
        setUpListeners()
        setUpViews()
    }

    private fun setUpViews() {
        setUpImages()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun initData() {
        intent.getSerializableExtra(Constants.BundleData.SERVICE)?.let {
            viewModel.serviceToView = it as Service
        }
    }

    private fun setUpListeners() {
        binding?.btnAddProduct?.setOnClickListener {
            if (isDataValidate()) {
                ServiceDetailsActivity.start(
                        this, viewModel.buildService(
                        additionalImages = smallMediaRecyclerAdapter.items,
                        baseImage = smallMediaRecyclerAdapter.items[0]
                ), intent.getBooleanExtra(Constants.BundleData.UPDATE, false),
                        true
                )
            }
        }
        binding?.imgUpload?.setOnClickListener {
            MediaActivity.start(this, true, resultLauncher)
        }
    }

    var resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    smallMediaRecyclerAdapter.submitItem(data?.getSerializableExtra(Constants.BundleData.MEDIA) as Media)
                    binding?.imagesRecyclerView?.smoothScrollToPosition(smallMediaRecyclerAdapter.itemCount - 1)
                }
            }

    //Images
    private fun setUpImages() {
        smallMediaRecyclerAdapter = SmallMediaRecyclerAdapter(this)
        binding?.imagesRecyclerView?.adapter = smallMediaRecyclerAdapter
        binding?.imagesRecyclerView?.setOnItemClickListener(this)
        viewModel.serviceToView?.additionalImages?.let {
            smallMediaRecyclerAdapter.submitItems(it)
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is Media) {
            if (view?.id == R.id.imgRemove) {
                smallMediaRecyclerAdapter.items.removeAt(position)
                smallMediaRecyclerAdapter.notifyItemRemoved(position)
            }
        }
    }

    private fun isDataValidate(): Boolean {
        binding?.edAccessoryInfo?.text.toString().validate(
                ValidatorInputTypesEnums.TEXT,
                this
        )
                .let {
                    if (!it.isValid) {
                        showValidationErrorAlert(
                                title = resources.getString(R.string.add_service_info),
                                message = it.errorMessage
                        )
                        return false
                    }
                }

        if (smallMediaRecyclerAdapter.itemCount == 0) {
            showValidationErrorAlert(
                    resources.getString(R.string.add_service_images),
                    resources.getString(R.string.please_select_the_service_images)
            )
            return false
        }

        binding?.edPrice?.text.toString().validate(
                ValidatorInputTypesEnums.PRICE,
                this
        )
                .let {
                    if (!it.isValid) {
                        showValidationErrorAlert(
                                title = resources.getString(R.string.price),
                                message = it.errorMessage
                        )
                        return false
                    }
                }
        return true
    }

    companion object {
        fun start(
                context: Context,
                service: Service?,
                update: Boolean = false
        ) {
            val intent = Intent(context, AddServiceActivity::class.java).apply {
                putExtra(Constants.BundleData.SERVICE, service)
                putExtra(Constants.BundleData.UPDATE, update)
            }
            context.startActivity(intent)
        }


    }

}