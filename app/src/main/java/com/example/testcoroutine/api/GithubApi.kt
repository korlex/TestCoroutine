package com.example.testcoroutine.api

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object GithubApi {

    const val BASE_URL = "https://api.github.com/"

    fun createGithubService(context: Context): GithubService {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(ChuckInterceptor(context))
            .build()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(GithubService::class.java)
    }

}