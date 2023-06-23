# Description
Всё про Dagger 2

## Navigation
+ [up](../di)
+ [root](../master)
+ di/dagger/[basic](../di_dagger_basic) - основы Dagger 2

## Details
Подключаем саму библиотеку и расширение для компилятора:
```groovy
plugins {
    id 'kotlin-kapt'
}
dependencies {
    implementation 'com.google.dagger:dagger:$version'
    kapt 'com.google.dagger:dagger-compiler:$version'
}
```

С помощью dependency injection отделяем процесс получения зависимостей от создания объекта. За это отвечает компонент,
работающий с графом зависимостей (набор всех объектов и связей между ними)

Отличие второй версии от первой в избавлении от рефлексии (таким образом ускорение) и валидация графа во время компиляции
