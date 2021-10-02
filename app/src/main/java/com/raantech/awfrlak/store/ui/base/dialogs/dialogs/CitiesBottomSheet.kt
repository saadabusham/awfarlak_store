package com.raantech.awfrlak.store.ui.base.dialogs.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.BottomSheetCitiesBinding
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.City
import com.raantech.awfrlak.store.ui.auth.login.viewmodels.LoginViewModel
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.main.adapters.CityRecyclerAdapter
import com.raantech.awfrlak.store.utils.extensions.longToast
import com.raantech.awfrlak.store.utils.recycleviewutils.VerticalSpaceDecoration

class CitiesBottomSheet(
    private val cityPickerCallBack: CityPickerCallBack,
    private val selectedList: MutableList<City>
) :
    BottomSheetDialogFragment(), BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: LoginViewModel by activityViewModels()
    lateinit var buttomSheetCategoriesBinding: BottomSheetCitiesBinding
    lateinit var cityRecyclerAdapter: CityRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onStart() {
        super.onStart()
        view?.post {
            val parent = requireView().parent as View
            val params = (parent).layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior
            bottomSheetBehavior.peekHeight = requireView().measuredHeight
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buttomSheetCategoriesBinding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_cities, null, false)
        buttomSheetCategoriesBinding.viewModel = this
        isCancelable = true
        setUpRecyclerView()
        return buttomSheetCategoriesBinding.root
    }

    private fun setUpRecyclerView() {
        cityRecyclerAdapter = CityRecyclerAdapter(requireContext(), true)
        buttomSheetCategoriesBinding.recyclerView.adapter = cityRecyclerAdapter
        buttomSheetCategoriesBinding.recyclerView.setOnItemClickListener(this)
        buttomSheetCategoriesBinding.recyclerView.addItemDecoration(
            VerticalSpaceDecoration(
                resources.getDimension(R.dimen._5sdp).toInt(),
                0
            )
        )
        viewModel.getCities().observe(this, citiesObserver())
    }

    private fun citiesObserver(): CustomObserverResponse<List<City>> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<List<City>> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: ResponseWrapper<List<City>>?
                ) {
                    data?.body?.let {
                        selectedList.forEach { selectedLanguage ->
                            it.forEach { city ->
                                if (selectedLanguage.name == city.name) {
                                    city.selected = selectedLanguage.selected
                                }
                            }
                        }
                        cityRecyclerAdapter.addItems(it)
                    }
                }
            }
        )
    }


    fun onDoneClicked() {
        if (!cityRecyclerAdapter.items.filter { it.selected }.isNullOrEmpty()) {
            dismiss()
            val list = mutableListOf<City>()
            cityRecyclerAdapter.items.forEach {
                if (it.selected)
                    list.add(it)
            }
            cityPickerCallBack.callBack(list)
        } else {
            longToast(resources.getString(R.string.please_select_city))
        }
    }

    fun onCancelClicked() {
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState) as BottomSheetDialog
    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    interface CityPickerCallBack {
        fun callBack(citiesList: List<City>)
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {

    }
}