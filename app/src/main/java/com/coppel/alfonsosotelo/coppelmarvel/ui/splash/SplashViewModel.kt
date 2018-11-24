package com.coppel.alfonsosotelo.coppelmarvel.ui.splash

import android.app.Application
import com.coppel.alfonsosotelo.coppelmarvel.api.MarvelApiService
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseViewModel
import com.coppel.alfonsosotelo.coppelmarvel.entities.Character
import com.coppel.alfonsosotelo.coppelmarvel.entities.User
import com.coppel.alfonsosotelo.coppelmarvel.utils.DataState
import com.coppel.alfonsosotelo.coppelmarvel.utils.addToDisposables
import com.coppel.alfonsosotelo.coppelmarvel.utils.getErrorMessage
import com.coppel.alfonsosotelo.coppelmarvel.utils.setDefaultSchedulers
import com.vicpin.krealmextensions.count
import com.vicpin.krealmextensions.saveAll

/**
 * ViewModel for SplashActivity
 */
class SplashViewModel(application: Application): BaseViewModel<Unit>(application) {

    /**
     * Check if data is downloaded if it's not download it
     */
    fun isDataDownloaded(): Boolean {
        var isDownloaded = true

        if (count<Character>() == 0L) {
            isDownloaded = false
            MarvelApiService.get(getApplication()).getCharacters().setDefaultSchedulers()
                .doOnSubscribe { setStatus(DataState.LOADING) }
                .subscribe({
                    it.getResults().saveAll()
                    setStatus(DataState.SUCCESS, SUCCESS_DOWNLOAD)
                }, {
                    setStatus(DataState.ERROR, it.getErrorMessage(getApplication()))
                }).addToDisposables(disposables)
        }

        return isDownloaded
    }

    fun isLoggedIn() = User.isLoggedIn()

    companion object {
        const val SUCCESS_DOWNLOAD = "success_download"
    }
}