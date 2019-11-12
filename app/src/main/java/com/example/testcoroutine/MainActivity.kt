package com.example.testcoroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.testcoroutine.api.GithubApi
import com.example.testcoroutine.api.GithubService
import com.example.testcoroutine.api.data.Repo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val testChannel1: Channel<String> = Channel()
    private val testChannel2: Channel<String> = Channel()


    private lateinit var githubService: GithubService
    private lateinit var inputTestHandler: InputTestHandler
    private lateinit var parseTestHandler: ParseTestHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        githubService = GithubApi.createGithubService(this)
        inputTestHandler = InputTestHandler(this)
        parseTestHandler = ParseTestHandler(this, inputTestHandler)

//        works!
//        val strings = listOf<String>("hey1", "hey2", "hey3")
//        launch {
//            launch { for (string in strings) { testChannel1.send(string) } }
//            repeat(5) { tvLogs.text = testChannel1.receive() }
//        }

//        not working!
//        val strings = listOf<String>("hey1", "hey2", "hey3")
//        launch {
//            Log.d("TAG", "hey1")
//            secTryInOnCreate(strings)
//            repeat(5) { tvLogs.text = testChannel1.receive() }
//        }


//        not working!
//        btnFire.setOnClickListener { thirdTry("hey") }
//        launch {
//         tvLogs.text = "${tvLogs.text} ${testChannel1.receive()}"
//        }


//        not working!
//        launch {
//            btnFire.setOnClickListener { fourthTry("hey") }
//            tvLogs.text = "${tvLogs.text} ${testChannel1.receive()}"
//        }


//        works!!
//        btnFire.setOnClickListener { sixthTry("hey") }

//        not working!!
//        btnFire.setOnClickListener { seventhTry("hey7") }

//        receive works!! poll() not
//        btnFire.setOnClickListener { eightTry1("hey") }

//        not working!! temp change
//        btnFire.setOnClickListener { ninthTry1("firstCounter") }


//        not working!!
//        btnFire.setOnClickListener { tenthTry1() }


//        works!!
//        btnFire.setOnClickListener { eleventhTry1("hey") }

//        not working !! и с ланчом и без в twelveTry2
//        btnFire.setOnClickListener { twelvethTry1("hey") }

//        not working !!
//        btnFire.setOnClickListener { therteenTry1("hey") }
//        therteenTry3()

//        not working !!
//        btnFire.setOnClickListener { fourteenTry0("hey") }


        btnFire.setOnClickListener { f15Try0(5) }
    }

//    Log.d("TAG","1")


    override fun onStop() {
        super.onStop()
        job.cancel()
    }



    private fun hardTry1() {
        // coroutineScope ?
        tvLogs.text = "hardTry1() start"
        launch {
            coroutineScope {
                withContext(Dispatchers.IO) {
                    for (i in 0 until 2) {
                        val response = githubService.getLocationsByQuery().await()
                        inputTestHandler.addToFirstChannel(response)
                    }
                }

                withContext(Dispatchers.Default) {
                    parseTestHandler.addToSecondChannel()
                }

                parseTestHandler.secondChannel.consumeEach {
                    if(it is Repo) {
                        tvLogs.text = it.archive_url
                    } else {
                        tvLogs.text = "exception"
                    }
                }
            }
        }
    }






    private fun f15Try0(count: Int) {
        val list = mutableListOf<String>()
        for (i in 0..count) {
            list.add("$i hey")
        }
        f15Try1(list)
    }


    private fun f15Try1(list: List<String>) {
        launch {


            coroutineScope {

                launch {
                    //testChannel1.send("channel1 size = ${testChannel1.toList().size}")  no
//                delay(1000)
//                testChannel1.send("to chan1")
//                    for (item in list) testChannel1.send(item)
                    ch1Op(list)

                }

                launch {
                    //                delay(1000) yes
//                val test = testChannel1.receive()  yes
                    //testChannel2.send("$test  channel2 size = ${testChannel2.toList().size}") no
//                testChannel2.send("$test chan2")  yes

//                    testChannel1.consumeEach {
//                        val t1 = "$it , to chan2"
//                        testChannel2.send(t1)
//                    }

                    ch2Op()

                }

                testChannel2.consumeEach {
                    val t2 = "$it out"
                    tvLogs.text = "${tvLogs.text} $t2"
                }

            }

            //tvLogs.text = "${tvLogs.text} ${testChannel2.receive()}"



        }
    }


    private suspend fun ch1Op(list: List<String>) {
        for (item in list) {
            testChannel1.send(item)

        }
        Log.d("TAG","here1")
    }


    private suspend fun ch2Op() {
        testChannel1.consumeEach {
            val t1 = "$it , to chan2"
            testChannel2.send(t1)
        }
        Log.d("TAG","here2")

    }


//    private fun fourteenTry0(string: String) {
//        launch {
//            val strings = fourteenTry1("hey")
//            strings.consumeEach { tvLogs.text = it }
//        }
//    }
//
//    private fun CoroutineScope.fourteenTry1(string: String): ReceiveChannel<String> = produce {
//        var counter = 0
//        counter++
//        send("$counter $string")
//    }
//
//    private fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
//        for (x in 1..5) send(x * x)
//    }


//    private fun therteenTry1(string: String) {
//        launch {
//            testChannel1.send("$string | send to channel")
//        }
//    }
//
//    private fun therteenTry2(string: String) {
//        launch {
//            delay(500)
//            testChannel1.send("$string | send to channel")
//        }
//    }
//
//    private fun therteenTry3() {
//        launch {
//            tvLogs.text = "${tvLogs.text} ${testChannel1.receive()} | from Channel"
//        }
//    }


//    private fun twelvethTry1(string: String) {
//        Log.d("TAG","1")
//        launch {
//            Log.d("TAG","2")
//            twelvethTry2(string)
//            Log.d("TAG","3")
//            tvLogs.text = "${tvLogs.text} ${testChannel1.receive()} | get from channel"
//            Log.d("TAG","4")
//
//        }
//    }
//
//    private suspend fun twelvethTry2(string: String) {
//        Log.d("TAG","5")
//        coroutineScope {
//            Log.d("TAG","6")
//            launch {
//                Log.d("TAG","7")
//                delay(500)
//                Log.d("TAG","8")
//                testChannel1.send("$string | put in channel")
//                Log.d("TAG","9")
//            }
//        }
//    }


//    private fun eleventhTry1(string: String) {
//        launch {
//            //tvLogs.text = "${tvLogs.text} ${eleventhTry2(string).await()} /n"
//            val test = eleventhTry2("hey").await()
//            tvLogs.text = "${tvLogs.text} $test /n"
//        }
//    }
//
//    private suspend fun eleventhTry2(string: String): Deferred<String> =
//        coroutineScope { async { eleventhTry3(string) } }
//
//    private suspend fun eleventhTry3(string: String): String {
//        delay(500)
//        return "$string modified"
//    }


//    private fun tenthTry1() {
//        launch {
//            tenthTry2()
//            tvLogs.text = testChannel1.receive()
//        }
//    }
//
//    private suspend fun tenthTry2() {
//        coroutineScope {
//            launch {
//                testChannel1.send("hey")
//            }
//        }
//    }


//    private fun ninthTry1(string: String) {
//        launch {
//            ninthTry2(string)
//            val strFromFirstChan = testChannel1.receive()
//            tvLogs.text = strFromFirstChan
//            ninthTry3(strFromFirstChan)
//            tvLogs.text = testChannel2.receive()
//        }
//    }
//
//    private fun ninthTry2(string: String) {
//        launch {
//            testChannel1.send("$string  secCounter")
//        }
//    }
//
//    private suspend fun ninthTry3(string: String) {
//        launch {
//            testChannel2.send("$string thirdCounter")
//        }
//    }


//    private fun eightTry1(str: String) {
//        launch {
//            eightTry2(str)
//            tvLogs.text = testChannel1.receive()
//        }
//    }
//
//    private fun eightTry2(str: String) {
//        launch{
//            testChannel1.send(str)
//        }
//    }

//    private fun seventhTry(str: String) {
//        launch {
//            testChannel1.send(str)
//            tvLogs.text = testChannel1.poll()
//        }
//    }


//    private fun sixthTry(str: String) {
//        launch {
//            launch { testChannel1.send(str) }
//            tvLogs.text = testChannel1.poll()
//        }
//    }


//    private fun fourthTry(string: String) {
//        launch {
//            testChannel1.send(string)
//        }
//    }


//    private fun thirdTry(string: String) {
//        launch {
//            testChannel1.send(string)
//        }
//    }


//    private suspend fun secTryInOnCreate(strings: List<String>) {
//        Log.d("TAG", "hey2")
//        for (string in strings) { testChannel1.send(string) }
//    }

//    private fun runInitialChain() {
//        launch {
//            Log.d("TAG", "hey1")
//            testChannel1.send("runInitialChain")
//            tvLogs.text = testChannel2.receive()
//        }
//    }
//
//    private suspend fun runCoroutine1() {
//        withContext(Dispatchers.IO) {
//            for (string in testChannel1) {
//                delay(1000)
//                testChannel2.send("$counter ) $string ; from testChannel 1 to testChannel2 /n")
//            }
//        }
//    }

//    private suspend fun runCoroutine2() {
//        withContext<String>(Dispatchers.IO) {
//            for (string in testChannel2) {
//                delay(1000)
//            }
//            "sd"
//        }
//    }
//
//    private suspend fun runCoroutineTest2(): String {
//        val t1: String = withContext(Dispatchers.IO) {
//            delay(1000)
//            "sdsd"
//        }
//
//        val t2 = withContext<String>(Dispatchers.IO) { "sd" }
//    }

}