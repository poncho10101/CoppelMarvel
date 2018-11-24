package com.coppel.alfonsosotelo.coppelmarvel.interfaces

import com.coppel.alfonsosotelo.coppelmarvel.utils.DataStatus

/**
 * Use this interface to handle the DataStatus from ViewModels
 */
interface DataStatusHandler {
    /**
     * Use this method to receive the DataStatus info from a livedata observer
     */
    fun handleDataStatus(dataStatus: DataStatus?)
}