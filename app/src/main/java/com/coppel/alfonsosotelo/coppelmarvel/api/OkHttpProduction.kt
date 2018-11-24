package com.coppel.alfonsosotelo.coppelmarvel.api

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * This class is used to make an OkHttpClient, and add options to it
 */
class OkHttpProduction {
    fun OkHttpProduction() = Unit

    companion object getOkHttpClient {
        val DISK_CACHE_SIZE = 50 * 1024 * 1024 // 50MB

        /**
         * Adding cache size, timeout, logging interceptor, and HttpInterceptor
         */
        fun getOkHttpClient(context: Context, debug: Boolean, authorization: Boolean = true): OkHttpClient {
            // Install an HTTP cache in the context cache directory.
            val cacheDir = File(context.cacheDir, "http")
            val cache = Cache(cacheDir, DISK_CACHE_SIZE.toLong())

            val builder = OkHttpClient.Builder().cache(cache)
            builder.connectTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES)

            if (debug) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(loggingInterceptor)
            }

            if (authorization) {
                builder.addInterceptor(HttpInterceptor(context))
            } else {
                builder.addInterceptor { chain ->
                    var request = chain.request()

                    val requestBuilder = request.newBuilder()

                    request = requestBuilder.build()

                    val response: Response = chain.proceed(request)

                    response
                }
            }

            return builder.build()
        }
    }
}