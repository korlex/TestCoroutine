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

/**
 Реализация бесконечных запросов в сеть с остановкой и продолжением? через 3 канала (самый проффесиональный чтоли) )

 в OnCreate() идёт вызов launchMainCoroutin()

 - идёт запуск главного корутина

 - в нём запускаются ещё 3

 - в них while() , но посколькольку в каждом while есть блокирующий channel receive, то они постоянно находяся в ожидании


 На кнопки (start/stop) идёт отправка в commandChannel - первый внутренний корутин ловит данные - понимает , что ему делать -

 запускает бесконечный while или отменяет его



 Рассмотрим 1ый внутренний корутин

 в нём инициализируется job , чтобы мы могли взаимодействовать с "sub - sub корутином"
 далее while(true)  где идёт ожидание комманды
 как только команда приходит - , то идёт её рассмотрение

 Если start, то мы создаём корутин , если stop , то уничтожаем прошлый


 ------------------------------

 Другие возможные варианты ?

 просто на btn`s (start/stop)
 запуск главного корутина и его cancel

 (так себе?)


 Может есть и ещё другин варианты , но на данный момент, смомневаюсь , что они лучше будут чем этот

 */


class Main4Activity : AppCompatActivity() {

    private var activity4Job = Job()
    private val activity4Scope = CoroutineScope(Dispatchers.Main + activity4Job)

    private val commandChannel: Channel<String> = Channel()
    private val testChannel0: Channel<Pair<String, Int>> = Channel()
    private val testChannel: Channel<String> = Channel()


    private var param = true



    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        btnStart.setOnClickListener { sendCommand(START) }
        btnStop.setOnClickListener { sendCommand(STOP) }
        launchMainCoroutin()
    }


    @InternalCoroutinesApi
    private fun launchMainCoroutin() {
        tvLogs4.text = "launchMainCoroutin() call\n"
        activity4Scope.launch {
            coroutineScope {

                Log.d("TAG", "1) Before first launch")
                launch {

                    var j1: Job? = null

                    while (true) {
                        val t1: String = commandChannel.receive()
                        if(t1 == START) {

                            j1 = launch {
                                while (true) {
                                    val t2 = withContext(Dispatchers.IO) { async { WeatherApi.loadSpbWeather() } }
                                    val t3 = t2.await().third.get().take(10)
                                    testChannel0.send(Pair(t3, 123))
                                }
                            }
                        } else {
                            j1?.cancel()
                        }
                    }

                }

                Log.d("TAG", "2) Before second launch")
                launch {
                    Log.d("TAG", "2) Inside second launch")
                    while (param) {
                        Log.d("TAG", "2) Inside while loop of the second launch")
                        val t1 = testChannel0.receive()
                        val t2 = t1.first
                        testChannel.send(t2)
                    }
                }

                Log.d("TAG", "3) Before third launch")
                launch {
                    Log.d("TAG", "3) Inside while loop of the third launch")
                    while (true) {
                        Log.d("TAG", "3) Inside while loop of the third launch")
                        val t1 = testChannel.receive()
                        val t2 = t1.take(10)
                        tvLogs4.text = "${tvLogs4.text} \n $t2 \n"
                    }
                }

            }
        }
    }



    private fun sendCommand(command: String) {
        Log.d("TAG", "SEND COMMAND $command")
        activity4Scope.launch { commandChannel.send(command) }
    }


    companion object {
        private const val START = "start"
        private const val STOP = "stop"
    }

}
