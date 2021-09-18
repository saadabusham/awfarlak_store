package com.raantech.awfrlak.store.ui.main.products

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.FragmentProductsBinding
import com.raantech.awfrlak.databinding.FragmentStatisticsBinding
import com.raantech.awfrlak.store.ui.base.fragment.BaseBindingFragment
import com.raantech.awfrlak.store.ui.main.viewmodels.GeneralViewModel
import com.raantech.awfrlak.store.ui.store.adapters.StoreImagesAdapter
import com.raantech.awfrlak.store.ui.store.fragment.AccessoriesFragment
import com.raantech.awfrlak.store.ui.store.fragment.MobilesFragment
import com.raantech.awfrlak.store.ui.store.fragment.ServicesFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_store.*

@AndroidEntryPoint
class ProductsFragment : BaseBindingFragment<FragmentProductsBinding>() {

    private val viewModel: GeneralViewModel by viewModels()
    private val mFragmentList = mutableListOf<Fragment>()

    override fun getLayoutId(): Int = R.layout.fragment_products

    override fun onViewVisible() {
        super.onViewVisible()
        setUpBinding()
        setUpTabLayout()
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
    }
    fun setUpTabLayout() {
        setUpViewPager()
        binding?.tabLayout?.addTab(binding?.tabLayout!!.newTab(), 0, true)
        binding?.tabLayout?.addTab(binding?.tabLayout!!.newTab(), 1, false)
        binding?.tabLayout?.setSelectedTabIndicatorColor(Color.parseColor("#00000000"))
        binding?.tabLayout?.setSelectedTabIndicatorHeight(0)
        binding?.tabLayout?.setTabTextColors(
            Color.parseColor("#000000"),
            Color.parseColor("#ffffff")
        )
        binding?.tabLayout?.getTabAt(0)?.text = resources.getString(R.string.mobiles)
        binding?.tabLayout?.getTabAt(1)?.text = resources.getString(R.string.accessories)

        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }

    private fun setUpViewPager() {
        mFragmentList.add(MobilesFragment())
        mFragmentList.add(AccessoriesFragment())
        val adapter: FragmentStatePagerAdapter =
            object : FragmentStatePagerAdapter(childFragmentManager) {
                override fun getItem(position: Int): Fragment {
                    return mFragmentList[position]
                }

                override fun getCount(): Int {
                    return mFragmentList.size
                }
            }
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
    }



}