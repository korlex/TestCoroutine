package com.example.testcoroutine

import android.util.Log
import com.example.testcoroutine.api2.model.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.selects.whileSelect

class InputHandler2(scope: CoroutineScope): CoroutineScope by scope {

    private val infoChannel: Channel<String> = Channel()
    private val channel1: Channel<String> = Channel()
    private val channel2: Channel<WeatherResponse> = Channel()


    // блокировка каналов ?? ohhh ....

    init {
        fun2()
    }

    fun fun1(string: String) {
        launch {
            Log.d("TAG","1")
            channel1.send(string)
            //infoChannel.send("initial data sent to channel1")
        }
    }

    fun fun2() {
        launch {
            Log.d("TAG","2")
            val t2 = fun3()
            val t3 = Gson().fromJson(t2, WeatherResponse::class.java)
            channel2.send(t3)
            //infoChannel.send("data parsed and send to channel2")
            //ddd(t3)
        }
    }


    suspend fun fun3(): String {
        return select<String> {

        }
    }


    private suspend fun ddd(wr: WeatherResponse) {
        Log.d("TAG","3")
        channel2.send(wr)

    }

    fun getChannel2(): Channel<WeatherResponse>  = channel2

    fun getInfoChannel(): Channel<String> = infoChannel
}