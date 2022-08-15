package com.raantech.awfrlak.store.ui.citypicker.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.jakewharton.rxbinding3.widget.textChangeEvents
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.ActivityCitiesBinding
import com.raantech.awfrlak.store.data.api.response.GeneralError
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.CitiesResponse
import com.raantech.awfrlak.store.data.models.City
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.base.bindingadapters.setupClearButtonWithAction
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.showErrorAlert
import com.raantech.awfrlak.store.utils.extensions.visible
import com.raantech.awfrlak.store.utils.plus
import com.raantech.awfrlak.store.ui.citypicker.adapters.CitiesRecyclerAdapter
import com.raantech.awfrlak.store.ui.citypicker.viewmodels.CitiesViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class CitiesPickerActivity : BaseBindingActivity<ActivityCitiesBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: CitiesViewModel by viewModels()

    private lateinit var citiesRecyclerAdapter: CitiesRecyclerAdapter
    private val originalList: MutableList<City> = mutableListOf()
    var compositeDisposable: CompositeDisposable? = CompositeDisposable()
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_cities,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.empty_string,
            navigationIcon = R.drawable.ic_close,
            backArrowTint = R.color.black
        )
        observeLoading()
        setUpRecyclerView()
        initSearch()
    }

    private fun onDone() {
        val selectedCity = citiesRecyclerAdapter.getSelectedItem()
        if (selectedCity == null) {
            showErrorAlert(
                message = getString(R.string.please_select_city)
            )
            return
        }
        val data = Intent()
        data.putExtra(
            Constants.BundleData.CITY,
            selectedCity
        )
        setResult(RESULT_OK, data)
        finish()
    }

    private fun setUpRecyclerView() {
        citiesRecyclerAdapter = CitiesRecyclerAdapter(this)
        binding?.recyclerView?.adapter = citiesRecyclerAdapter
        binding?.recyclerView?.setOnItemClickListener(this)
        loadData()
    }

    private fun initSearch() {
        binding?.edTitle?.setupClearButtonWithAction()
        compositeDisposable + binding?.edTitle?.textChangeEvents()
            ?.skipInitialValue()
            ?.debounce(400, TimeUnit.MILLISECONDS)
            ?.distinctUntilChanged()
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe {
                if (it.text.isNotEmpty()) {
                    applyFilter(it.text.toString())
                } else {
                    applyFilter("")
                }
            }
    }

    private fun applyFilter(text: String) {
        if (text.isEmpty()) {
            citiesRecyclerAdapter.clear()
            citiesRecyclerAdapter.submitItems(originalList)
        } else {
            citiesRecyclerAdapter.clear()
            citiesRecyclerAdapter.submitItems(originalList.filter {
                it.name?.contains(text) == true || it.code?.contains(
                    text
                ) == true
            })
            hideShowNoData()
        }
    }

    private fun loadData() {
        viewModel.getCities()
            .observe(this, citiesResultObserver())
    }


    private fun citiesResultObserver(): CustomObserverResponse<CitiesResponse> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<CitiesResponse> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: CitiesResponse?
                ) {
                    data?.cities?.let {
                        originalList.addAll(it)
                        citiesRecyclerAdapter.submitItems(it)
                    }
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onError(
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    message: String,
                    errors: List<GeneralError>?
                ) {
                    loading.postValue(false)
                    hideShowNoData()
                }

                override fun onLoading() {
                    super.onLoading()
                    loading.postValue(true)
                }
            }, withProgress = false
        )
    }

    private fun observeLoading() {
        loading.observe(this) {
            if (it) {
                binding?.layoutShimmer?.shimmerViewContainer?.visible()
                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
                binding?.layoutShimmer?.shimmerViewContainer?.gone()
                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
            }
        }
    }

    private fun hideShowNoData() {
        citiesRecyclerAdapter.itemCount.let {
            if (it == 0) {
                binding?.layoutNoResult?.root?.visible()
            } else {
                binding?.layoutNoResult?.root?.gone()
            }
        }
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        onDone()
    }

    companion object {
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, CitiesPickerActivity::class.java)
            context?.startActivity(intent)
        }

        fun start(
            context: Activity?,
            currentCode: String? = "",
            resultLauncher: ActivityResultLauncher<Intent>
        ) {
            val intent = Intent(context, CitiesPickerActivity::class.java).apply {
                putExtra(Constants.BundleData.CITY, currentCode)
            }
            resultLauncher.launch(intent)
        }
    }

}