package com.example.testcoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testcoroutine.api2.WeatherApi
import com.example.testcoroutine.api2.model.WeatherResponse
import kotlinx.android.synthetic.main.activity_main4.*
import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.channels.Channel


class Main4Activity : AppCompatActivity() {

    private var activity4Job = Job()
    private val activity4Scope = CoroutineScope(Dispatchers.Main + activity4Job)

    private val testChannel: Channel<String> = Channel()

    private val testChannel0: Channel<Pair<String, Int>> = Channel()

    private var param = true


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

                //                val handler4 = InputHandler4(this)
//
//                launch { innerFun1(handler4) }
//
//                val t1 = async { innerFun2(handler4) }
//
//                tvLogs4.text ="${tvLogs4.text} ${t1.await().title}\n"

                Log.d("TAG", "1) Before first launch")
                launch {
                    Log.d("TAG", "1) Inside first launch")
                    for (i in 0 until 5) {
                        Log.d("TAG", "1) Inside for loop of the first launch $i")
                        val t1 = withContext(Dispatchers.IO) { async { WeatherApi.loadSpbWeather() } }
                        val t2 = t1.await().third.get().take(10)
                        val t3 = "$i) $t2"
                        testChannel0.send(Pair(t3, i))
                    }
                }

                Log.d("TAG", "2) Before second launch")
                launch {
                    Log.d("TAG", "2) Inside second launch")
                    while (param) {
                        Log.d("TAG", "2) Inside while loop of the second launch")
                        val t1 = async { testChannel0.receive() }
                        val t2 = t1.await().first
                        val t3 = t1.await().second
                        if(t3 > 2) testChannel.send(t2)
                        if(t3 == 4) param = false
                    }
                }

                Log.d("TAG", "3) Before third launch")
                launch {
                    Log.d("TAG", "3) Inside while loop of the third launch")
                    while (param) {
                        Log.d("TAG", "3) Inside while loop of the third launch")
                        val t1 = async { testChannel.receive() }
                        tvLogs4.text = "${tvLogs4.text} \n ${t1.await().take(10)} \n"
                    }
                }

            }
            Log.d("TAG", "end")
        }
    }



    @InternalCoroutinesApi
    private suspend fun innerFun1(handler4: InputHandler4) {
        Log.d("TAG", "innerFun1()")
        for (i in 0 until 5) {
            val t1 = withContext(Dispatchers.IO) { async { WeatherApi.loadSpbWeather() } }
            val t2 = t1.await()
            handler4.actor1.send(Pair(t2.third.get(), i))
        }
    }

    private suspend fun innerFun2(handler4: InputHandler4): WeatherResponse =
        handler4.channel1.receive()


    private suspend fun innerFun3(handler4: InputHandler4) = select<WeatherResponse> {
        handler4.channel1.onReceive
    }


//    start() fun execution

//    D/TAG: 1) Before first launch
//    D/TAG: 2) Before second launch
//    D/TAG: 3) Before third launch
//    D/TAG: 1) Inside first launch
//    D/TAG: 1) Inside for loop of the first launch 0
//    D/TAG: 2) Inside second launch
//    D/TAG: 2) Inside while loop of the second launch
//    D/TAG: 3) Inside while loop of the third launch
//    D/TAG: 3) Inside while loop of the third launch
//    D/TAG: 1) Inside for loop of the first launch 1
//    D/TAG: 2) Inside while loop of the second launch
//    D/TAG: 1) Inside for loop of the first launch 2
//    D/TAG: 2) Inside while loop of the second launch
//    D/TAG: 1) Inside for loop of the first launch 3
//    D/TAG: 2) Inside while loop of the second launch
//    D/TAG: 1) Inside for loop of the first launch 4
//    D/TAG: 2) Inside while loop of the second launch
//    D/TAG: 3) Inside while loop of the third launch
//    D/TAG: end

}
