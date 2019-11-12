package com.example.testcoroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import okhttp3.ResponseBody
import java.lang.Exception

class InputTestHandler(scope: CoroutineScope) : CoroutineScope by scope {

  var firstChannel: Channel<Any> = Channel()


  suspend fun addToFirstChannel(responseBody: ResponseBody) {
    if(responseBody.string().isNotEmpty()) {
      firstChannel.send(responseBody.string())
    } else {
      firstChannel.send(Exception("oh1"))
    }
  }

}