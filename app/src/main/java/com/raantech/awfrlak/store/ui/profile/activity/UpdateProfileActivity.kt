package com.raantech.awfrlak.store.ui.profile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.ActivityUpdateProfileBinding
import com.raantech.awfrlak.store.data.api.response.GeneralError
import com.raantech.awfrlak.store.data.api.response.ResponseSubErrorsCodeEnum
import com.raantech.awfrlak.store.data.common.CustomObserverResponse
import com.raantech.awfrlak.store.data.models.auth.login2.UserDetailsResponseModel
import com.raantech.awfrlak.store.data.models.auth.login2.UserInfo
import com.raantech.awfrlak.store.ui.addresses.activity.AddressesActivity
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.main.MainActivity
import com.raantech.awfrlak.store.ui.profile.viewmodels.UpdateProfileViewModel
import com.raantech.awfrlak.store.utils.extensions.showErrorAlert
import com.raantech.awfrlak.store.utils.extensions.showValidationErrorAlert
import com.raantech.awfrlak.store.utils.extensions.validate
import com.raantech.awfrlak.store.utils.validation.ValidatorInputTypesEnums
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class UpdateProfileActivity : BaseBindingActivity<ActivityUpdateProfileBinding>() {

    private val viewModel : UpdateProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            R.layout.activity_update_profile,
            hasToolbar = true,
            toolbarView = toolbar,
            hasBackButton = true,
            showBackArrow = true,
            hasTitle = true,
            title = R.string.menu_account
        )
        binding?.viewModel = viewModel
        setUpListeners()
    }

    private fun setUpListeners() {
        binding?.btnRegister?.setOnClickListener {
            if (validateInput())
                viewModel.updateUser().observe(this, updateResultObserver())
        }

        binding?.btnUpdateAddress?.setOnClickListener {
            AddressesActivity.start(this)
        }
    }

    private fun validateInput(): Boolean {
        binding?.edUserName?.text.toString().validate(
            ValidatorInputTypesEnums.TEXT,
            this
        ).let {
                if (!it.isValid) {
                    showValidationErrorAlert(
                        title = getString(R.string.username),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        binding?.edEmail?.text.toString().validate(
            ValidatorInputTypesEnums.EMAIL,
            this
        ).let {
                if (!it.isValid) {
                    showValidationErrorAlert(
                        title = getString(R.string.email),
                        message = it.errorMessage
                    )
                    return false
                }
            }
        return true
    }

    private fun updateResultObserver(): CustomObserverResponse<UserInfo> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<UserInfo> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: UserInfo?
                ) {
                    data?.let {
                        viewModel.storeUser(it)
                        MainActivity.start(this@UpdateProfileActivity)
                    }
                }

                override fun onError(
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    message: String,
                    errors: List<GeneralError>?
                ) {
                    super.onError(subErrorCode, message, errors)
                    errors?.get(0)?.let {
                        showErrorAlert(it.key, it.getErrorsString())
                    }
                }
            }, showError = false
        )
    }

    companion object {
        fun start(
            context: Context?
        ) {
            val intent = Intent(context, UpdateProfileActivity::class.java)
            context?.startActivity(intent)
        }
    }
}