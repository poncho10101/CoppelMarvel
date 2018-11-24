package com.coppel.alfonsosotelo.coppelmarvel.utils

import android.content.Context
import java.util.*

/**
 * Class to Convert things in Data Binding
 */
object Converter {

    @JvmStatic
    fun dateToFormat(ctx: Context, date: Date): String {
        return date.formatWithLocale(ctx, isTime = false)
    }

    @JvmStatic
    fun dateTimeToFormat(ctx: Context, date: Date): String {
        return date.formatWithLocale(ctx)
    }

    @JvmStatic
    fun timeToFormat(ctx: Context, date: Date): String {
        return date.formatWithLocale(ctx, false)
    }
}