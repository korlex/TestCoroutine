package com.example.testcoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testcoroutine.api.GithubApi
import com.example.testcoroutine.api.GithubService
import com.example.testcoroutine.api2.WeatherApi
import com.example.testcoroutine.api2.model.WeatherResponse
import com.github.kittinunf.fuel.core.ResponseResultOf
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.selects.SelectInstance
import kotlinx.coroutines.selects.select

class Main2Activity : AppCompatActivity() {

    private var activity2Job = Job()
    private val activity2Scope = CoroutineScope(Dispatchers.Main + activity2Job)
    //private val inputHandler2: InputHandler2 = InputHandler2()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        btnFire2.setOnClickListener { receiveWeather() }
    }


    private fun receiveWeather() {
        tvLogs2.text = "receiveWeather() call\n"
        activity2Scope.launch {
            coroutineScope {
                val inputHandler2 = InputHandler2(this)
                for(i in 0 until 2) {
                    val t1 = withContext(Dispatchers.IO) { async { WeatherApi.loadSpbWeather() } }
                    inputHandler2.fun1(t1.await().third.get())
                }

                //tvLogs2.text = "${tvLogs2.text} ${test3(inputHandler2).title}\n"

                tvLogs2.text = "${tvLogs2.text} ${inputHandler2.getInfoChannel().receive()}"

            }
        }
    }

    private suspend fun test1(inputHandler2: InputHandler2) {
        tvLogs2.text = "${tvLogs2.text} ${inputHandler2.getChannel2().receive().title}"
    }

    private suspend fun test2(inputHandler2: InputHandler2) {
        tvLogs2.text = "${tvLogs2.text} ${inputHandler2.getInfoChannel().receive()}"
    }

    private suspend fun test3(inputHandler2: InputHandler2): WeatherResponse {
        return select<WeatherResponse> {
            inputHandler2.getChannel2().onSend
        }
    }

//    private fun receiveRepos() {
//        activity2Scope.launch {
//            tvLogs2.text = "receiveRepos() call \n"
//            val t1 = withContext(Dispatchers.IO) { githubService.getLocationsByQuery() }
//            tvLogs2.text = "${tvLogs2.text} ${t1.await().string()}"
//        }
//    }

}
