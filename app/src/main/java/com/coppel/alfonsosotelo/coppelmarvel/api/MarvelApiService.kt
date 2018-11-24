package com.coppel.alfonsosotelo.coppelmarvel.api

import android.content.Context
import com.coppel.alfonsosotelo.coppelmarvel.entities.Character
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Marvel Api REST methods should be added here
 */
interface MarvelApiService {

    @GET("v1/public/characters?limit=100")
    fun getCharacters(): Observable<RequestClass<Character>> // retrieve a list of characters limited by 100

    @GET("v1/public/characters?limit=100")
    fun getCharacters(@Query("offset") offset: Long): Observable<RequestClass<Character>> // retrieve a list of characters limited by 100 and starting by a offset


    /**
     * This class is used to handle the retrieved Marvel Info
     */
    class RequestClass<T> {
        var code: Int = 0
        var status: String = ""
        var data = RequestData<T>()

        fun getResults(): List<T> {
            return data.results
        }

        class RequestData<T> {
            var offset: Int = 0
            var limit: Int = 0
            var total: Int = 0
            var count: Int = 0
            var results: List<T> = listOf()
        }
    }
    companion object {
        /**
         * This retrieve an instance of MarvelApiService
         */
        fun get(context: Context): MarvelApiService {
            return ApiProduction(context).provideService(MarvelApiService::class.java)
        }
    }
}