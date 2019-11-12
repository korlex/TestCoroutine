package com.example.testcoroutine.api2

import com.example.testcoroutine.api2.model.WeatherResponse
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.ResponseResultOf
import com.github.kittinunf.fuel.httpGet

object WeatherApi : WeatherService {
    init {
        FuelManager.instance.basePath = "https://www.metaweather.com"
        //FuelManager.instance.addRequestInterceptor(loggingInterceptor())
    }

    override fun loadSpbWeather(): ResponseResultOf<String> =
        "api/location/2123260"
            .httpGet()
            .responseString()

}