package com.coppel.alfonsosotelo.coppelmarvel

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/**
 * this class is for use GlideApp.with
 */
@GlideModule
class MyAppGlideModule: AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)


    }
}