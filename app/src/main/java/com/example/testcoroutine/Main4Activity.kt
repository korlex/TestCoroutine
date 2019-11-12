package com.example.testcoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testcoroutine.api2.WeatherApi
import com.example.testcoroutine.api2.model.WeatherResponse
import kotlinx.android.synthetic.main.activity_main4.*
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select

class Main4Activity : AppCompatActivity() {

    private var activity4Job = Job()
    private val activity4Scope = CoroutineScope(Dispatchers.Main + activity4Job)

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        btnFire4.setOnClickListener { start() }
    }


    @InternalCoroutinesApi
    private fun start() {
        tvLogs4.text = "start() call\n"
        activity4Scope.launch {
            coroutineScope {
                val handler4 = InputHandler4(this)

                launch { innerFun1(handler4) }

                val t1 = async { innerFun2(handler4) }

                tvLogs4.text ="${tvLogs4.text} ${t1.await().title}\n"

            }
        }
    }

    @InternalCoroutinesApi
    private suspend fun innerFun1(handler4: InputHandler4) {
        for (i in 0 until 5) {
            val t1 = withContext(Dispatchers.IO) { async { WeatherApi.loadSpbWeather() } }
            val t2 = t1.await()
            //handler4.actor1.send(Pair(t1.await().third.get(), i))
            select<Pair<String, Int>> { handler4.actor1.onSend(Pair(t2.third.get(), i)) { Log.d("TAG", "hey") } }
        }
    }

    private suspend fun innerFun2(handler4: InputHandler4): WeatherResponse = handler4.channel1.receive()

}
