package com.raantech.awfrlak.store.ui.auth.login.viewmodels

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.common.Constants
import com.raantech.awfrlak.store.data.enums.UserEnums
import com.raantech.awfrlak.store.data.models.auth.login2.UserDetailsResponseModel
import com.raantech.awfrlak.store.data.models.map.Address
import com.raantech.awfrlak.store.data.repos.auth.UserRepo
import com.raantech.awfrlak.store.data.repos.configuration.ConfigurationRepo
import com.raantech.awfrlak.store.ui.base.viewmodel.BaseViewModel
import com.raantech.awfrlak.store.utils.DateTimeUtil
import com.raantech.awfrlak.store.utils.extensions.*
import com.raantech.awfrlak.store.utils.pref.SharedPreferencesUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepo: UserRepo,
    private val sharedPreferencesUtil: SharedPreferencesUtil,
    private val configurationRepo: ConfigurationRepo,
    @ApplicationContext context: Context
) : BaseViewModel() {

    companion object {

        const val VALIDATION_CODE_LENGTH = 5

        const val RESEND_ENABLE_TIME_IN_MIN: Long = 1
        const val RESEND_ENABLE_TIME_UPDATE_TIMER_IN_SECOND: Long = 1
        const val JORDANIAN_PHONE_NUMBER_WITHOUT_COUNTRY_CODE_REGEX = "^(7|07)(7|8|9)([0-9]{7})\$"

    }

    //    register
    val storeName: MutableLiveData<String> = MutableLiveData()
    val responsiblePerson: MutableLiveData<String> = MutableLiveData()
    val address: MutableLiveData<Address> = MutableLiveData()
    val addressString: MutableLiveData<String> = MutableLiveData()
    val city: MutableLiveData<String> = MutableLiveData()
    val registrationCertificate: MutableLiveData<String> = MutableLiveData("")
    val storeDescription: MutableLiveData<String> = MutableLiveData("")

    //    login
    val phoneNumberWithoutCountryCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val selectedCountryCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    // Login Verification Code
    val signUpVerificationCode: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val signUpResendPinCodeEnabled: MutableLiveData<Boolean>
            by lazy { MutableLiveData<Boolean>(false) }
    val signUpResendTimer: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val userTokenMutableLiveData: MutableLiveData<String> by lazy { MutableLiveData<String>("") }
    private val forgetCountDownTimer: CountDownTimer by lazy {
        object : CountDownTimer(
            RESEND_ENABLE_TIME_IN_MIN.minToMillisecond(),
            RESEND_ENABLE_TIME_UPDATE_TIMER_IN_SECOND.secondToMillisecond()
        ) {
            override fun onTick(millisUntilFinished: Long) {
                signUpResendTimer.value =
                    millisUntilFinished.millisecondFormatting(DateTimeUtil.TIME_FORMATTING_MIN_AND_SECOND)
            }

            override fun onFinish() {
                signUpResendPinCodeEnabled.value = true
                signUpResendTimer.value = context.resources.getString(R.string.resend_code)
            }
        }
    }

    fun loginUser() = liveData {
        emit(APIResource.loading())
        val response = userRepo.login(
            phoneNumberWithoutCountryCode.value.toString().checkPhoneNumberFormat()
                .concatStrings(selectedCountryCode.value.toString())
        )
        emit(response)
    }

    fun registerUser(
        logo: String,
        additionalImages: List<String>
    ) = liveData {
        emit(APIResource.loading())
        val response = userRepo.register(
            storeName.value.toString().getRequestBody(),
            city.value.toString().getRequestBody(),
            address.value?.lat.toString().getRequestBody(),
            address.value?.lon.toString().getRequestBody(),
            registrationCertificate.value.toString().createImageMultipart("commercial_register"),
            responsiblePerson.value.toString().getRequestBody(),
            logo.createImageMultipart("logo"),
            storeDescription.value.toString().getRequestBody(),
            phoneNumberWithoutCountryCode.value.toString().getRequestBody(),
            additionalImages.createImageMultipart("additional_images[]")
        )
        emit(response)
    }


    fun storeUser(user: UserDetailsResponseModel) {
        signUpVerificationCode.postValue("")
        user.authToken?.let { userRepo.saveAccessToken(it) }
        userRepo.setUserStatus(UserEnums.UserState.LoggedIn)
        userRepo.setUser(user)
    }

    fun startHandleResendSignUpPinCodeTimer() {
        signUpResendPinCodeEnabled.value = false
        forgetCountDownTimer.cancel()
        forgetCountDownTimer.start()
    }

    fun verifyCode(token:String) = liveData {
        emit(APIResource.loading())
        val response = userRepo.verify(
            userTokenMutableLiveData.value.toString(),
            signUpVerificationCode.value.toString().toInt(),
            token,
            Constants.AppPlatform
        )
        emit(response)
    }

    fun resendVerificationCode() = liveData {
        emit(APIResource.loading())
        val response = userRepo.resendCode(
            userTokenMutableLiveData.value.toString()
        )
        emit(response)
    }


    fun getCities() = liveData {
        emit(APIResource.loading())
        val response = configurationRepo.getCities()
        emit(response)
    }
}