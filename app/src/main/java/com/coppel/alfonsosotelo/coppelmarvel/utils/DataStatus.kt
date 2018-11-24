package com.coppel.alfonsosotelo.coppelmarvel.utils


enum class DataState { LOADING, SUCCESS, ERROR }

data class DataStatus constructor(
        val dataState: DataState,
        val message: String? = null
)