package com.coppel.alfonsosotelo.coppelmarvel.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.coppel.alfonsosotelo.coppelmarvel.R
import com.coppel.alfonsosotelo.coppelmarvel.interfaces.DataStatusHandler
import com.coppel.alfonsosotelo.coppelmarvel.ui.dashboard.DashboardActivity
import com.coppel.alfonsosotelo.coppelmarvel.ui.login.LoginActivity
import com.coppel.alfonsosotelo.coppelmarvel.utils.*
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * The initial Activity in the app, works as a loading page
 * Download first data here
 */
class SplashActivity: AppCompatActivity(), DataStatusHandler {

    //SplashViewModel loaded lazily
    private val viewModel by lazy {
        withViewModel({SplashViewModel(application)}) {
            observe(status, ::handleDataStatus)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Check if is Logged in if is not sends to login activity
        if (!viewModel.isLoggedIn()){
            sendToLogin()
            return
        }

        // check if the data is downloaded
        if (!viewModel.isDataDownloaded()) {
            return
        }

        sendToDashboardActivity()
    }

    override fun handleDataStatus(dataStatus: DataStatus?) {
        when(dataStatus?.dataState){
            DataState.SUCCESS -> {
                if (dataStatus.message == SplashViewModel.SUCCESS_DOWNLOAD) { // if finish to download the data sends to Dashboard
                    sendToDashboardActivity()
                }
            }
            DataState.LOADING -> {}
            DataState.ERROR -> {
                dataStatus.message?.let { // if download data fails a snackbar appears and user can retry it
                    baseContainer.setSnackbar(it, R.string.retry) {
                        viewModel.isDataDownloaded()
                    }
                }
            }
        }
    }

    private fun sendToLogin() {
        startActivity<LoginActivity>()
        finish()
    }

    private fun sendToDashboardActivity() {
        startActivity<DashboardActivity>()
        finish()
    }
}