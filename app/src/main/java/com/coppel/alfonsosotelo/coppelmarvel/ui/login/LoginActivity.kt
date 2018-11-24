package com.coppel.alfonsosotelo.coppelmarvel.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.coppel.alfonsosotelo.coppelmarvel.R
import com.coppel.alfonsosotelo.coppelmarvel.databinding.ActivityLoginBinding
import com.coppel.alfonsosotelo.coppelmarvel.ui.splash.SplashActivity
import com.coppel.alfonsosotelo.coppelmarvel.utils.*
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Login User Activity
 */
class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding // Binding class for activity_login.xml, loaded late
    private val viewModel by lazy { // LoginViewModel loaded lazily
        withViewModel({LoginViewModel(application)}) {
            observe(status) { dataStatus -> // observing the status changes
                when(dataStatus?.dataState) {
                    DataState.SUCCESS -> { // When the DataStatus.dataState changes to SUCCESS
                        if (dataStatus.message == LoginViewModel.SUCCESS_LOGIN) { // If the message of the SUCCESS change is "success_login" then send to Splash
                            sendToSplash()
                        }
                    }
                    DataState.ERROR -> { // When the DataStatus.dataState changes to ERROR
                        dataStatus.message?.let {
                            showError(it)
                            setStatus(DataState.SUCCESS)
                        }
                    }
                    DataState.LOADING -> {} // When the DataStatus.dataState changes to LOADING
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binds the View with Data Binding, this method replaces the setContentView(resId)
        binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login).apply {
            viewModel = this@LoginActivity.viewModel
            setLifecycleOwner(this@LoginActivity)
        }
    }

    private fun sendToSplash() {
        startActivity<SplashActivity>()
        finish()
    }

    private fun showError(message: String) {
        baseContainer.setSnackbar(message)
    }
}