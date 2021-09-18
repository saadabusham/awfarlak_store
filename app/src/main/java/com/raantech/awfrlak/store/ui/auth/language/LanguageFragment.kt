package com.raantech.awfrlak.store.ui.auth.language

import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.raantech.awfrlak.R
import com.raantech.awfrlak.store.common.CommonEnums
import com.raantech.awfrlak.databinding.FragmentLanguageBinding
import com.raantech.awfrlak.store.ui.base.activity.BaseBindingActivity
import com.raantech.awfrlak.store.ui.base.fragment.BaseBindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : BaseBindingFragment<FragmentLanguageBinding>() {

    private val viewModel: LanguageViewModel by navGraphViewModels(R.id.auth_nav_graph) { defaultViewModelProviderFactory }

    override fun getLayoutId(): Int = R.layout.fragment_language

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpListeners()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }

    private fun setUpListeners() {
        binding?.btnContinue?.setOnClickListener {
            viewModel.saveLanguage().observe(viewLifecycleOwner, Observer {
                activity?.let {
                    (it as BaseBindingActivity<*>).setLanguage(if (viewModel.englishSelected.value!!)
                        CommonEnums.Languages.English.value else CommonEnums.Languages.Arabic.value)
                    navigationController.navigate(R.id.action_languageFragment_to_loginFragment)
                }
            })
        }

    }
}