# Description
Котлин. Лямбда-выражения и функции высшего порядка

## Navigation
+ [up](../kotlin)
+ [root](../master)

## Details
Все примеры в файле [LambdaUnitTest.kt](app/src/test/java/com/artofmainstreams/examples/LambdaUnitTest.kt)</br>
Функции высшего порядка - это функции, которые умеют принимать функции как параметры или возвращать их

### High-order function
Функция высшего порядка - это функция, которая в качестве аргумента принимает лямбду или возвращает её</br>
Функциональный тип - тип данных, позволяющий работать с функциями как с объектами
```kotlin
// псевдоним для функционального типа
typealias Operation = (Int, Int) -> Int
```

### Замыкание
Лямбда-выражение или анонимная функция имеют доступ к своему замыканию, т.е. к переменным,
объявленным вне этого выражения или функции. В отличие от Java, захваченные переменные могут быть изменены

### inline, crossinline, noinline
Эффективно inline для:
+ раскрытия циклов - встраиваем в одном месте, пропускаем вызов функции на каждой итерации цикла
+ функциональных параметров - пропускаем создание лишнего анонимного класса, меньше вызовов gc

Неэффективно inline для:
+ больших функций и функций, вызываемых во многих местах

Сайд-эффекты:
+ приватные члены класса недоступны если только функция не приватная
+ функциональные параметры нельзя вызывать во вложенных функциях (решается через noinline или crossinline)
+ return выводит полностью из внешней функции (решается через noinline или crossinline)

Если аргументы предназначены для использования в будущем, то inline невозможно. Например:
```kotlin
fun <T, R> Sequence<T>.map(transform: (T) -> R): Sequence<R> {
    return TransformingSequence(this, transform)
}
```
Если передаётся несколько лямбд и одну из них невозможно сделать inline (в ней слишком много кода или она прокидывается дальше),
то можно воспользовать ключевым словом `noinline`:
```kotlin
inline fun foo(inlined: () -> Unit, noinline notInlined: () -> Unit) {
  // ...
}
```
В случае, если лямбда используется в другом контексте, но нужно пометить её через `crossinline`.
С помощью этого слова мы говорим, что переданная лямбда не содержит non-local return:
```kotlin
inline fun f(crossinline body: () -> Unit) {
    val f = object: Runnable {
        override fun run() = body()
    }
    // ...
}
```

Если коллекция очень большая, то лучше через `asSequence` использовать последовательности, которые
`inline` не используют, но цепочка применяется для каждого элемента

Можно возвращаться не из функции (`non-local return`), а из лямбды с помощью `label`:
```kotlin
fun lookForAlice(people: List<Person>) {
    people.forEach label@{
        if (it.name == "Алиса") return@label
    }
    println("Алиса должно быть где-то")
}
fun lookForAliceVersionTwo(people: List<Person>) {
    people.forEach {
        if (it.name == "Алиса") return@forEach
    }
    println("Алиса должно быть где-то")
}
```
Можно использовать анонимные функции, тогда `return` будет локальным:
```kotlin
fun lookForAlice(people: List<Person>) {
    people.forEach(fun (person) {
        if (person.name == "Алиса") return
        println("${person.name} не Алиса")
    })
}
```

### Reified
Помогает при рефлексии не передавать класс объекта. Помогает обращаться к рефлексивным свойствам класса


### Links
+ [Курс от SkillBranch Middle Android Developer](https://skill-branch.ru/middle-android-developer)
+ [Статья на Medium про inline](https://medium.com/@tferreirap/kotlin-quick-look-at-inline-noinline-and-crossinline-e62e8833db1f)
+ [Документация на русском](https://kotlinlang.ru/docs/reference/lambdas.html)
+ [Вопросы для интервью](https://habr.com/ru/articles/736392/)
