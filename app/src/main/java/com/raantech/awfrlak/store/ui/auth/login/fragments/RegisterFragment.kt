package com.raantech.awfrlak.store.ui.auth.login.fragments

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.navGraphViewModels
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.FragmentRegisterBinding
import com.raantech.awfrlak.store.data.api.response.GeneralError
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.auth.login2.UserDetailsResponseModel
import com.raantech.awfrlak.store.data.models.map.Address
import com.raantech.awfrlak.store.data.models.media.Media
import com.raantech.awfrlak.store.ui.auth.login.adapters.SmallMediaRecyclerAdapter
import com.raantech.awfrlak.store.ui.auth.login.viewmodels.LoginViewModel
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.bindingadapters.setOnItemClickListener
import com.raantech.awfrlak.store.ui.base.fragment.BaseBindingFragment
import com.raantech.awfrlak.store.ui.main.MainActivity
import com.raantech.awfrlak.store.ui.map.MapActivity
import com.raantech.awfrlak.store.utils.extensions.showErrorAlert
import com.raantech.awfrlak.store.utils.extensions.showValidationErrorAlert
import com.raantech.awfrlak.store.utils.extensions.validate
import com.raantech.awfrlak.store.utils.getLocationName
import com.raantech.awfrlak.store.utils.pickImages
import com.raantech.awfrlak.store.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class RegisterFragment : BaseBindingFragment<FragmentRegisterBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_register

    lateinit var imagesMediaRecyclerAdapter: SmallMediaRecyclerAdapter
    lateinit var logoMediaRecyclerAdapter: SmallMediaRecyclerAdapter

    private val viewModel: LoginViewModel by navGraphViewModels(R.id.auth_nav_graph) { defaultViewModelProviderFactory }

    override fun onViewVisible() {
        super.onViewVisible()
        addToolbar(
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.register
        )
        binding?.viewModel = viewModel
        setUpListeners()
        init()
    }

    private fun init() {
        setUpLogoRecyclerView()
        setUpImagesRecyclerView()
    }

    private fun setUpListeners() {
        binding?.btnRegister?.setOnClickListener {
            if (validateInput()) {
                viewModel.registerUser(
                    logoMediaRecyclerAdapter.items[0].url ?: "",
                    imagesMediaRecyclerAdapter.items.map { it.url ?: "" })
                    .observe(this, registerResultObserver())
            }
        }
        binding?.imgimagesUpload?.setOnClickListener {
            pickImages(
                requestCode = IMAGES
            )
        }

        binding?.cvCertificate?.setOnClickListener {
            pickImages(
                requestCode = CERTIFICATE
            )
        }

        binding?.imgLogoUpload?.setOnClickListener {
            if (logoMediaRecyclerAdapter.itemCount == 0)
                pickImages(
                    requestCode = LOGO
                )
        }
        binding?.cvAddress?.setOnClickListener {
            MapActivity.start(requireActivity(), locationResultLauncher)
        }
    }


    private fun setUpImagesRecyclerView() {
        imagesMediaRecyclerAdapter = SmallMediaRecyclerAdapter(requireContext())
        binding?.imagesRecyclerView?.adapter = imagesMediaRecyclerAdapter
        binding?.imagesRecyclerView?.setOnItemClickListener(object :
            BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {
                if (item is Media) {
                    if (view?.id == R.id.imgRemove) {
                        imagesMediaRecyclerAdapter.items.removeAt(position)
                        imagesMediaRecyclerAdapter.notifyItemRemoved(position)
                    }
                }
            }
        })
    }

    private fun setUpLogoRecyclerView() {
        logoMediaRecyclerAdapter = SmallMediaRecyclerAdapter(requireContext())
        binding?.logoRecyclerView?.adapter = logoMediaRecyclerAdapter
        binding?.logoRecyclerView?.setOnItemClickListener(object :
            BaseBindingRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, item: Any) {
                if (item is Media) {
                    if (view?.id == R.id.imgRemove) {
                        logoMediaRecyclerAdapter.items.removeAt(position)
                        logoMediaRecyclerAdapter.notifyItemRemoved(position)
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            LOGO -> {
                val fileUri = data?.data
                fileUri?.path?.let {
                    logoMediaRecyclerAdapter.submitItem(Media(url = it))
                }
            }
            CERTIFICATE -> {
                val fileUri = data?.data
                fileUri?.path?.let {
                    viewModel.registrationCertificate.postValue(it)
                }
            }
            IMAGES -> {
                val fileUri = data?.data
                fileUri?.path?.let {
                    imagesMediaRecyclerAdapter.submitItem(Media(url = it))
                    binding?.imagesRecyclerView?.smoothScrollToPosition(imagesMediaRecyclerAdapter.itemCount - 1)
                }
            }
        }
    }

    var locationResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                viewModel.address.value =
                    data?.getSerializableExtra(Constants.BundleData.ADDRESS) as Address
                binding?.tvAddress?.text =
                    getLocationName(
                        viewModel.address.value?.lat,
                        viewModel.address.value?.lon
                    ).also {
                        viewModel.addressString.postValue(it)
                    }
            }
        }


    private fun validateInput(): Boolean {
        binding?.edStoreInfo?.text.toString().validate(
            ValidatorInputTypesEnums.TEXT,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showValidationErrorAlert(
                        title = getString(R.string.store_information),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        binding?.tvCity?.text.toString().validate(
            ValidatorInputTypesEnums.TEXT,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showValidationErrorAlert(
                        title = getString(R.string.city),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        binding?.edResponsiblePerson?.text.toString().validate(
            ValidatorInputTypesEnums.TEXT,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showValidationErrorAlert(
                        title = getString(R.string.responsible_person),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        binding?.tvAddress?.text.toString().validate(
            ValidatorInputTypesEnums.TEXT,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showValidationErrorAlert(
                        title = getString(R.string.address),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        viewModel.registrationCertificate.value.toString().validate(
            ValidatorInputTypesEnums.TEXT,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showValidationErrorAlert(
                        title = getString(R.string.registration_certificate),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        binding?.etPhoneNumber?.text.toString().validate(
            ValidatorInputTypesEnums.PHONE_NUMBER,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showValidationErrorAlert(
                        title = getString(R.string.phone_number),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        if (logoMediaRecyclerAdapter.itemCount == 0) {
            requireActivity().showValidationErrorAlert(
                title = getString(R.string.logo),
                message = getString(R.string.please_select_logo)
            )
            return false
        }
        binding?.edStoreInfo?.text.toString().validate(
            ValidatorInputTypesEnums.TEXT,
            requireContext()
        )
            .let {
                if (!it.isValid) {
                    requireActivity().showValidationErrorAlert(
                        title = getString(R.string.store_information),
                        message = it.errorMessage
                    )
                    return false
                }
            }

        if (imagesMediaRecyclerAdapter.itemCount == 0) {
            requireActivity().showValidationErrorAlert(
                title = getString(R.string.store_images),
                message = getString(R.string.please_select_store_images)
            )
            return false
        }
        return true
    }

    private fun registerResultObserver(): CustomObserverResponse<UserDetailsResponseModel> {
        return CustomObserverResponse(
            requireActivity(),
            object : CustomObserverResponse.APICallBack<UserDetailsResponseModel> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: UserDetailsResponseModel?
                ) {
                    data?.let {
                        if (data.isRegistered == true) {
                            viewModel.storeUser(it)
                            viewModel.userTokenMutableLiveData.postValue(it.authToken)
//                            navigationController.navigate(R.id.action_registerFragment_to_verificationLoginFragment)
                            MainActivity.start(requireContext())
                        }
                    }
                }

                override fun onError(
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    message: String,
                    errors: List<GeneralError>?
                ) {
                    super.onError(subErrorCode, message, errors)
                    errors?.get(0)?.let {
                        requireActivity().showErrorAlert(it.key, it.getErrorsString())
                    } ?: also {
                        requireActivity().showErrorAlert("", message)
                    }
                }
            }, showError = false
        )
    }

    companion object {
        const val LOGO = 1
        const val IMAGES = 2
        const val CERTIFICATE = 3
    }

}