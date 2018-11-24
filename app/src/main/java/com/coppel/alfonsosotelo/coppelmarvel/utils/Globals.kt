package com.coppel.alfonsosotelo.coppelmarvel.utils

import com.coppel.alfonsosotelo.coppelmarvel.BuildConfig

/**
 * App Globals Variables
 */
object Globals {
    const val APP_VERSION: String = BuildConfig.VERSION_NAME // version in app build.gradle
    val DEBUG: Boolean = if (!BuildConfig.DEBUG) BuildConfig.DEBUG else true //in release always be false, in debug you can choice

    const val DB_NAME = "coppelmarvel.realm" // name of realm database
    const val DB_SCHEMA_VERSION: Long = 1 // version of realm database, add one when make changes on entities

    const val MARVEL_PRIVATE_KEY = Keys.MARVEL_PRIVATE_KEY
    const val MARVEL_PUBLIC_KEY = Keys.MARVEL_PUBLIC_KEY
    const val API_BASE_URL = "http://gateway.marvel.com/"
    const val API_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ" // marvel api date format

}