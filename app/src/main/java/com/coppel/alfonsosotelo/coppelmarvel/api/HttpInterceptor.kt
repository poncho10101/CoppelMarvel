package com.coppel.alfonsosotelo.coppelmarvel.api

import android.content.Context
import com.coppel.alfonsosotelo.coppelmarvel.utils.Globals
import com.coppel.alfonsosotelo.coppelmarvel.utils.toMd5Hash
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*


/**
 * This class is used to Intercept Http calls, you can add things to every request
 * like Tokens, Apikeys, etc.
 */
class HttpInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val builder = request.newBuilder()

        //adding Marvel api Mandatory Parameters
        builder.url(
            request.url().newBuilder().apply {
                val ts = Date().time.toString()
                addQueryParameter("apikey", Globals.MARVEL_PUBLIC_KEY)
                addQueryParameter("ts", ts)
                addQueryParameter("hash", (ts+Globals.MARVEL_PRIVATE_KEY+Globals.MARVEL_PUBLIC_KEY).toMd5Hash())
            }.build()
        )

        request = builder.build()

        return chain.proceed(request)
    }
}