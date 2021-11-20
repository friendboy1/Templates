package com.artofmainstreams.examples

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import org.junit.Test
import kotlin.coroutines.coroutineContext

/**
 * Примеры использования flow
 */
class FlowTest {
    // region Channels
    @Test
    fun `channel test`() {
        runBlocking {
            val channel = Channel<Int>()
            launch {
                repeat(5) { index ->
                    delay(1000)
                    println("Producing next one")
                    channel.send(index * 2)
                }
                channel.close()
            }
            launch {
                repeat(5) {
                    val received = channel.receive()
                    println(received)
                }
                // or
                // channel.consumeEach { element ->
                //     println(element)
                // }
            }
        }
    }

    @Test
    fun `produce test`() {
        runBlocking {
            val channel = produce {
                repeat(5) { index ->
                    println("Producing next one")
                    delay(1000)
                    send(index * 2)
                }
            }
            for (element in channel) {
                println(element)
            }
        }
    }

    @Test
    fun `unlimited channel type`() {
        runBlocking {
            val channel = produce(capacity = Channel.UNLIMITED) {
                repeat(5) { index ->
                    send(index * 2)
                    delay(100)
                    println("Sent")
                }
            }
            delay(1000)
            for (element in channel) {
                println(element)
                delay(1000)
            }
        }
    }

    @Test
    fun `buffered channel type`() {
        runBlocking {
            val channel = produce(capacity = 3) {
                repeat(5) { index ->
                    send(index * 2)
                    delay(100)
                    println("Sent")
                }
            }
            delay(1000)
            for (element in channel) {
                println(element)
                delay(1000)
            }
        }
    }

    @Test
    fun `rendezvous channel type`() {
        runBlocking {
            val channel = produce {
                // or produce(capacity = Channel.RENDEZVOUS) {
                repeat(5) { index ->
                    send(index * 2)
                    delay(100)
                    println("Sent")
                }
            }
            delay(1000)
            for (element in channel) {
                println(element)
                delay(1000)
            }
        }
    }

    @Test
    fun `conflated channel type`() {
        runBlocking {
            val channel = produce(capacity = Channel.CONFLATED) {
                repeat(5) { index ->
                    send(index * 2)
                    delay(100)
                    println("Sent")
                }
            }
            delay(1000)
            for (element in channel) {
                println(element)
                delay(1000)
            }
        }
    }

    @Test
    fun `onBufferOverflow drop oldest`() {
        runBlocking {
            val channel = Channel<Int>(
                capacity = 2,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
            launch {
                repeat(5) { index ->
                    channel.send(index * 2)
                    delay(100)
                    println("Sent")
                }
                channel.close()
            }
            delay(1000)
            for (element in channel) {
                println(element)
                delay(1000)
            }
        }
    }

    @Test
    fun `onUndeliveredElement test`() {
        runBlocking {
            val channel = Channel<Int>(capacity = 3) { resource ->
                println("Не доставлен")
            }
//          or
//          val channel2 = Channel<Int>(
//              capacity = 3,
//              onUndeliveredElement = { resource ->
//                  println("Не доставлен")
//              }
//          )
            // Producer code
            launch {
                val resourceToSend = 8
                channel.send(resourceToSend)
                channel.send(resourceToSend)
            }
            // Consumer code
            val resourceReceived = channel.receive()
            try {
                println("Полученный ресурс: $resourceReceived")
                channel.cancel()
            } finally {
                println("Закрыли")
            }
        }
    }

    @Test
    fun `multiple coroutines from one channel`() {
        runBlocking {
            val channel = produce {
                repeat(10) {
                    delay(100)
                    send(it)
                }
            }
            repeat(3) { id ->
                delay(10)
                launch {
                    for (msg in channel) {
                        println("#$id received $msg")
                    }
                }
            }
        }
    }

    @Test
    fun `multiple coroutines to one channel`() {
        runBlocking {
            val channel = Channel<String>()
            launch {
                while (true) {
                    delay(200L)
                    channel.send("foo")
                }
            }
            launch {
                while (true) {
                    delay(500L)
                    channel.send("BAR!")
                }
            }
            repeat(50) {
                println(channel.receive())
            }
            coroutineContext.cancelChildren()
        }
    }

    @Test
    fun `merge multiple channels into one`() {
        val channels: List<ReceiveChannel<*>> = listOf(Channel<String>(), Channel<Int>())
        runBlocking {
            produce {
                for (channel in channels) {
                    launch {
                        for (elem in channel) {
                            send(elem)
                        }
                    }
                }
            }
        }
    }
    // endregion Channels

    // region Flow
    @Test
    fun `Flow vs Channel`() {
        runBlocking {
            /**
             * Channel - горячий поток, вызывается в отдельной корутине и если никто не подпишется,
             * будет ждать
             */
            val channel = produce {
                var i = 0
                while (i < 10) {
                    i += 1
                    send(i)
                }
            }
        }
        val flow = flow {
            var i = 0
            while (i < 10) {
                i += 1
                emit(i)
            }
        }
    }

    @Test
    fun `flow builders`() {
        runBlocking {

            flowOf(1, 2, 3, 4, 5)
                .collect { print(it) } // 12345

            emptyFlow<Int>()
                .collect { print(it) } // (nothing)

            listOf(1, 2, 3, 4, 5)
                // or setOf(1, 2, 3, 4, 5)
                // or sequenceOf(1, 2, 3, 4, 5)
                .asFlow()
                .collect { print(it) } // 12345

            val function = suspend {
                // this is suspending lambda expression
                delay(1000)
                "UserName"
            }
            function.asFlow()
                .collect { println(it) }

            ::getUserName.asFlow()
                .collect { println(it) }

            flow {
                repeat(3) { num ->
                    delay(1000)
                    emit(num)
                }
            }
                .collect { println(it) }

            callbackFlow {
                val callback = object : Callback {
                    override fun onNextValue(value: Int) {
                        try {
                            trySendBlocking(value)
                        } catch (e: Exception) {
                            // Handle exception from the channel:
                            // failure in flow or premature closing
                        }
                    }

                    override fun onApiError(cause: Throwable) {
                        cancel(CancellationException("API Error", cause))
                    }

                    override fun onCompleted() = channel.close()
                }
                //api.register(callback)
                // дожидаемся завершения
                awaitClose {
                    //    api.unregister(callback)
                }
            }
        }
    }

    suspend fun getUserName(): String {
        delay(1000)
        return "UserName"
    }

    interface Callback {
        fun onNextValue(value: Int)
        fun onApiError(cause: Throwable)
        fun onCompleted(): Boolean
    }

    @Test
    fun `flow lifecycle functions`() {
        runBlocking {
            flow {
                present("flow builder", "Message")
                emit(1)
                emit(2)
                emit(3)
                emit(4)
            }
                .onEach { print("onEach: $it ") }
                .flowOn(CoroutineName("Name1")) // действует на поток вверх
                .map { it.toString() }  // processing
                .onStart { emit("Before") }
                .flowOn(CoroutineName("Name2"))
                .onCompletion { present("onCompletion", "Completed") }
                .flowOn(CoroutineName("Name3"))
                .onEmpty { emit("Something went wrong...") }
                .catch { println("Caught $it") } // только onCompletion вызовется, ловит сверху
                .collect {
                    present("collect", it)
                }
            flowOf("Another coroutine")
                .onEach { present("launchIn", it) }
                .launchIn(this + CoroutineName("Some"))
        }
    }

    suspend fun present(place: String, message: String) {
        val ctx = coroutineContext
        val name = ctx[CoroutineName]?.name
        println("[$name] $message on $place")
    }

    // region Processing
    @Test
    fun `flow map`() {
        runBlocking {
            flowOf(1, 2, 3) // [1, 2, 3]
                .map { it * it } // [1, 4, 9]
                .collect { print(it) } // 149
        }
    }

    @Test
    fun `flow filter`() {
        runBlocking {
            (1..10).asFlow() // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
                .filter { it <= 5 } // [1, 2, 3, 4, 5]
                .filter { it % 2 == 0 } // [2, 4]
                .collect { print(it) } // 24
        }
    }

    @Test
    fun `flow take first`() {
        runBlocking {
            ('A'..'Z').asFlow()
                .take(5) // [A, B, C, D, E]
                .collect { print(it) } // ABCDE
        }
    }

    @Test
    fun `flow drop first`() {
        runBlocking {
            ('A'..'Z').asFlow()
                .drop(20) // [U, V, W, X, Y, Z]
                .collect { print(it) } // UVWXYZ
        }
    }

    /**
     * Порядок не сохраняется
     */
    @Test
    fun `flow merge`() {
        runBlocking {
            val ints: Flow<Int> = flowOf(1, 2, 3)
            val doubles: Flow<Double> = flowOf(0.1, 0.2, 0.3)
            val together: Flow<Number> = merge(ints, doubles)
            print(together.toList())
            // [1, 0.1, 0.2, 0.3, 2, 3]
            // or [1, 0.1, 0.2, 0.3, 2, 3]
            // or [0.1, 1, 2, 3, 0.2, 0.3]
            // or any other combination
        }
    }

    /**
     * Попарно объединяются (последний теряется, если не нашёл пару)
     */
    @Test
    fun `flow zip`() {
        runBlocking {
            val flow1 = flowOf("A", "B", "C")
                .onEach { delay(400) }
            val flow2 = flowOf(1, 2, 3, 4)
                .onEach { delay(1000) }
            flow1.zip(flow2) { f1, f2 -> "${f1}_${f2}" }
                .collect { println(it) }
        }
    }

    /**
     * Попарно объединяются, берётся последний пришедший из каждого flow
     */
    @Test
    fun `flow combine`() {
        runBlocking {
            val flow1 = flowOf("A", "B", "C")
                .onEach { delay(400) }
            val flow2 = flowOf(1, 2, 3, 4)
                .onEach { delay(1000) }
            flow1.combine(flow2) { f1, f2 -> "${f1}_${f2}" }
                .collect { println(it) }
        }
    }

    /**
     * Терминальный оператор, который может быть применён для flow. Не прерывается
     */
    @Test
    fun `flow fold`() {
        runBlocking {
            val list = flowOf(1, 2, 3, 4)
                .onEach { delay(1000) }
            val res = list.fold(0) { acc, i -> acc + i }
            println(res)
        }
    }

    /**
     * Аналогично scan, но после каждого вычисления выдаёт элемент
     */
    @Test
    fun `flow scan`() {
        runBlocking {
            flowOf(1, 2, 3, 4)
                .onEach { delay(1000) }
                .scan(0) { acc, v -> acc + v }
                .collect { println(it) }
        }
    }

    /**
     * Последовательно добавляет новые элементы в новый flow
     */
    @Test
    fun `flow flatMapConcat`() {
        runBlocking {
            flowOf("A", "B", "C")
                .flatMapConcat { flowFrom(it) }
                .collect { println(it) }
        }
    }

    /**
     * Добавляет элементы во flow как придут (параллельно, по умолчанию 16 за раз)
     */
    @Test
    fun `flow flatMapMerge`() {
        runBlocking {
            flowOf("A", "B", "C")
                .flatMapMerge(concurrency = 2) { flowFrom(it) }
                .collect { println(it) }
        }
    }

    /**
     * Когда приходит новый элемент (в исходном списке), предыдущие забываются
     */
    @Test
    fun `flow flatMapLatest`() {
        runBlocking {
            flowOf("A", "B", "C")
                .flatMapLatest { flowFrom(it) }
                .collect { println(it) }
        }
    }

    @Test
    fun `terminal operators`() {
        runBlocking {
            val flow = flowOf(1, 2, 3, 4) // [1, 2, 3, 4]
                .map { it * it } // [1, 4, 9, 16]
            println(flow.first()) // 1
            println(flow.count()) // 4
            println(flow.reduce { acc, value -> acc * value }) // 576
            println(flow.fold(0) { acc, value -> acc + value }) // 30
        }
    }

    fun flowFrom(elem: String) = flowOf(1, 2, 3)
        .onEach { delay(1000) }
        .map { "${it}_${elem} " }
    // endregion Processing

    // region SharedFlow and StateFlow
    /**
     * Т.к. мы подписываемся на MutableSharedFlow, а он не закрывается, программа не завершается,
     * т.к. ожидаем завершения работы корутин. Можно только отменить их. Параметр replay это
     * cache, который сбрасывается с помощью resetReplayCache
     */
    @Test
    fun `mutableSharedFlow example never ends`() {
        runBlocking {
            val mutableSharedFlow =
                MutableSharedFlow<String>(replay = 0) // or MutableSharedFlow<String>()
            launch {
                mutableSharedFlow.collect {
                    println("#1 received $it")
                }
            }
            launch {
                mutableSharedFlow.collect {
                    println("#2 received $it")
                }
            }
            delay(1000)
            mutableSharedFlow.emit("Message1")
            mutableSharedFlow.emit("Message2")
        }
    }

    /**
     * Полезно когда один flow хотим распространить на нескольких слушателей
     * SharingStarted.Eagerly - немедленно начать отправлять элементы
     * SharingStarted.Lazily - отправлять когда появится первый подписчик
     * SharingStarted.WhileSubscribed() - отправлять когда появится первый подписчик и
     * останавливаться (после stopTimeoutMillis = 0), когда последний исчезает,
     * replayExpirationMillis = Long.MAX_VALUE держать cache после последнего подписчика
     */
    @Test
    fun `shareIn example`() {
        runBlocking {
            val flow = flowOf("A", "B", "C")
                .onEach { delay(1000) }
            val sharedFlow: SharedFlow<String> = flow.shareIn( scope = this,
                started = SharingStarted.Eagerly,   // немедленно начать слушать
                // replay = 0 (default)
            )
            delay(500)
            launch {
                sharedFlow.collect { println("#1 $it") }
            }
            delay(1000)
            launch {
                sharedFlow.collect { println("#2 $it") }
            }
            delay(1000)
            launch {
                sharedFlow.collect { println("#3 $it") }
            }
        }
    }

    @Test
    fun `stateFlow example`() {
        runBlocking {
            val state = MutableStateFlow("A")
            println(state.value) // A
            launch {
                state.collect { println("Value changed to $it") }
                // Value changed to A
            }
            delay(1000)
            state.value = "B" // Value changed to B
            delay(1000)
            launch {
                state.collect { println("and now it is $it") }
                // and now it is B
            }
            delay(1000)
            state.value = "C" // Value changed to C and now it is C
        }
    }

    /**
     * Полезно когда flow хотим превратить в StateFlow, ждёт первого элемента для инициализации
     * Можно добавить SharingStarted.Lazily и начальное значение (тогда будет not suspending)
     */
    @Test
    fun `stateIn example`() {
        runBlocking {
            val flow = flowOf("A", "B", "C")
                .onEach { delay(1000) }
                .onEach { println("Produced $it") }
            val stateFlow: StateFlow<String> = flow.stateIn(this)
            println("Listening")
            println(stateFlow.value)
            val job = launch {
                stateFlow.collect { println("Received $it") }
            }
            delay(10000)
            this.coroutineContext.cancelChildren()
        }
    }
    // endregion SharedFlow and StateFlow
// endregion

}