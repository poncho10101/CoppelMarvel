package com.coppel.alfonsosotelo.coppelmarvel.ui.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.coppel.alfonsosotelo.coppelmarvel.R
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseViewModel
import com.coppel.alfonsosotelo.coppelmarvel.entities.User
import com.coppel.alfonsosotelo.coppelmarvel.exceptions.ApiRequestException
import com.coppel.alfonsosotelo.coppelmarvel.utils.DataState
import com.coppel.alfonsosotelo.coppelmarvel.utils.addToDisposables
import com.coppel.alfonsosotelo.coppelmarvel.utils.default
import com.coppel.alfonsosotelo.coppelmarvel.utils.setDefaultSchedulers
import com.vicpin.krealmextensions.save
import io.reactivex.Single

/**
 * ViewModel related to Login process
 */
class LoginViewModel(application: Application): BaseViewModel<User>(application) {
    override var data: MutableLiveData<User> = MutableLiveData<User>().default(User())

    // This Single does a login mock
    private val loginObservable = Single.create<User> {
        Thread.sleep(1800)
        if (data.value?.username == "coppel" && data.value?.password == "coppel") {
            it.onSuccess(User().apply {
                id = 1
                username = "coppel"
                password = ""
            })
        } else {
            it.onError(
                ApiRequestException(
                    getApplication<Application>().getString(R.string.login_error_message)
                )
            )
        }
    }
    /**
     * Dummy login
     */
    fun login() {
        if (validateData()) {
            loginObservable.setDefaultSchedulers()
                .doOnSubscribe {
                    setStatus(DataState.LOADING)
                }.subscribe({
                    it.save()
                    setStatus(DataState.SUCCESS, SUCCESS_LOGIN)
                }, {
                    setStatus(DataState.ERROR, it.message)
                }).addToDisposables(disposables)
        }
    }

    /**
     * This method validates that the data is correct
     * @return true if is valid
     */
    fun validateData(): Boolean {
        var valid = true

        if (data.value?.username == "" || data.value?.password == "") {
            setStatus(DataState.ERROR)
            valid = false
        }

        return valid
    }

    companion object {
        const val SUCCESS_LOGIN = "success_login"
    }
}