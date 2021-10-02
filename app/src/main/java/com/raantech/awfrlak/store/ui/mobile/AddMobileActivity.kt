package com.raantech.awfrlak.store.ui.mobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.ActivityAddMobileBinding
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.GeneralLookup
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.store.data.models.media.Media
import com.raantech.awfrlak.store.ui.auth.login.adapters.SmallMediaRecyclerAdapter
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.media.MediaActivity
import com.raantech.awfrlak.store.ui.mobile.adapters.GeneralLookupSpinnerAdapter
import com.raantech.awfrlak.store.ui.mobile.viewmodels.MobileViewModel
import com.raantech.awfrlak.store.utils.extensions.showValidationErrorAlert
import com.raantech.awfrlak.store.utils.extensions.validate
import com.raantech.awfrlak.store.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMobileActivity : BaseBindingActivity<ActivityAddMobileBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    lateinit var smallMediaRecyclerAdapter: SmallMediaRecyclerAdapter
    private lateinit var mobileTypeSpinnerAdapter: GeneralLookupSpinnerAdapter
    private lateinit var mobileStorageSpinnerAdapter: GeneralLookupSpinnerAdapter
    private lateinit var mobileColorsSpinnerAdapter: GeneralLookupSpinnerAdapter
    private lateinit var mobileSimsSpinnerAdapter: GeneralLookupSpinnerAdapter
    private lateinit var mobileStatusSpinnerAdapter: GeneralLookupSpinnerAdapter

    val viewModel: MobileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.mobileToView =
            intent.getSerializableExtra(Constants.BundleData.MOBILE) as MobilesItem?
        viewModel.fillData()
        setContentView(
            R.layout.activity_add_mobile,
            hasToolbar = true,
            toolbarView = binding?.layoutToolbar?.toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.add_mobile_for_sale
        )
        setUpBinding()
        initData()
        setUpListeners()
        setUpViews()
    }

    private fun setUpViews() {
        setUpImages()
        setUpMobileType()
        setUpMobileStorage()
        setUpMobileColors()
        setUpMobileSims()
        setUpMobileStatus()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun initData() {
        intent.getSerializableExtra(Constants.BundleData.MOBILE)?.let {
            viewModel.mobileToView = it as MobilesItem
        }
    }

    private fun setUpListeners() {
        binding?.btnAddProduct?.setOnClickListener {
            if (isDataValidate()) {
                MobileDetailsActivity.start(
                    this, viewModel.buildMobileItem(
                        smallMediaRecyclerAdapter.items,
                        color = mobileColorsSpinnerAdapter.getSelectedItem(),
                        isNew = mobileStatusSpinnerAdapter.getSelectedItem().id == 1,
                        simCardsNumbers = mobileSimsSpinnerAdapter.getSelectedItem().id ?: 0,
                        storage = mobileStorageSpinnerAdapter.getSelectedItem(),
                        type = mobileTypeSpinnerAdapter.getSelectedItem(),
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
        viewModel.mobileToView?.additionalImages?.let {
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

    //    Mobile types
    private fun setUpMobileType() {
        mobileTypeSpinnerAdapter =
            GeneralLookupSpinnerAdapter(binding!!.spinnerMobileType, this)
        mobileTypeSpinnerAdapter.let { binding?.spinnerMobileType?.setSpinnerAdapter(it) }
        binding?.spinnerMobileType?.getSpinnerRecyclerView()?.layoutManager =
            LinearLayoutManager(this)
        binding?.spinnerMobileType?.setOnSpinnerItemSelectedListener<GeneralLookup> { oldIndex, oldItem, newIndex, newItem ->
            binding?.spinnerMobileType?.dismiss()
        }
        viewModel.getMobileType()
            .observe(this, mobileTypeResultObserver())
    }

    private fun mobileTypeResultObserver(): CustomObserverResponse<List<GeneralLookup>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<List<GeneralLookup>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<GeneralLookup>?
                ) {
                    data?.let {
                        mobileTypeSpinnerAdapter.setItems(it)
                        mobileTypeSpinnerAdapter.spinnerItems.withIndex()
                            .singleOrNull { viewModel.mobileToView?.type?.id == it.value.id }
                            ?.let {
                                binding?.spinnerMobileType?.selectItemByIndex(it.index)
                            }
                    }
                }
            })
    }

    //    Mobile storage
    private fun setUpMobileStorage() {
        mobileStorageSpinnerAdapter =
            GeneralLookupSpinnerAdapter(binding!!.spinnerMobileStorage, this)
        mobileStorageSpinnerAdapter.let { binding?.spinnerMobileStorage?.setSpinnerAdapter(it) }
        binding?.spinnerMobileStorage?.getSpinnerRecyclerView()?.layoutManager =
            LinearLayoutManager(this)
        binding?.spinnerMobileStorage?.setOnSpinnerItemSelectedListener<GeneralLookup> { oldIndex, oldItem, newIndex, newItem ->
            binding?.spinnerMobileStorage?.dismiss()
        }
        viewModel.getStorage()
            .observe(this, mobileStorageResultObserver())
    }


    private fun mobileStorageResultObserver(): CustomObserverResponse<List<GeneralLookup>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<List<GeneralLookup>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<GeneralLookup>?
                ) {
                    data?.let {
                        mobileStorageSpinnerAdapter.setItems(it)
                        mobileStorageSpinnerAdapter.spinnerItems.withIndex()
                            .singleOrNull { viewModel.mobileToView?.storage?.id == it.value.id }
                            ?.let {
                                binding?.spinnerMobileStorage?.selectItemByIndex(it.index)
                            }
                    }
                }
            })
    }

    //    Mobile colors
    private fun setUpMobileColors() {
        mobileColorsSpinnerAdapter =
            GeneralLookupSpinnerAdapter(binding!!.spinnerMobileColors, this)
        mobileColorsSpinnerAdapter.let { binding?.spinnerMobileColors?.setSpinnerAdapter(it) }
        binding?.spinnerMobileColors?.getSpinnerRecyclerView()?.layoutManager =
            LinearLayoutManager(this)
        binding?.spinnerMobileColors?.setOnSpinnerItemSelectedListener<GeneralLookup> { oldIndex, oldItem, newIndex, newItem ->
            binding?.spinnerMobileColors?.dismiss()
        }
        viewModel.getColors()
            .observe(this, mobileColorsResultObserver())
    }


    private fun mobileColorsResultObserver(): CustomObserverResponse<List<GeneralLookup>> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<List<GeneralLookup>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: List<GeneralLookup>?
                ) {
                    data?.let {
                        mobileColorsSpinnerAdapter.setItems(it)
                        mobileColorsSpinnerAdapter.spinnerItems.withIndex()
                            .singleOrNull { viewModel.mobileToView?.color?.id == it.value.id }
                            ?.let {
                                binding?.spinnerMobileColors?.selectItemByIndex(it.index)
                            }
                    }
                }
            })
    }

    //    Mobile sims
    private fun setUpMobileSims() {
        mobileSimsSpinnerAdapter =
            GeneralLookupSpinnerAdapter(binding!!.spinnerMobileSims, this)
        mobileSimsSpinnerAdapter.let { binding?.spinnerMobileSims?.setSpinnerAdapter(it) }
        binding?.spinnerMobileSims?.getSpinnerRecyclerView()?.layoutManager =
            LinearLayoutManager(this)
        binding?.spinnerMobileSims?.setOnSpinnerItemSelectedListener<GeneralLookup> { oldIndex, oldItem, newIndex, newItem ->
            binding?.spinnerMobileSims?.dismiss()
        }
        mobileSimsSpinnerAdapter.setItems(
            arrayListOf(
                GeneralLookup(name = "1", id = 1),
                GeneralLookup(name = "2", id = 2)
            )
        )
        if (viewModel.mobileToView?.simCardsNumbers != null)
            if (viewModel.mobileToView?.simCardsNumbers == 1) {
                binding?.spinnerMobileSims?.selectItemByIndex(0)
            } else {
                binding?.spinnerMobileSims?.selectItemByIndex(1)
            }
    }

    //    Mobile status
    private fun setUpMobileStatus() {
        mobileStatusSpinnerAdapter =
            GeneralLookupSpinnerAdapter(binding!!.spinnerMobileStatus, this)
        mobileStatusSpinnerAdapter.let { binding?.spinnerMobileStatus?.setSpinnerAdapter(it) }
        binding?.spinnerMobileStatus?.getSpinnerRecyclerView()?.layoutManager =
            LinearLayoutManager(this)
        binding?.spinnerMobileStatus?.setOnSpinnerItemSelectedListener<GeneralLookup> { oldIndex, oldItem, newIndex, newItem ->
            binding?.spinnerMobileStatus?.dismiss()
        }
        mobileStatusSpinnerAdapter.setItems(
            arrayListOf(
                GeneralLookup(name = getString(R.string.new_mobile), id = 1),
                GeneralLookup(name = getString(R.string.old_mobile), id = 2)
            )
        )
        if (viewModel.mobileToView?.isNew != null)
            if (viewModel.mobileToView?.isNew == true) {
                binding?.spinnerMobileStatus?.selectItemByIndex(0)
            } else {
                binding?.spinnerMobileStatus?.selectItemByIndex(1)
            }
    }

    private fun isDataValidate(): Boolean {
        if (mobileTypeSpinnerAdapter.index == -1) {
            showValidationErrorAlert(
                resources.getString(R.string.app_name),
                resources.getString(R.string.please_select_the_mobile_type)
            )
            return false
        }
        if (mobileStorageSpinnerAdapter.index == -1) {
            showValidationErrorAlert(
                resources.getString(R.string.app_name),
                resources.getString(R.string.please_select_the_mobile_storage)
            )
            return false
        }
        if (mobileColorsSpinnerAdapter.index == -1) {
            showValidationErrorAlert(
                resources.getString(R.string.app_name),
                resources.getString(R.string.please_select_the_mobile_colors)
            )
            return false
        }
        if (mobileSimsSpinnerAdapter.index == -1) {
            showValidationErrorAlert(
                resources.getString(R.string.app_name),
                resources.getString(R.string.please_select_the_mobile_sims_number)
            )
            return false
        }
        if (mobileStatusSpinnerAdapter.index == -1) {
            showValidationErrorAlert(
                resources.getString(R.string.app_name),
                resources.getString(R.string.please_select_the_mobile_status)
            )
            return false
        }

        binding?.edMobileInfo?.text.toString().validate(
            ValidatorInputTypesEnums.TEXT,
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
                        title = resources.getString(R.string.add_mobile_info),
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
            mobilesItem: MobilesItem?,
            update: Boolean = false
        ) {
            val intent = Intent(context, AddMobileActivity::class.java).apply {
                putExtra(Constants.BundleData.MOBILE, mobilesItem)
                putExtra(Constants.BundleData.UPDATE, update)
            }
            context.startActivity(intent)
        }


    }

}