package com.artofmainstreams.examples

import org.junit.Test

/**
 * Примеры по лямбдам
 */
typealias Handler = (Int, Int) -> String

class LambdaUnitTest {
    /**
     * Особенность в том, чтобы не было теневых методов
     */
    inline var junior: Junior
        get() = Junior()
    set(v) { Junior() }

    val middle: Middle
        inline get() = Middle()

    var senior: Senior
        get() {
            return Senior()
        }
        inline set(v) { Senior() }

    @Test
    fun `simple lambda`() {
        val sum = { x: Int, y: Int -> x + y }
        assert(sum(1, 1) == 2)
    }

    @Test
    fun `short simple lambda`() {
        val sum: (x: Int, y: Int) -> Int = { x, y -> x + y }
        assert(sum.invoke(1, 1) == 2)
    }

    @Test
    fun `typealias`() {
        val sum: Handler = { x, y -> "$x + $y" }
        assert(sum(1, 1) == "1 + 1")
    }

    @Test
    fun `qualified return`() {
        val list = listOf(0, -1, 2)
        list.filter { value ->
            val shouldFilter = value < 0
            shouldFilter
        }
        // равносильно
        list.filter { value ->
            return@filter value < 0 // если без квалификатора, то выйдем из всей функции
        }
    }

    /**
     * Замыкание
     */
    @Test
    fun closure() {
        val list = listOf(0, -1, 2)
        var sum = 0
        list.filter { it > 0 }.forEach { value ->
            sum += value
        }
        println(sum)
    }

    @Test
    fun `inline function`() {
        multiplyBy3(2) { result ->
            println("Result: $result")
            return
        }
        println("done") // не выведется, потому что return возвратит из метода
    }

    @Test
    fun reified() {
        println(tag())
    }

    @Test
    fun `return lambda`() {
        println(someText().invoke("ОГО"))
    }

    /**
     * Отличительная особенность анонимных функций в том, что return возвращает из неё
     */
    @Test
    fun `anonymous function`() {
        val ints = listOf(1, 2, 3)
        ints.filter(fun(item) = item > 0)
    }

    private inline fun multiplyBy3(value: Int, onResult: (Int) -> Unit): Int {
        val result = value * 3
        onResult(result)
        return result
    }

    interface A<in X, out Y, in Z, out Q> {
        fun some(): (X) -> ((Y) -> Z) -> Q
    }

    class AImpl : A<Middle, Middle, Middle, Middle> {
        override fun some(): (Junior) -> ((Senior) -> Junior) -> Senior {
            return {
                {
                    Senior()
                }
            }
        }
    }

    open class Junior
    open class Middle : Junior()
    open class Senior : Middle()

    fun test() {
        val a = AImpl()
        val junior = Junior()
        val middle = Middle()
        val senior = Senior()
        a.some().invoke(junior).invoke { senior ->
            junior
        }
    }

    private fun someText(): (String) -> Unit {
        return { str ->
            println("вау, $str")
        }
    }

    private inline fun <reified T> T.tag(): String = T::class.java.simpleName
}