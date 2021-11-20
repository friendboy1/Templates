package com.artofmainstreams.examples

import org.junit.Test

/**
 * Примеры использования расширений в котлине
 */
class ExtensionsUnitTest {

    /**
     * Пример показывает как вызвать лямбда-расширение, определённое в конкретном классе
     */
    @Test
    fun `extension lambda`() {
        val person = Person("John", "Doe", "Ivanovich")
        val personOperations = PersonOperations()
        personOperations.apply {
            person.extensionLambda()
            println("Extension property: ${person.fullname}")
        }
        getExtensionsFromClass {
            println("Хитрый способ достать расширение из класса:  ${person.fullname()}")
        }
    }
}

data class Person(
    val firstName: String,
    val secondName: String,
    val surname: String
)

class PersonOperations {
    fun Person.fullname(): String = "$firstName $secondName $surname"

    /**
     * Extension property
     */
    val Person.fullname: String
        get() = "$firstName $secondName $surname"

    val extensionLambda: Person.() -> Unit = {
        println(firstName)
        println(secondName)
        println(surname)
    }
}

fun getExtensionsFromClass(block: PersonOperations.() -> Unit) {
    val personOperations = PersonOperations()
    personOperations.block()
}