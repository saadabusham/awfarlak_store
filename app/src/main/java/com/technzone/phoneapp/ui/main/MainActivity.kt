package com.technzone.phoneapp.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.technzone.phoneapp.R
import com.technzone.phoneapp.common.CommonEnums
import com.technzone.phoneapp.data.api.response.ResponseSubErrorsCodeEnum
import com.technzone.phoneapp.data.common.CustomObserverResponse
import com.technzone.phoneapp.databinding.ActivityMainBinding
import com.technzone.phoneapp.ui.base.activity.BaseBindingActivity
import com.technzone.phoneapp.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.technzone.phoneapp.ui.base.bindingadapters.setOnItemClickListener
import com.technzone.phoneapp.ui.cart.CartActivity
import com.technzone.phoneapp.ui.main.adapters.DrawerRecyclerAdapter
import com.technzone.phoneapp.ui.main.viewmodels.MainViewModel
import com.technzone.phoneapp.ui.media.MediaActivity
import com.technzone.phoneapp.ui.more.aboutus.AboutUsActivity
import com.technzone.phoneapp.ui.more.settings.SettingsActivity
import com.technzone.phoneapp.ui.purchase.PurchasesActivity
import com.technzone.phoneapp.ui.splash.SplashActivity
import com.technzone.phoneapp.utils.LocaleUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.layout_home_toolbar.*
import kotlin.math.abs

@AndroidEntryPoint
class MainActivity : BaseBindingActivity<ActivityMainBinding>(),
    BaseBindingRecyclerViewAdapter.OnItemClickListener {

    private val viewModel: MainViewModel by viewModels()
    lateinit var drawerRecyclerAdapter: DrawerRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            layoutResID = R.layout.activity_main,
            hasToolbar = true,
            toolbarView = toolbar,
            hasTitle = true,
            title = R.string.app_name
        )
        setUpBinding()
        setupNavigation()
        setUpDrawer()
        setUpListeners()
    }

    private fun setUpListeners() {
        binding?.appBarMain?.layoutToolbar?.imgCart?.setOnClickListener {
            if (!viewModel.cartCount.value.equals("0"))
                CartActivity.start(this)
        }
        binding?.appBarMain?.layoutToolbar?.imgSearch?.setOnClickListener {
//            FiltersActivity.start(this)
        }
    }

    private fun setUpBinding() {
        binding?.viewModel = viewModel
        binding?.appBarMain?.layoutToolbar?.viewModel = viewModel
        viewModel.getCartsCount()
    }


    private fun setupNavigation() {
        val navController = findNavController(R.id.main_nav_host_fragment)
        NavigationUI.setupWithNavController(
            bnv_main,
            navController
        )

        bnv_main?.setOnNavigationItemReselectedListener {
            // Do Nothing To Disable ReLunch fragment when reClick on nav icon
        }

    }

    private fun setUpDrawer() {
        drawerRecyclerAdapter = DrawerRecyclerAdapter(this)
        drawerRecyclerAdapter.submitItems(getDrawerList())
        binding?.drawerRecyclerView?.adapter = drawerRecyclerAdapter
        binding?.drawerRecyclerView?.setOnItemClickListener(this)
        val toggle = ActionBarDrawerToggle(
            this, binding?.drawerLayout, binding?.appBarMain?.layoutToolbar?.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        initDrawer(toggle)
    }


    private fun initDrawer(toggle: ActionBarDrawerToggle) {
        val drawable = ResourcesCompat.getDrawable(
            resources, R.drawable.ic_menu,
            theme
        )
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(drawable)
        binding?.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()
        toggle.toolbarNavigationClickListener = View.OnClickListener { v: View? ->
            if (binding?.drawerLayout?.isDrawerVisible(GravityCompat.START) == true) {
                binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            } else {
                binding?.drawerLayout?.openDrawer(GravityCompat.START)
            }
        }
        binding?.drawerLayout?.setScrimColor(Color.TRANSPARENT)
        binding?.drawerLayout?.drawerElevation = 0.toFloat()
        binding?.drawerLayout?.addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerSlide(drawer: View, slideOffset: Float) {
                if (LocaleUtil.getLanguage() == "ar") {
                    binding?.appBarMain?.holder?.rotation = (slideOffset * -1) * 10
                    binding?.appBarMain?.container?.scaleX = abs(slideOffset * 0.4f - 1)
                    binding?.appBarMain?.container?.scaleY = abs(slideOffset * 0.4f - 1)
                    binding?.appBarMain?.container?.pivotX = 0.toFloat()
                    binding?.appBarMain?.container?.pivotY = (1000).toFloat()
                } else {
                    binding?.appBarMain?.container?.x =
                        (binding?.navigationView?.width!! * (slideOffset))
                    binding?.appBarMain?.holder?.rotation = slideOffset * 10
                    binding?.appBarMain?.container?.scaleX = abs(slideOffset * 0.4f - 1)
                    binding?.appBarMain?.container?.scaleY = abs(slideOffset * 0.4f - 1)
                    binding?.appBarMain?.container?.pivotX = 0.toFloat()
                    binding?.appBarMain?.container?.pivotY = (1000).toFloat()
                }
            }

            override fun onDrawerClosed(drawerView: View) {}
        }
        )
    }

    private fun getDrawerList(): List<String> {
        return arrayListOf(
            resources.getString(R.string.menu_notifications),
            resources.getString(R.string.menu_my_purchases),
            resources.getString(R.string.menu_favorites),
            resources.getString(R.string.media),
            resources.getString(R.string.menu_account),
            resources.getString(R.string.menu_settings),
            resources.getString(R.string.menu_about_us),
            resources.getString(R.string.logout)
        )
    }

    override fun onItemClick(view: View?, position: Int, item: Any) {
        if (item is String) {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
            when (position) {
//                0 -> UpdateProfileActivity.start(this)
                1 -> PurchasesActivity.start(this)
//                2 -> WishListActivity.start(this)
                3 -> MediaActivity.start(this)
//                4 -> ReportProviderActivity.start(this)
                5 -> SettingsActivity.start(this)
                6 -> AboutUsActivity.start(this)
                7 -> {
                    viewModel.logoutRemote().observe(this, logoutResultObserver())
                }
            }
        }
    }


    private fun logoutResultObserver(): CustomObserverResponse<Any> {
        return CustomObserverResponse(
            this,
            object : CustomObserverResponse.APICallBack<Any> {
                override fun onSuccess(
                    statusCode: Int,
                    subErrorCode: ResponseSubErrorsCodeEnum,
                    data: Any?
                ) {
                    viewModel.logoutLocale()
                    SplashActivity.start(this@MainActivity)
                }
            })
    }


    companion object {
        const val EXTRA_FROM_NOTIFICATION = "notifications"
        fun start(context: Context?) {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(intent)
        }
    }

}