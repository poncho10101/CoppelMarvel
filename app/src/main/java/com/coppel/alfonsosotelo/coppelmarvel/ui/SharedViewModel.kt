package com.coppel.alfonsosotelo.coppelmarvel.ui

import android.app.Application
import android.view.MenuItem
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.lifecycle.MutableLiveData
import com.coppel.alfonsosotelo.coppelmarvel.R
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseViewModel
import com.coppel.alfonsosotelo.coppelmarvel.entities.Character
import com.coppel.alfonsosotelo.coppelmarvel.entities.User
import com.coppel.alfonsosotelo.coppelmarvel.utils.DataState
import com.coppel.alfonsosotelo.coppelmarvel.utils.default
import com.vicpin.krealmextensions.delete

/**
 * This ViewModel is for share data between Fragments and parent Activity
 * call it using the Activity as Provider
 */
class SharedViewModel(application: Application): BaseViewModel<Unit>(application) {
    var updatedCharacters = MutableLiveData<List<Character>>() // Updated characters need to be added here to update the list

    // Contains the data to the toolbar, should be modified from the fragments
    // Activity should observe and bind the toolbar
    var toolbarHandler = MutableLiveData<ToolbarHandler>()

    /**
     * Makes Logout and set success status with success_logout message, to send to splash activity
     */
    fun logout() {
        User.getLoggedUser()?.id?.let {
            delete<User> { equalTo("id", it)}

            setStatus(DataState.SUCCESS, SUCCESS_LOGOUT)
        }
    }

    companion object {
        const val SUCCESS_LOGOUT = "success_logout"
    }

    /**
     * Class containing data of the toolbar
     * @param title Title to put in the toolbar
     * @param menuRes Menu Resource Id to put in the toolbar
     * @param drawableRes Drawable Resource Id to put in the nav icon in the toolbar
     * @param menuClickListener Listener to menu click in the toolbar
     * @param navigationClickListener Listener to navigation icon click in the toolbar
     * @param clear it first value is true the toolbar will be clean(title, menuRes and menuClick) before put the new data
     *          if the second value is true too, the rest of the toolbar data(drawableRes, navigationClick) will be clean before put the new data
     *          you can put this value like (true to true) or (Pair(true, true)) to clean all the toolbar.
     */
    class ToolbarHandler(var title: String? = null,
                         @MenuRes var menuRes: Int? = null,
                         @DrawableRes var drawableRes: Int? = null,
                         var menuClickListener: ((MenuItem) -> Unit)? = null,
                         var navigationClickListener: (() -> Unit)? = null,
                         var clear: Pair<Boolean, Boolean> = false to false
    )
}