package com.mvvm.localtest.data.source.remote

import com.mvvm.localtest.data.Task
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiCall {

    @GET("bOGtZGYtxK?indent=2")
    suspend fun getAllData(): Response<List<Task>>

    companion object {
        operator fun invoke(): ApiCall {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.json-generator.com/api/json/get/")
                .build()
                .create(ApiCall::class.java)
        }
    }
}