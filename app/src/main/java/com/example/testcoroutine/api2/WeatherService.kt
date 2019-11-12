package com.example.testcoroutine.api2

import com.example.testcoroutine.api2.model.WeatherResponse
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.ResponseResultOf

interface WeatherService {
    fun loadSpbWeather(): ResponseResultOf<String>

}