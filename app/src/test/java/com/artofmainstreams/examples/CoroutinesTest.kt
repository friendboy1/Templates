package com.artofmainstreams.examples

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import org.junit.Test
import java.lang.IllegalStateException

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

/**
 * Примеры использования корутин
 */
class CoroutinesTest {

    private val executor =
        Executors.newSingleThreadScheduledExecutor {
            Thread(it, "SingleThreadScheduler").apply { isDaemon = true }
        }

    /**
     * Пример прерывания корутины
     */
    private suspend fun customDelay(timeMillis: Long): Long =
        suspendCancellableCoroutine { continuation ->
            executor.schedule(
                { continuation.resume(timeMillis) },
                timeMillis,
                TimeUnit.MILLISECONDS
            )
        }

    @Test
    fun `coroutine with suspend`() {
        runBlocking {
            `suspend coroutine`()
        }
    }

    private suspend fun `suspend coroutine`() {
        println("Начало")
        println("Пауза: ${customDelay(10000)} мс")
        println("Завершение")
    }

    // region Builders
    @Test
    fun `builder launch`() {
        GlobalScope.launch {
            delay(10000L)
            println("World!")
        }
        GlobalScope.launch {
            delay(10000L)
            println("World!")
        }
        GlobalScope.launch {
            delay(10000L)
            println("World!")
        }
        println("Hello,")
        Thread.sleep(200000L)
    }

    @Test
    fun `builder runBlocking`() {
        runBlocking {
            delay(10000L)
            println("World!")
        }
        runBlocking {
            delay(10000L)
            println("World!")
        }
        runBlocking {
            delay(10000L)
            println("World!")
        }
        println("Hello,")
        Thread.sleep(200000L)
    }

    @Test
    fun `builder async`() {
        runBlocking {
            println("Старт")
            val resultDeferred: Deferred<Int> = GlobalScope.async {
                delay(7000L)
                42
            }
            val result: Int = resultDeferred.await()
            println(result)
            println(resultDeferred.await())
        }
    }
    // endregion Builders

    @Test
    fun `test coroutine scope`() {
        runBlocking {
            println(`coroutine scope`())
        }
    }

    private suspend fun `coroutine scope`(): Int {
        return coroutineScope {
            async { 8 }
        }.await()
    }

    @Test
    fun `coroutine context`() {
        val context: CoroutineContext = CoroutineName("Some name")
        val coroutineName: CoroutineName? = context[CoroutineName] // or ctx.get(CoroutineName)
        println(coroutineName?.name) // Some name
        val job: Job? = context[Job] // or ctx.get(Job)
        println(job) // null
    }

    @Test
    fun `replace job`() {
        runBlocking {
            launch(Job()) { // новая job заменяет родительскую
                delay(1000)
                println("Не будет напечатано, т.к. потеряли родителя")
            }
        }
    }

    @Test
    fun `waiting jobs`() {
        runBlocking {
            val job1 = launch {
                delay(1000)
                println("Job 1")
            }
            val job2 = launch {
                delay(2000)
                println("Job 2")
            }
            job1.join() // ожидаем завершения
            job2.join()
            println("Все jobs завершены")
        }
    }

    @Test
    fun `alone job`() {
        runBlocking {
            val job = Job()
            launch(job) { // новая job заменяет родительскую
                delay(1000)
                println("Job 1")
            }
            launch(job) { // новая job заменяет родительскую
                delay(2000)
                println("Job 2")
            }
            // job.join() - так лучше не делать, потому что даже после завершения работы детей,
            // ожидается выполнение
            job.children.forEach { it.join() }
            println("Все jobs завершены")
        }
    }

    /**
     * CompletableJob при вызове complete, ожидаем завершения всех корутин, но не можем создавать новые
     */
    @Test
    fun `complete job`() {
        runBlocking {
            val job = Job()
            launch {
                delay(50)
                job.complete()
            }
            launch(job) {
                repeat(5) { number ->
                    delay(200)
                    println("Повтор: $number")
                }
            }
            job.join()
            launch(job) {
                println("Не будет напечатано, т.к. job завершена")
            }
            println("Завершение")
        }
    }

    @Test
    fun `cancell coroutine`() {
        runBlocking {
            val job = launch {
                try {
                    repeat(1000) { number ->
                        delay(200)
                        println("Повтор: $number")
                    }
                } finally {
                    withContext(NonCancellable) {
                        delay(1000L)
                        println("Очистка завершена")
                    }
                }
            }
            delay(1050)
            job.cancel()
            job.join() // нужно, чтобы дождаться отмены всех детей
            //job.cancelAndJoin() - удобный extension
            println("Успешно отменено")
        }
    }

    @Test
    fun `unstoppable coroutine`() {
        runBlocking {
            val job = Job()
            launch(job) {
                repeat(1000) { number ->
                    Thread.sleep(200) // Имитация сложных вычислений
                    println("Повтор: $number")
                }
            }
            delay(1000)
            job.cancelAndJoin()
            println("Успешно отменено (нет, job непрерываемая)")
            delay(1000)
        }
    }

    @Test
    fun `stoppable coroutine`() {
        runBlocking {
            val job = Job()
            launch(job) {
                repeat(1000) { number ->
                    Thread.sleep(200)
                    yield()
                    println("Повтор: $number")
                }
            }
            delay(1000)
            job.cancelAndJoin()
            println("Успешно отменено")
            delay(1000)
        }
    }

    @Test
    fun `another way to stop coroutine`() {
        runBlocking {
            val job = Job()
            launch(job) {
                do {
                    Thread.sleep(200)
                    yield()
                    println("Напечатано")
                } while (isActive)
            }
            delay(1100)
            job.cancelAndJoin()
            println("Успешно отменено")
        }
    }

    @Test
    fun `yet another way to stop coroutine`() {
        runBlocking {
            val job = Job()
            launch(job) {
                repeat(1000) { number ->
                    Thread.sleep(200)
                    yield()
                    ensureActive()
                    println("Повтор: $number")
                }
            }
            delay(1100)
            job.cancelAndJoin()
            println("Успешно отменено")
        }
    }

    @Test
    fun `supervisor job`() {
        runBlocking {
            val scope = CoroutineScope(SupervisorJob()) // ошибка прокинется наверх, но не затронет соседние корутины
            scope.launch {
                delay(1000)
                throw Error("Some error")
            }
            scope.launch {
                delay(2000)
                println("Будет напечатано")
            }
            delay(3000)
        }
    }

    @Test
    fun `dont do this supervisor job`() {
        runBlocking {
            // Лишено смысла и равносильно Job, т.к. заменяется parent и остается один child
            launch(SupervisorJob()) {
                launch {
                    delay(1000)
                    throw Error("Some error")
                }
                launch {
                    delay(2000)
                    println("Не будет напечатано")
                }
            }
            delay(3000)
        }
    }

    @Test
    fun `best way supervisor job`() {
        runBlocking {
            val job = SupervisorJob()
            launch(job) {
                delay(1000)
                throw Error("Some error")
            }
            launch(job) {
                delay(2000)
                println("Будет напечатано")
            }
            job.join()
        }
    }

    @Test
    fun `supervisor scope`() {
        runBlocking {
            supervisorScope {
                launch {
                    delay(1000)
                    throw Error("Some error")
                }
                launch {
                    delay(2000)
                    println("Будет напечатано")
                }
            }
            delay(1000)
            println("Завершено")
        }
    }

    /**
     * Если убрать supervisorScope, то исключение пробросится наверх даже с блоком try-catch
     * даже если await не вызывать
     */
    @Test
    fun `supervisor scope await`() {
        runBlocking {
            supervisorScope {
                val result = async {
                    throw IllegalStateException("Some error")
                }
                launch {
                    try {
                        result.await()
                    } catch (e: CancellationException) {
                        throw e
                    }
                    catch (e: Exception) {
                        println("Обработали")
                    }
                }
            }
        }
    }

    class MyException : Throwable()

    @Test
    fun `supervisor with await`() {
        runBlocking {
            supervisorScope {
                val str1 = async<String> {
                    delay(1000)
                    throw MyException()
                }
                val str2 = async {
                    delay(2000)
                    "Text2"
                }
                try {
                    println(str1.await())
                } catch (e: MyException) {
                    println(e)
                }
                println(str2.await())
            }
        }
    }

    val scope = CoroutineScope(Job() + Dispatchers.Default)

    @Test
    fun `unstructured concurrency`() = runBlocking {
        val job = scope.launch {
            loadDataUnstructured()
        }
        job.cancelAndJoin()
        Thread.sleep(1000)
    }

    @Test
    fun `structured concurrency`() = runBlocking {
        val job = scope.launch {
            loadDataStructured()
        }
        job.cancelAndJoin()
        Thread.sleep(1000)
    }

    private suspend fun loadDataUnstructured() {
        scope.launch {
            delay(100)
            println("Work leak ")
        }
    }

    private suspend fun loadDataStructured() {
        coroutineScope {
            delay(100)
            println("Work leak ")
        }
    }

    object MyNonPropagatingException : CancellationException()

    @Test
    fun `cancellation exception`() {
        runBlocking {
            coroutineScope {
                launch {
                    launch {
                        delay(2000)
                        println("Не будет напечатано")
                    }
                    throw MyNonPropagatingException
                }
                launch {
                    delay(2000)
                    println("Будет напечатано")
                }
            }
        }
    }

    @Test
    fun `exception handler`() {
        runBlocking {
            val handler =
                CoroutineExceptionHandler { context, exception ->
                    println("Ошибка: $exception")
                }
            val scope = CoroutineScope(SupervisorJob() + handler)
            scope.launch {
                delay(1000)
                throw Error("Some error")
            }
            scope.launch {
                delay(2000)
                println("Будет напечатано")
            }
            delay(3000)
        }
    }

    /**
     * Пример ошибки со scope
     */
    data class UserInfo(val name: String, val age: Int)
    data class Message(val text: String)

    private fun getAge(): Int = throw Error("Service exception")
    private suspend fun getUserName(): String {
        delay(500)
        return "John Doe"
    }

    private fun getMessages(): List<Message> = listOf(Message("Hello, world!"))

    @Test
    fun `scope unhandled`() {
        runBlocking {
            val userInfo = try {
                val userName = async { getUserName() }
                val userAge = async { getAge() }
                UserInfo(userName.await(), userAge.await()) // ломает весь scope
            } catch (e: Error) {
                null
            }
            val messages = async { getMessages() }
            println("User info: $userInfo")
            println("Messages: ${messages.await()}")
        }
    }

    @Test
    fun `scope handled`() {
        runBlocking {
            val userInfo = try {
                coroutineScope { // создаёт новый scope, который можно ронять и новую корутину,
                    // которая зависит от предыдущей, параллельность не запускает
                    val userName = async { getUserName() }
                    val age = async { getAge() }
                    UserInfo(userName.await(), age.await())
                }
            } catch (e: Error) {
                null
            }
            val messages = async { getMessages() }
            println("User: $userInfo")
            println("Messages: ${messages.await()}")
        }
    }

    @Test
    fun `unconfined dispatcher`() {
        runBlocking {
            withContext(newSingleThreadContext("Thread1")) {
                var continuation: Continuation<Unit>? = null
                launch(newSingleThreadContext("Thread2")) {
                    delay(1000)
                    continuation?.resume(Unit)
                }
                launch(Dispatchers.Unconfined) {
                    println(Thread.currentThread().name) // Thread1
                    suspendCancellableCoroutine<Unit> {
                        continuation = it
                    }
                    println(Thread.currentThread().name) // Thread2
                    delay(1000)
                    println(Thread.currentThread().name)
                    // kotlinx.coroutines.DefaultExecutor
                    // delay меняет dispatcher
                }
            }
        }
    }

    sealed class CounterCommand {
        class Add(val count: Int) : CounterCommand()
        class Remove(val count: Int) : CounterCommand()
        class Get(val response: CompletableDeferred<Int>) : CounterCommand()
    }

    class Counter(coroutineContext: CoroutineContext = EmptyCoroutineContext) {
        private val scope = CoroutineScope(coroutineContext)
        private var counter = 0

        private val counterCommands = scope.actor<CounterCommand>(capacity = Channel.BUFFERED) {
            for (command in this) {
                when (command) {
                    is CounterCommand.Add -> counter += command.count
                    is CounterCommand.Get -> command.response.complete(counter)
                    is CounterCommand.Remove -> counter -= command.count
                }
            }
        }

        fun add(count: Int) {
            counterCommands.trySend(CounterCommand.Add(count))
        }

        fun remove(count: Int) {
            counterCommands.trySend(CounterCommand.Remove(count))
        }

        suspend fun getCount(): Int {
            val getCommand = CounterCommand.Get(CompletableDeferred())
            counterCommands.send(getCommand)
            return getCommand.response.await()
        }
    }

    @Test
    fun `synchronized value`() {
        runBlocking {
            val counter = Counter()
            counter.add(8)
            counter.add(7)
            counter.remove(3)
            println(counter.getCount())
        }
    }
}

