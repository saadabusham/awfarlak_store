package com.technzone.phoneapp.ui.more.aboutus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.technzone.phoneapp.R
import com.technzone.phoneapp.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.phoneapp.data.common.CustomObserverResponse
import com.technzone.phoneapp.data.models.more.AboutUsResponse
import com.technzone.phoneapp.databinding.ActivityAboutUsBinding
import com.technzone.phoneapp.ui.base.activity.BaseBindingActivity
import com.technzone.phoneapp.utils.extensions.openEmail
import com.technzone.phoneapp.utils.extensions.openFacebookPage
import com.technzone.phoneapp.utils.extensions.openInstagram
import com.technzone.phoneapp.utils.extensions.openTwitter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.layout_toolbar.*

@AndroidEntryPoint
class AboutUsActivity : BaseBindingActivity<ActivityAboutUsBinding>() {

    private val viewModel: AboutUsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                layoutResID = R.layout.activity_about_us,
                hasToolbar = true,
                toolbarView = toolbar,
                hasBackButton = true,
                showBackArrow = true,
                hasTitle = false,
                titleString = resources.getString(R.string.menu_about_us)

        )
        viewModel.getAboutUs().observe(this,aboutUsResultObserver())
        setUpListeners()
    }

    private fun setUpListeners() {
        imgFacebook?.setOnClickListener { openFacebookPage(viewModel.getSocialMedia()?.facebookUrl) }
        imgInstegram?.setOnClickListener { openInstagram(viewModel.getSocialMedia()?.instagramUrl) }
        imgTwitter?.setOnClickListener { openTwitter(viewModel.getSocialMedia()?.twitterUrl) }
        imgEmail?.setOnClickListener { openEmail(viewModel.getSocialMedia()?.appEmail) }
    }

    private fun aboutUsResultObserver(): CustomObserverResponse<AboutUsResponse> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<AboutUsResponse> {
                override fun onSuccess(
                        statusCode: Int,
                        subErrorCode: ResponseSubErrorsCodeEnum,
                        data: AboutUsResponse?
                ) {
                    binding?.body = data?.aboutUs
                }
            })
    }


    companion object {

        fun start(context: Context?) {
            val intent = Intent(context, AboutUsActivity::class.java)
            context?.startActivity(intent)
        }

    }

}