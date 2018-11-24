package com.coppel.alfonsosotelo.coppelmarvel.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.coppel.alfonsosotelo.coppelmarvel.R
import com.coppel.alfonsosotelo.coppelmarvel.databinding.ActivityDashboardBinding
import com.coppel.alfonsosotelo.coppelmarvel.interfaces.DataStatusHandler
import com.coppel.alfonsosotelo.coppelmarvel.ui.SharedViewModel
import com.coppel.alfonsosotelo.coppelmarvel.ui.splash.SplashActivity
import com.coppel.alfonsosotelo.coppelmarvel.utils.*
import kotlinx.android.synthetic.main.activity_dashboard.*

/**
 * This Activity is the "Main" where the user will pass almost all the time
 * Use Fragments to show things in this Activity, preferably with Navigation Components (Nav Graphs)
 */
class DashboardActivity : AppCompatActivity(), DataStatusHandler {
    private var currentController: NavController? = null // Instance here the current NavController

    private val sharedViewModel by lazy { // Instance of SharedViewModel loaded lazily
        withViewModel({SharedViewModel(application)}) { // Instancing the SharedViewModel with application parameter
            observe(status, ::handleDataStatus)
            observe(toolbarHandler) {
                binding.toolbarHandler = it
            }
        }
    }

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityDashboardBinding>(this, R.layout.activity_dashboard).apply {
            toolbarHandler = this@DashboardActivity.sharedViewModel.toolbarHandler.value
        }

        currentController = findNavController(R.id.navCharacters)

        // Set the toolbar with nav controller
        initToolbar()
    }

    override fun handleDataStatus(dataStatus: DataStatus?) {
        when(dataStatus?.dataState) {
            DataState.SUCCESS -> {
                if (dataStatus.message == SharedViewModel.SUCCESS_LOGOUT) {
                    sendToSplashActivity()
                }
            }
            DataState.LOADING -> {}
            DataState.ERROR -> {}
        }
    }

    // When the user clicks in the back toolbar arrow
    override fun onSupportNavigateUp(): Boolean {
        return currentController?.navigateUp() ?: true
    }

    // When back pressed tell the current NavController to back in his BackStack
    // If is no more fragments in the backstack closes the Activity
    override fun onBackPressed() {
        currentController?.let {
            if (it.popBackStack().not()) {
                finish()
            }
        } ?: run { finish() }
    }

    private fun initToolbar() {
        currentController?.let { toolbar.setupWithNavController(it) }
    }

    private fun sendToSplashActivity() {
        startActivity<SplashActivity>()
        finish()
    }
}
