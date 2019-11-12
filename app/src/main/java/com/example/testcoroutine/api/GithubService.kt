package com.example.testcoroutine.api

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET

interface GithubService {
    @GET("/users/square/repos?sort=updated")
    fun getLocationsByQuery(): Deferred<ResponseBody>
}