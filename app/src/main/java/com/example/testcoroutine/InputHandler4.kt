package com.example.testcoroutine

import android.util.Log
import com.example.testcoroutine.api2.model.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select

class InputHandler4(scope: CoroutineScope): CoroutineScope by scope {

    @InternalCoroutinesApi
    val actor1: SendChannel<Pair<String, Int>> = actor {
        consumeEach {
            Log.d("TAG", "1")
            val t3 = Gson().fromJson(it.first, WeatherResponse::class.java)
            t3.title = "${t3.title} ${it.second}"
            channel1.send(t3)
        }
    }

    val channel0: Channel<Pair<String, Int>> = Channel(Channel.UNLIMITED)

    val channel1: Channel<WeatherResponse> = Channel(Channel.UNLIMITED)


}