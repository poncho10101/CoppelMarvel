package com.coppel.alfonsosotelo.coppelmarvel.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.coppel.alfonsosotelo.coppelmarvel.utils.DataState
import com.coppel.alfonsosotelo.coppelmarvel.utils.DataStatus
import com.coppel.alfonsosotelo.coppelmarvel.utils.default
import io.reactivex.disposables.CompositeDisposable

/**
 * Base ViewModel, you should inherits from this class in the ViewModels
 * T: the data LiveData type, if you will don't need it, you can use Unit
 */
abstract class BaseViewModel<T>(application: Application): AndroidViewModel(application) {
    open var data = MutableLiveData<T>() // the data of an entity/list or what you need
    open var status = MutableLiveData<DataStatus>().default(DataStatus(DataState.SUCCESS)) // status of the process
    protected val disposables = CompositeDisposable() // Rx disposables need to be added here


    /**
     * disposes the disposables
     */
    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }


    /**
     * use this method to change the status, easier than status.postValue(DataStatus(dataState, message?))
     * @param dataState the State of the Status
     * @param message optional message, can be used to error messages or successful actions
     * @sample setStatus(DataState.ERROR)
     */
    fun setStatus(dataState: DataState, message: String? = null) {
        status.postValue(DataStatus(dataState, message))
    }
}