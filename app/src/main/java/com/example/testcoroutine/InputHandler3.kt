package com.example.testcoroutine

import android.util.Log
import com.example.testcoroutine.api2.model.WeatherResponse
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.selects.*
import kotlinx.coroutines.sync.Mutex

class InputHandler3(scope: CoroutineScope) : CoroutineScope by scope {

    @InternalCoroutinesApi
    private val actor1: SendChannel<Pair<String, Int>> = actor {
        consumeEach {
            Log.d("TAG", "1")
            val t3 = Gson().fromJson(it.first, WeatherResponse::class.java)
            t3.title = "${t3.title} ${it.second}"
            channel1.send(t3)
        }
    }

    private val channel1: Channel<WeatherResponse> = Channel(Channel.UNLIMITED)

    private val testChannel3: Channel<Pair<String, Int>> = Channel(Channel.UNLIMITED)


    init {
        launch {
            testChannel3.consumeEach {
                Log.d("TAG", "1")
                val t3 = Gson().fromJson(it.first, WeatherResponse::class.java)
                t3.title = "${t3.title} ${it.second}"
                channel1.send(t3)
            }
        }
    }


//    @InternalCoroutinesApi
//    suspend fun fun1(str: String, count: Int): WeatherResponse {
//        return select<WeatherResponse> {
//            actor1.onSend(Pair(str, count)) {
//                Log.d("TAG", "1")
//                channel1.receive()
//            }
//        }
//    }

//    suspend fun fun2(str: String, count: Int) = coroutineScope {
//        async {
//            if(count >= 2) fun3(str, count) else null
//
//        }
//    }
//
//    fun fun3(str: String, count: Int): WeatherResponse {
//        val t3 = Gson().fromJson(str, WeatherResponse::class.java)
//        t3.title = "${t3.title} ${count}"
//        return t3
//    }


    suspend fun fun2(str: String, count: Int): WeatherResponse {
        return select {
            testChannel3.onSend(Pair(str, count)) {
                channel1.receive()
            }
        }
    }


}