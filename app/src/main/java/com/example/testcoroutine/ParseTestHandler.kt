package com.example.testcoroutine

import com.example.testcoroutine.api.data.Repo
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import java.lang.Exception

class ParseTestHandler(scope: CoroutineScope, val inputTestHandler: InputTestHandler) : CoroutineScope by scope {
    var secondChannel: Channel<Any> = Channel()

    suspend fun addToSecondChannel() {
        val test = inputTestHandler.firstChannel.receive()
        if(test == String()) {
            val t1 = Gson().fromJson(test as String, Repo::class.java)
            secondChannel.send(t1)
        } else {
            secondChannel.send(Exception("oh2"))
        }
    }
}