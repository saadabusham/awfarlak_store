package com.raantech.awfrlak.store.ui.accessory

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
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.GeneralLookup
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.media.Media
import com.raantech.awfrlak.store.ui.accessory.viewmodels.AccessoryViewModel
import com.raantech.awfrlak.store.ui.auth.login.adapters.SmallMediaRecyclerAdapter
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.media.MediaActivity
import com.raantech.awfrlak.store.ui.mobile.adapters.GeneralLookupSpinnerAdapter
import com.raantech.awfrlak.store.utils.extensions.showValidationErrorAlert
import com.raantech.awfrlak.store.utils.extensions.validate
import com.raantech.awfrlak.store.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAccessoryActivity : BaseBindingActivity<ActivityAddAccessoryBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    lateinit var smallMediaRecyclerAdapter: SmallMediaRecyclerAdapter
    private lateinit var accessoryTypeSpinnerAdapter: GeneralLookupSpinnerAdapter
    private lateinit var accessoryDedecatedSpinnerAdapter: GeneralLookupSpinnerAdapter

    val viewModel: AccessoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.accessoriesToView =
            intent.getSerializableExtra(Constants.BundleData.ACCESSORY) as AccessoriesItem?
        viewModel.fillData()
        setContentView(
            R.layout.activity_add_accessory,
            hasToolbar = true,
            toolbarView = binding?.layoutToolbar?.toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.add_accessory_for_sale
        )
        setUpBinding()
        initData()
        setUpListeners()
        setUpViews()
    }

    private fun setUpViews() {
        setUpImages()
        setUpAccessoryType()
        setUpAccessoryDedicated()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun initData() {
        intent.getSerializableExtra(Constants.BundleData.ACCESSORY)?.let {
            viewModel.accessoriesToView = it as AccessoriesItem
        }
    }

    private fun setUpListeners() {
        binding?.btnAddProduct?.setOnClickListener {
            if (isDataValidate()) {
//                MobileDetailsActivity.start(
//                    this, viewModel.buildMobileItem(
//                        smallMediaRecyclerAdapter.items,
//                        dedicatedFor = accessoryDedecatedSpinnerAdapter.getSelectedItem(),
//                        type = accessoryTypeSpinnerAdapter.getSelectedItem(),
//                        baseImage = smallMediaRecyclerAdapter.items[0]
//                    ), intent.getBooleanExtra(Constants.BundleData.UPDATE, false),
//                    true
//                )
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
        viewModel.accessoriesToView?.additionalImages?.let {
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

    //    Accessory types
    private fun setUpAccessoryType() {
        accessoryTypeSpinnerAdapter =
            GeneralLookupSpinnerAdapter(binding!!.spinnerAccessoryType, this)
        accessoryTypeSpinnerAdapter.let { binding?.spinnerAccessoryType?.setSpinnerAdapter(it) }
        binding?.spinnerAccessoryType?.getSpinnerRecyclerView()?.layoutManager =
            LinearLayoutManager(this)
        binding?.spinnerAccessoryType?.setOnSpinnerItemSelectedListener<GeneralLookup> { oldIndex, oldItem, newIndex, newItem ->
            binding?.spinnerAccessoryType?.dismiss()
        }
        viewModel.getAccessoriesType()
            .observe(this, accessoryTypeResultObserver())
    }

    private fun accessoryTypeResultObserver(): CustomObserverResponse<List<GeneralLookup>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<List<GeneralLookup>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<GeneralLookup>?
                ) {
                    data?.let {
                        accessoryTypeSpinnerAdapter.setItems(it)
                        accessoryTypeSpinnerAdapter.spinnerItems.withIndex()
                            .singleOrNull { viewModel.accessoriesToView?.id == it.value.id }
                            ?.let {
                                binding?.spinnerAccessoryType?.selectItemByIndex(it.index)
                            }
                    }
                }
            })
    }

    //    Accessory storage
    private fun setUpAccessoryDedicated() {
        accessoryDedecatedSpinnerAdapter =
            GeneralLookupSpinnerAdapter(binding!!.spinnerAccessoryDedicated, this)
        accessoryDedecatedSpinnerAdapter.let { binding?.spinnerAccessoryDedicated?.setSpinnerAdapter(it) }
        binding?.spinnerAccessoryDedicated?.getSpinnerRecyclerView()?.layoutManager =
            LinearLayoutManager(this)
        binding?.spinnerAccessoryDedicated?.setOnSpinnerItemSelectedListener<GeneralLookup> { oldIndex, oldItem, newIndex, newItem ->
            binding?.spinnerAccessoryDedicated?.dismiss()
        }
        viewModel.getAccessoryDedicated()
            .observe(this, accessoryDedicatedResultObserver())
    }


    private fun accessoryDedicatedResultObserver(): CustomObserverResponse<List<GeneralLookup>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<List<GeneralLookup>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<GeneralLookup>?
                ) {
                    data?.let {
                        accessoryDedecatedSpinnerAdapter.setItems(it)
                        accessoryDedecatedSpinnerAdapter.spinnerItems.withIndex()
                            .singleOrNull { viewModel.accessoriesToView?.accessoryDedicated?.id == it.value.id }
                            ?.let {
                                binding?.spinnerAccessoryDedicated?.selectItemByIndex(it.index)
                            }
                    }
                }
            })
    }

    private fun isDataValidate(): Boolean {
        if (accessoryTypeSpinnerAdapter.index == -1) {
            showValidationErrorAlert(
                resources.getString(R.string.app_name),
                resources.getString(R.string.please_select_the_accessory_type)
            )
            return false
        }
        if (accessoryDedecatedSpinnerAdapter.index == -1) {
            showValidationErrorAlert(
                resources.getString(R.string.app_name),
                resources.getString(R.string.please_select_the_accessory_dedicated)
            )
            return false
        }
        binding?.edAccessoryInfo?.text.toString().validate(
            ValidatorInputTypesEnums.TEXT,
            this
        )
            .let {
                if (!it.isValid) {
                    showValidationErrorAlert(
                        title = resources.getString(R.string.add_accessory_info),
                        message = it.errorMessage
                    )
                    return false
                }
            }

        if (smallMediaRecyclerAdapter.itemCount == 0) {
            showValidationErrorAlert(
                resources.getString(R.string.product_images),
                resources.getString(R.string.please_select_the_product_images)
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
                accessoriesItem: AccessoriesItem?,
                update: Boolean = false
        ) {
            val intent = Intent(context, AddAccessoryActivity::class.java).apply {
                putExtra(Constants.BundleData.ACCESSORY, accessoriesItem)
                putExtra(Constants.BundleData.UPDATE, update)
            }
            context.startActivity(intent)
        }


    }

}