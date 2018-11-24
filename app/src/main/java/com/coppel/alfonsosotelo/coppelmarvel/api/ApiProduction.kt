package com.coppel.alfonsosotelo.coppelmarvel.api

import android.content.Context
import com.coppel.alfonsosotelo.coppelmarvel.utils.Globals
import com.google.gson.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier

class ApiProduction(private var mContext: Context) {

    var baseUrl = Globals.API_BASE_URL

    companion object {

        fun provideGson(fieldWithUnderscore: Boolean): Gson {
            val gsonBuilder = GsonBuilder()

            gsonBuilder.excludeFieldsWithModifiers(Modifier.TRANSIENT)

            if (fieldWithUnderscore) {
                gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            }

            gsonBuilder.setDateFormat(Globals.API_DATE_FORMAT)

            gsonBuilder.serializeNulls()

            return gsonBuilder.create()
        }
    }

    private fun provideRestAdapter(fieldWithUnderscore: Boolean, authorization: Boolean = true): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(OkHttpProduction.getOkHttpClient(mContext, Globals.DEBUG, authorization))
                .addConverterFactory(GsonConverterFactory.create(provideGson(fieldWithUnderscore)))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    fun <S> provideService(serviceClass: Class<S>, fieldWithUnderscore: Boolean = false, authorization: Boolean = true): S {
        return provideRestAdapter(fieldWithUnderscore, authorization).create(serviceClass)
    }
}