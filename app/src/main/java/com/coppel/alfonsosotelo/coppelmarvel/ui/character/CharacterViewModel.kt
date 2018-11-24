package com.coppel.alfonsosotelo.coppelmarvel.ui.character

import android.app.Application
import android.webkit.URLUtil
import com.coppel.alfonsosotelo.coppelmarvel.R
import com.coppel.alfonsosotelo.coppelmarvel.base.BaseViewModel
import com.coppel.alfonsosotelo.coppelmarvel.entities.Character
import com.coppel.alfonsosotelo.coppelmarvel.utils.DataState
import com.vicpin.krealmextensions.queryFirst
import com.vicpin.krealmextensions.save
import java.util.*

/**
 * ViewModel related to Character
 */
class CharacterViewModel(application: Application): BaseViewModel<Character>(application) {

    /**
     * Loads character from local DB
     */
    fun loadCharacter(id: Long) {
        queryFirst<Character>{
            equalTo("id", id)
        }?.let {
            data.postValue(it)
            setStatus(DataState.SUCCESS)
        } ?: run {
            setStatus(DataState.ERROR, getApplication<Application>().getString(R.string.entity_not_found))
        }
    }

    /**
     * Saves the character in local DB
     */
    fun save() {
        if (validateFields()) {
            data.value?.let {
                it.modified = Date()
                it.save()
                data.postValue(it)
                setStatus(DataState.SUCCESS, SUCCESS_SAVE)
            }
        }
    }

    fun validateFields(): Boolean {
        var valid = true

        if (data.value?.name?.isNotEmpty() != true)
            valid = false

        when {
            data.value?.resourceURI?.isNotEmpty() != true -> valid = false
            URLUtil.isValidUrl(data.value?.resourceURI).not() -> valid = false
        }

        if (!valid)
            setStatus(DataState.ERROR)

        return valid
    }

    companion object {
        const val SUCCESS_SAVE = "success_save"
    }
}