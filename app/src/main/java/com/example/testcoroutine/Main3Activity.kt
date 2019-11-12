package com.example.testcoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.testcoroutine.api2.WeatherApi
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex

class Main3Activity : AppCompatActivity() {

    private var activity3Job = Job()
    private val activity3Scope = CoroutineScope(Dispatchers.Main + activity3Job)

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        btnFire3.setOnClickListener { receiveWeather32() }
    }

    @InternalCoroutinesApi
    private fun receiveWeather3() {
        tvLogs3.text = "receiveWeather3() call\n"
        activity3Scope.launch {
            coroutineScope {
                val handler3 = InputHandler3(this)
                for(i in 0 until 5) {
//                    val t1 = withContext(Dispatchers.IO) { async { WeatherApi.loadSpbWeather() } }
//                    //handler3.fun1(t1.await().third.get())
//                    val t2 = async { handler3.fun1(t1.await().third.get(), i) }
//                    tvLogs3.text ="${tvLogs3.text} ${t2.await().title}\n"
                    innerFun3(handler3, i)
                    if(i ==1) innerFun3(handler3, i)
                }

            }
        }
    }

    @InternalCoroutinesApi
    private suspend fun innerFun3(handler3: InputHandler3, count: Int) {
        val t1 = withContext(Dispatchers.IO) { async { WeatherApi.loadSpbWeather() } }
        //handler3.fun1(t1.await().third.get())
//        val t2 = withContext(Dispatchers.Main) { handler3.fun1(t1.await().third.get(), count) }
//        tvLogs3.text ="${tvLogs3.text} ${t2.title}\n"
    }


    @InternalCoroutinesApi
    private fun receiveWeather32() {
        tvLogs3.text = "receiveWeather32() call\n"
        activity3Scope.launch {
            coroutineScope {
                val handler3 = InputHandler3(this)
                for(i in 0 until 4) {
                    val t1 = withContext(Dispatchers.IO) { async { WeatherApi.loadSpbWeather() } }
                    //handler3.fun1(t1.await().third.get())
                    val t2 = async { handler3.fun2(t1.await().third.get(), i) }
                    tvLogs3.text ="${tvLogs3.text} ${t2.await().title}\n"
                }
            }
        }
    }

    private suspend fun innerFun32() {

    }


}
