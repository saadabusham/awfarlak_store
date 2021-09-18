package com.raantech.awfrlak.store.ui.main.home.fragment

import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.paginate.Paginate
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.api.response.GeneralError
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.enums.CategoriesEnum
import com.raantech.awfrlak.store.data.models.home.*
import com.raantech.awfrlak.databinding.FragmentHomeBinding
import com.raantech.awfrlak.store.ui.accessory.AccessoryDetailsActivity
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.base.fragment.BaseBindingFragment
import com.raantech.awfrlak.store.ui.main.home.adapters.*
import com.raantech.awfrlak.store.ui.main.viewmodels.GeneralViewModel
import com.raantech.awfrlak.store.ui.mobile.MobileDetailsActivity
import com.raantech.awfrlak.store.ui.service.ServiceDetailsActivity
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(),
        BaseBindingRecyclerViewAdapter.OnItemClickListener {

    override fun getLayoutId(): Int = R.layout.fragment_home

    private val viewModel: GeneralViewModel by viewModels()
    private val loading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val loadingMobiles: MutableLiveData<Boolean> = MutableLiveData(false)
    private val loadingAccessories: MutableLiveData<Boolean> = MutableLiveData(false)
    private val loadingStores: MutableLiveData<Boolean> = MutableLiveData(false)
    private val loadingServices: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isMobilesFinished = false
    private var isAccessoriesFinished = false
    private var isStoresFinished = false
    private var isServicesFinished = false

    lateinit var accessoriesRecyclerAdapter: AccessoriesRecyclerAdapter
    lateinit var accessoriesGridRecyclerAdapter: AccessoriesGridRecyclerAdapter
    lateinit var phonesRecyclerAdapter: PhonesRecyclerAdapter
    lateinit var phonesGridRecyclerAdapter: PhonesGridRecyclerAdapter
    lateinit var sliderAdapter: SliderAdapter
    lateinit var storesRecyclerAdapter: StoresRecyclerAdapter
    lateinit var storesGridRecyclerAdapter: StoresGridRecyclerAdapter
    lateinit var servicesGridRecyclerAdapter: ServicesGridRecyclerAdapter

    var adapter: CategoriesAdapter? = null

    var refreshData: Boolean = false

    var isFullScreen: Boolean = false

    override fun onResume() {
        super.onResume()
        if (!refreshData) {
            refreshData = true
            return
        }
        reloadData()
    }

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        loadingObserver()
        loadingMobilesObserver()
        loadingAccessoriesObserver()
        loadingStoresObserver()
        loadingServicesObserver()
        setUpListeners()
        init()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun init() {
        setUpRvMobiles()
        setUpRvMobilesGrid()
        setUpRvAccessories()
        setUpRvAccessoriesGrid()
        setUpRvStores()
        setUpRvStoresGrid()
        setUpRvServicesGrid()
        setUpRvSlider()
        loadData()
        setUpCategories()
    }

    private fun setUpListeners() {
        binding?.layoutPhones?.tvAll?.setOnClickListener {
            handleMobilesSelected(true)
        }
        binding?.layoutAccessories?.tvAll?.setOnClickListener {
            handleAccessoriesSelected(true)
        }
        binding?.layoutStores?.tvAll?.setOnClickListener {
            handleStoresSelected(true)
        }
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isFullScreen) {
                    handleAllSelected()
                } else
                    requireActivity().finish()
            }
        })
    }

    private fun reloadData() {
        phonesRecyclerAdapter.clear()
        accessoriesRecyclerAdapter.clear()
        storesRecyclerAdapter.clear()
        sliderAdapter.clear()
        loadData()
    }

    private fun setUpCategories() {
        adapter = binding?.spinnerCategories?.let { CategoriesAdapter(it, requireActivity()) }
        adapter?.let { binding?.spinnerCategories?.setSpinnerAdapter(it) }
        binding?.spinnerCategories?.getSpinnerRecyclerView()?.layoutManager =
                LinearLayoutManager(requireActivity())
        adapter?.setItems(
                arrayListOf(
                        Category(CategoriesEnum.ALL, resources.getString(R.string.all_categories)),
                        Category(CategoriesEnum.MOBILES, resources.getString(R.string.mobiles)),
                        Category(CategoriesEnum.ACCESSORIES, resources.getString(R.string.accessories)),
                        Category(CategoriesEnum.STORES, resources.getString(R.string.stores)),
                        Category(CategoriesEnum.SERVICES, resources.getString(R.string.services))
                )
        )
        binding?.spinnerCategories?.selectItemByIndex(0)
        binding?.spinnerCategories?.setIsFocusable(true)
        binding?.spinnerCategories?.getSpinnerBodyView()
        binding?.spinnerCategories?.setOnSpinnerItemSelectedListener<Category> { oldIndex, oldItem, newIndex, newItem ->
            when (newItem.categoryEnum) {
                CategoriesEnum.ALL -> {
                    handleAllSelected()
                }
                CategoriesEnum.MOBILES -> {
                    handleMobilesSelected(false)
                }
                CategoriesEnum.ACCESSORIES -> {
                    handleAccessoriesSelected(false)
                }
                CategoriesEnum.STORES -> {
                    handleStoresSelected(false)
                }
                CategoriesEnum.SERVICES -> {
                    handleServiceSelected(false)
                }
            }
        }
    }

    private fun viewFullScreen(fullScreen: Boolean) {
        isFullScreen = fullScreen
        TransitionManager.beginDelayedTransition(binding?.root as ViewGroup)
        if (fullScreen) binding?.spinnerCategories?.gone()
        else binding?.spinnerCategories?.visible()
    }

    private fun handleAllSelected() {
        viewFullScreen(false)
        binding?.layoutPhonesGrid?.linearPhonesGrid?.gone()
        binding?.layoutAccessoriesGrid?.linearAccessoriesGrid?.gone()
        binding?.layoutStoreGrid?.linearStoresGrid?.gone()
        binding?.layoutServicesGrid?.linearServicesGrid?.gone()
        binding?.layoutSlider?.relativeSlider?.visible()
        binding?.layoutPhones?.linearPhones?.visible()
        binding?.layoutAccessories?.linearAccessories?.visible()
        binding?.layoutStores?.linearStores?.visible()
//        loadData()
    }

    private fun handleMobilesSelected(fullScreen: Boolean) {
        viewFullScreen(fullScreen)
        binding?.layoutAccessoriesGrid?.linearAccessoriesGrid?.gone()
        binding?.layoutSlider?.relativeSlider?.gone()
        binding?.layoutPhones?.linearPhones?.gone()
        binding?.layoutAccessories?.linearAccessories?.gone()
        binding?.layoutStores?.linearStores?.gone()
        binding?.layoutStoreGrid?.linearStoresGrid?.gone()
        binding?.layoutServicesGrid?.linearServicesGrid?.gone()
        binding?.layoutPhonesGrid?.linearPhonesGrid?.visible()
        loadMobiles()
    }

    private fun handleAccessoriesSelected(fullScreen: Boolean) {
        viewFullScreen(fullScreen)
        binding?.layoutPhonesGrid?.linearPhonesGrid?.gone()
        binding?.layoutSlider?.relativeSlider?.gone()
        binding?.layoutPhones?.linearPhones?.gone()
        binding?.layoutAccessories?.linearAccessories?.gone()
        binding?.layoutStores?.linearStores?.gone()
        binding?.layoutStoreGrid?.linearStoresGrid?.gone()
        binding?.layoutServicesGrid?.linearServicesGrid?.gone()
        binding?.layoutAccessoriesGrid?.linearAccessoriesGrid?.visible()
        loadAccessories()
    }

    private fun handleStoresSelected(fullScreen: Boolean) {
        viewFullScreen(fullScreen)
        binding?.layoutStores?.linearStores?.gone()
        binding?.layoutAccessoriesGrid?.linearAccessoriesGrid?.gone()
        binding?.layoutPhonesGrid?.linearPhonesGrid?.gone()
        binding?.layoutSlider?.relativeSlider?.gone()
        binding?.layoutPhones?.linearPhones?.gone()
        binding?.layoutAccessories?.linearAccessories?.gone()
        binding?.layoutServicesGrid?.linearServicesGrid?.gone()
        binding?.layoutStoreGrid?.linearStoresGrid?.visible()
        loadStores()
    }

    private fun handleServiceSelected(fullScreen: Boolean) {
        viewFullScreen(fullScreen)
        binding?.layoutStoreGrid?.linearStoresGrid?.gone()
        binding?.layoutAccessoriesGrid?.linearAccessoriesGrid?.gone()
        binding?.layoutPhonesGrid?.linearPhonesGrid?.gone()
        binding?.layoutSlider?.relativeSlider?.gone()
        binding?.layoutPhones?.linearPhones?.gone()
        binding?.layoutAccessories?.linearAccessories?.gone()
        binding?.layoutStores?.linearStores?.gone()
        binding?.layoutServicesGrid?.linearServicesGrid?.visible()
        loadServices()
    }

    //    All
    private fun loadingObserver() {
        loading.observe(this, {
            if (it) {
                binding?.layoutSlider?.layoutShimmer?.shimmerViewContainer?.visible()
                binding?.layoutPhones?.layoutShimmer?.shimmerViewContainer?.visible()
                binding?.layoutAccessories?.layoutShimmer?.shimmerViewContainer?.visible()
                binding?.layoutStores?.layoutShimmer?.shimmerViewContainer?.visible()

//                binding?.layoutShimmer?.shimmerViewContainer?.startShimmer()
            } else {
                binding?.layoutSlider?.layoutShimmer?.shimmerViewContainer?.gone()
                binding?.layoutPhones?.layoutShimmer?.shimmerViewContainer?.gone()
                binding?.layoutAccessories?.layoutShimmer?.shimmerViewContainer?.gone()
                binding?.layoutStores?.layoutShimmer?.shimmerViewContainer?.gone()

//                binding?.layoutShimmer?.shimmerViewContainer?.stopShimmer()
            }
        })
    }

    private fun loadData() {
        viewModel.getHome().observe(this, homeObserver())
    }

    //    Mobiles

    private fun loadingMobilesObserver() {
        loadingMobiles.observe(this, {
            if (it) {
                binding?.layoutPhonesGrid?.layoutShimmer?.shimmerViewContainer?.visible()
            } else {
                binding?.layoutPhonesGrid?.layoutShimmer?.shimmerViewContainer?.gone()
            }
        })
    }

    private fun setUpRvMobiles() {
        phonesRecyclerAdapter = PhonesRecyclerAdapter(requireContext())
        binding?.layoutPhones?.recyclerView?.adapter = phonesRecyclerAdapter
        binding?.layoutPhones?.recyclerView.setOnItemClickListener(this)
    }

    private fun setUpRvMobilesGrid() {
        phonesGridRecyclerAdapter = PhonesGridRecyclerAdapter(requireContext())
        binding?.layoutPhonesGrid?.recyclerView?.adapter = phonesGridRecyclerAdapter
        binding?.layoutPhonesGrid?.recyclerView.setOnItemClickListener(this)
        Paginate.with(binding?.layoutPhonesGrid?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loadingMobiles.value == false && phonesGridRecyclerAdapter.itemCount > 0 && !isMobilesFinished) {
                    loadMobiles()
                }
            }

            override fun isLoading(): Boolean {
                return loadingMobiles.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isMobilesFinished
            }

        })
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(false)
                .build()
    }

    private fun loadMobiles() {
        viewModel.getMobiles(phonesGridRecyclerAdapter.itemCount).observe(this, mobilesObserver())
    }

    //    Accessories

    private fun loadingAccessoriesObserver() {
        loadingAccessories.observe(this, {
            if (it) {
                binding?.layoutAccessoriesGrid?.layoutShimmer?.shimmerViewContainer?.visible()
            } else {
                binding?.layoutAccessoriesGrid?.layoutShimmer?.shimmerViewContainer?.gone()
            }
        })
    }

    private fun setUpRvAccessories() {
        accessoriesRecyclerAdapter = AccessoriesRecyclerAdapter(requireContext())
        binding?.layoutAccessories?.recyclerView?.adapter = accessoriesRecyclerAdapter
        binding?.layoutAccessories?.recyclerView.setOnItemClickListener(this)
    }

    private fun setUpRvAccessoriesGrid() {
        accessoriesGridRecyclerAdapter = AccessoriesGridRecyclerAdapter(requireContext())
        binding?.layoutAccessoriesGrid?.recyclerView?.adapter = accessoriesGridRecyclerAdapter
        binding?.layoutAccessoriesGrid?.recyclerView.setOnItemClickListener(this)
        Paginate.with(binding?.layoutAccessoriesGrid?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loadingAccessories.value == false && accessoriesGridRecyclerAdapter.itemCount > 0 && !isAccessoriesFinished) {
                    loadAccessories()
                }
            }

            override fun isLoading(): Boolean {
                return loadingAccessories.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isAccessoriesFinished
            }

        })
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(false)
                .build()
    }

    private fun loadAccessories() {
        viewModel.getAccessories(accessoriesGridRecyclerAdapter.itemCount)
                .observe(this, accessoriesObserver())
    }

    //    Stores

    private fun loadingStoresObserver() {
        loadingStores.observe(this, {
            if (it) {
                binding?.layoutStoreGrid?.layoutShimmer?.shimmerViewContainer?.visible()
            } else {
                binding?.layoutStoreGrid?.layoutShimmer?.shimmerViewContainer?.gone()
            }
        })
    }

    private fun setUpRvStores() {
        storesRecyclerAdapter = StoresRecyclerAdapter(requireContext())
        binding?.layoutStores?.recyclerView?.adapter = storesRecyclerAdapter
        binding?.layoutStores?.recyclerView.setOnItemClickListener(this)
    }

    private fun setUpRvStoresGrid() {
        storesGridRecyclerAdapter = StoresGridRecyclerAdapter(requireContext())
        binding?.layoutStoreGrid?.recyclerView?.adapter = storesGridRecyclerAdapter
        binding?.layoutStoreGrid?.recyclerView.setOnItemClickListener(this)
        Paginate.with(binding?.layoutStoreGrid?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loadingStores.value == false && storesGridRecyclerAdapter.itemCount > 0 && !isStoresFinished) {
                    loadAccessories()
                }
            }

            override fun isLoading(): Boolean {
                return loadingStores.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isStoresFinished
            }

        })
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(false)
                .build()
    }

    private fun loadStores() {
        viewModel.getStores(storesGridRecyclerAdapter.itemCount)
                .observe(this, storesObserver())
    }

    //    Services

    private fun loadingServicesObserver() {
        loadingServices.observe(this, {
            if (it) {
                binding?.layoutServicesGrid?.layoutShimmer?.shimmerViewContainer?.visible()
            } else {
                binding?.layoutServicesGrid?.layoutShimmer?.shimmerViewContainer?.gone()
            }
        })
    }

    private fun setUpRvServicesGrid() {
        servicesGridRecyclerAdapter = ServicesGridRecyclerAdapter(requireContext())
        binding?.layoutServicesGrid?.recyclerView?.adapter = servicesGridRecyclerAdapter
        binding?.layoutServicesGrid?.recyclerView.setOnItemClickListener(this)
        Paginate.with(binding?.layoutServicesGrid?.recyclerView, object : Paginate.Callbacks {
            override fun onLoadMore() {
                if (loadingServices.value == false && servicesGridRecyclerAdapter.itemCount > 0 && !isServicesFinished) {
                    loadServices()
                }
            }

            override fun isLoading(): Boolean {
                return loadingServices.value ?: false
            }

            override fun hasLoadedAllItems(): Boolean {
                return isServicesFinished
            }

        })
                .setLoadingTriggerThreshold(1)
                .addLoadingListItem(false)
                .build()
    }

    private fun loadServices() {
        viewModel.getServices(servicesGridRecyclerAdapter.itemCount)
                .observe(this, servicesObserver())
    }

    //    Sliders
    private fun setUpRvSlider() {
        sliderAdapter = SliderAdapter(requireContext())
        binding?.layoutSlider?.recyclerView?.adapter = sliderAdapter
        binding?.layoutSlider?.recyclerView.setOnItemClickListener(this)
    }

    private fun hideShowNoData() {
//        if (accessoriesRecyclerAdapter.itemCount == 0) {
//            binding?.recyclerView?.gone()
//            binding?.layoutNoData?.linearNoData?.visible()
//        } else {
//            binding?.layoutNoData?.linearNoData?.gone()
//            binding?.recyclerView?.visible()
//        }
    }

    private fun homeObserver(): CustomObserverResponse<HomeResponse> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<HomeResponse> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: HomeResponse?
                    ) {
                        data?.let {
                            data.slider?.let {
                                it.slides?.let { it1 -> sliderAdapter.submitItems(it1) }
                            }
                            data.accessories?.let {
                                accessoriesRecyclerAdapter.submitItems(it)
                            }
                            data.mobiles?.let {
                                phonesRecyclerAdapter.submitItems(it)
                            }
                            data.stores?.let {
                                storesRecyclerAdapter.submitItems(it)
                            }
                        }
                        loading.postValue(false)
                    }

                    override fun onError(
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            message: String,
                            errors: List<GeneralError>?
                    ) {
                        super.onError(subErrorCode, message, errors)
                        loading.postValue(false)
                        hideShowNoData()
                    }

                    override fun onLoading() {
                        loading.postValue(true)
                    }
                }, false, showError = false
        )
    }

    private fun mobilesObserver(): CustomObserverResponse<List<MobilesItem>> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<List<MobilesItem>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: List<MobilesItem>?
                    ) {
                        data?.let {
                            phonesGridRecyclerAdapter.submitItems(it)
                        }
                        if (data.isNullOrEmpty())
                            isMobilesFinished = true
                        loadingMobiles.postValue(false)
                    }

                    override fun onError(
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            message: String,
                            errors: List<GeneralError>?
                    ) {
                        super.onError(subErrorCode, message, errors)
                        loadingMobiles.postValue(false)
                        hideShowNoData()
                    }

                    override fun onLoading() {
                        loadingMobiles.postValue(true)
                    }
                }, false, showError = false
        )
    }

    private fun accessoriesObserver(): CustomObserverResponse<List<AccessoriesItem>> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<List<AccessoriesItem>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: List<AccessoriesItem>?
                    ) {
                        data?.let {
                            accessoriesGridRecyclerAdapter.submitItems(it)
                        }
                        if (data.isNullOrEmpty())
                            isAccessoriesFinished = true
                        loadingAccessories.postValue(false)
                    }

                    override fun onError(
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            message: String,
                            errors: List<GeneralError>?
                    ) {
                        super.onError(subErrorCode, message, errors)
                        loadingAccessories.postValue(false)
                        hideShowNoData()
                    }

                    override fun onLoading() {
                        loadingAccessories.postValue(true)
                    }
                }, false, showError = false
        )
    }

    private fun storesObserver(): CustomObserverResponse<List<Store>> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<List<Store>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: List<Store>?
                    ) {
                        data?.let {
                            storesGridRecyclerAdapter.submitItems(it)
                        }
                        if (data.isNullOrEmpty())
                            isStoresFinished = true
                        loadingStores.postValue(false)
                    }

                    override fun onError(
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            message: String,
                            errors: List<GeneralError>?
                    ) {
                        super.onError(subErrorCode, message, errors)
                        loadingStores.postValue(false)
                        hideShowNoData()
                    }

                    override fun onLoading() {
                        loadingStores.postValue(true)
                    }
                }, false, showError = false
        )
    }

    private fun servicesObserver(): CustomObserverResponse<List<Service>> {
        return CustomObserverResponse(
                requireActivity(),
                object : CustomObserverResponse.APICallBack<List<Service>> {
                    override fun onSuccess(
                            statusCode: Int,
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            data: List<Service>?
                    ) {
                        data?.let {
                            servicesGridRecyclerAdapter.submitItems(it)
                        }
                        if (data.isNullOrEmpty())
                            isServicesFinished = true
                        loadingServices.postValue(false)
                    }

                    override fun onError(
                            subErrorCode: ResponseSubErrorsCodeEnum,
                            message: String,
                            errors: List<GeneralError>?
                    ) {
                        super.onError(subErrorCode, message, errors)
                        loadingServices.postValue(false)
                        hideShowNoData()
                    }

                    override fun onLoading() {
                        loadingServices.postValue(true)
                    }
                }, false, showError = false
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        when (item) {
            is MobilesItem -> {
                MobileDetailsActivity.start(requireActivity(), item)
            }
            is AccessoriesItem -> {
                AccessoryDetailsActivity.start(requireActivity(),item)
            }
            is Store -> {
            }
            is SlidesItem -> {

            }
            is Service -> {
                ServiceDetailsActivity.start(requireActivity(), item)
            }
        }
    }

}