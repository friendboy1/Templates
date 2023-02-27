# Description
Jetpack Compose. Анимации. За основу взята 3-я лекция Android Academy. 
Все примеры можно посмотреть запустив приложение

## Navigation
+ [up](https://github.com/friendboy1/Templates/tree/jetpack_compose)
+ [root](https://github.com/friendboy1/Templates/tree/master)

## Details
Признаки хорошей анимации: 
+ Прерываемость - состояние внезапно изменилось и анимацию можно прервать
+ Непрерывность - когда прервали, анимация от текущего (промежуточного) состояния идёт, а не от крайнего
+ Плавность - когда прерываем, скорость не сбрасывается

Анимацию можно разделить на следующие категории:
- Высокоуровневые анимации
  - AnimatedVisibility
  - AnimatedContent
  - Crossfade
  - Modifier.animateContentSize
- Низкоуровневые
  - animate*AsState
  - updateTransition
  - rememberInfiniteTransition
  - Animatable
  - TargetBasedAnimation/DecayAnimation
  - Animation
- AnimationSpec
  - tween
  - spring
  - snap
  - repeatable
  - infiniteRepeatable

### AnimatedVisibility
Простая анимация, которую нельзя поменять, но можно комбинировать через плюс. 
Первым аргументом передаётся видимости либо через `Boolean`, либо через `MutableTransitionState`, 
который позволяет отслеживать состояние (есть ли анимация в данный момент). 
Можно добавлять свою анимацию через `transition.animate*` и время будет синхронизировано.
Пример в файле 
[AnimatedVisibilityScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/AnimatedVisibilityScreen.kt)
```kotlin
AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn() + scaleIn(), // появление - существует 8 стандартных поведений
            exit = fadeOut() + scaleOut()   // исчезание - по аналогии, тоже 8 стандартных поведений
        ) {
            // вот тут можно добавить анимацию transition.animate*, 
            // окончание которой AnimatedVisibility будет ждать
            Image(
                painter = painterResource(id = R.drawable.profile_picture),
                contentDescription = "Фотография профиля"
            )
        }
```

### AnimationSpec
Нужен для кастомизации анимации, используется повсеместно
- tween
  - ограничен временем
  - движется между стартом и концом
  - easing - отвечает за ускорение, можно добавить свой через билдер с кривой безье
- spring (пружинка)
  - нельзя указать длительность и easing
  - может выходить за пределы анимации
  - dampingRation - коэффициент гашения
  - stiffness - жёсткость пружины (с какой скоростью будет двигаться)
- repeatable
  - оборачивает другую анимацию
  - iterations - количество итераций
  - repeatMode - если Reverse, то по завершении идёт в обратную сторону
- infiniteRepeatable
  - оборачивает другую анимацию
  - repeatMode - если Reverse, то по завершении идёт в обратную сторону
- snap
  - просто переключает состояние
- keyFrames
  - указываем какое значение в какой момент должно быть

Пример:
[AnimatedSpecScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/AnimatedSpecScreen.kt)

### AnimatedContent
Позволяет анимировать изменение содержимого внутри себя. По умолчанию размер обрезается до последнего кадра,
но с помощью `SizeTransform(clip = false)` можно убрать это ограничение. Пример:
[AnimatedContentScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/AnimatedContentScreen.kt)

### Crossfade
Урезанная версия экспериментальной AnimatedContent. Нет возможности добавлять анимацию. Пример:
[AnimatedContentScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/AnimatedContentScreen.kt)

### Modifier.animateContentSize
Удобное расширение для анимирования элемента, когда его размер меняется. 
Пример:
[AnimatedContentScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/AnimatedContentScreen.kt)

### animate*AsState
Для анимирования отдельных параметров. Указываем целевое значение и анимация происходит от текущего. 
Под капотом используется `Animatable`. Есть следующие:
+ float
+ dp
+ size
+ offset
+ rect
+ int
+ intOffset
+ intSize
+ color
+ value используется под капотом и можно создать свой кастомный

Для кастомного нам понадобится `TwoWayConverter`, который преобразует значение во float или в
несколько float вплоть до 4х значений. Пример:
[AnimatedAsStateScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/AnimatedAsStateScreen.kt)

### updateTransition
Приходит на помощь, если нам необходимо знать не только целевое состояние, но и предыдущее
+ Хранитель состояний для одной или нескольких анимаций
+ Одновременно запускает анимации
+ Есть initialState и targetState (и ключевое слово `isTransitioningTo`)
+ Есть AnimatedVisibility, AnimatedContent, Crossfade
+ ChildTransition - разбитие сложной анимации на кусочки (пример в `UpdateTransitionSecondSampleScreen.kt`)
+ Инкапсулирование анимации (пример в `AnimatedAsStateScreen.kt#AnimationSquareByEncapsulatedUpdateTransition()`)
+ etc.

Примеры:
[AnimatedAsStateScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/AnimatedAsStateScreen.kt), 
[UpdateTransitionScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/UpdateTransitionScreen.kt), 
[UpdateTransitionSecondSampleScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/UpdateTransitionSecondSampleScreen.kt)

### InfiniteTransition
Вариант бесконечной анимации. Создаётся через `rememberInfiniteTransition()`, далее настраивается через `animate*`
Пример:
[AnimatedAsStateScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/AnimatedAsStateScreen.kt),

### Animatable
+ Stateful. Хранит в себе состояние
+ Поддерживает float и color
+ Завязан на корутины, важные функции: `animateTo()`, `animateDecay()`, `snapTo()`

Под капотом функция `animateTo` использует `TargetBasedAnimation`, а `animateDecay()` использует `DecayAnimation`, которые являются наследниками `Animation`

Пример:
[AnimatedAnimatableScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/AnimatableScreen.kt)

### Animation
+ Stateless (нет явных `currentValue` и `currentTime`). Хранит в себе информацию, на основе которой вычисляется анимация
+ `TargetBasedAnimation` с начальным и конечным значением
+ `DecayAnimation` анимация затухания, с начальным значением и скоростью

Пример:
[AnimatedAnimationScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/AnimationScreen.kt)

### Coroutine-based animations
```kotlin
// последовательное выполнение
scope.launch {
    animate() // 1
    animate() // 2
}
// параллельное выполнение
scope.launch {
    launch { animate() } // 1.1
    launch { animate() } // 1.2
}
// через coroutineScope, который дожидается выполнения всех дочерних корутин, объединим:
scope.launch {
    coroutineScope {
        launch { animate() } // 1.1
        launch { animate() } // 1.2
    }
    coroutineScope {
        launch { animate() } // 2.1
        launch { animate() } // 2.2
    }
}
```
Пример:
[CoroutineBasedApproachScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/CoroutineBasedApproachScreen.kt)

### Canvas
По сути ничего сложного, главное следить за рекомпозициями. 
Пример:
[CanvasAnimationScreen.kt](app/src/main/java/com/artofmainstreams/examples/ui/CanvasAnimationScreen.kt)

### Тестирование
Тестировать можно с помощью UI-тестов при помощи сравнения со скриншотами.
Запускается через команду `./gradlew executeScreenshotTests -Precord`. 
Пример из лекции: 
[Виджет](https://github.com/Vladuken/ComposeAnimations/blob/develop/app/src/main/java/com/vladuken/composeanimations/testing/Particle.kt)
[Тест](https://github.com/Vladuken/ComposeAnimations/blob/develop/app/src/androidTest/java/com/vladuken/composeanimations/ParticleTest.kt)

## Links
+ [Статья на хабре](https://habr.com/ru/companies/jugru/articles/683656/)
+ [Репозиторий с примерами из лекции](https://github.com/Vladuken/ComposeAnimations)
+ [CodeLab по анимации](https://developer.android.com/codelabs/jetpack-compose-animation#0)
+ [Performance видео](https://www.youtube.com/watch?v=EOQB8PTLkpY)
+ [Canvas видео](https://www.youtube.com/watch?v=1yiuxWK74vI)
+ [Курс от Android Academy, лекции](https://www.youtube.com/playlist?list=PLjLCGE4bVpHAGx8tW7aMx0q0RYH0HBVun)
+ [Курс от Android Academy, семинары](https://www.youtube.com/playlist?list=PLjLCGE4bVpHC9jREt1l-9MnbRMgdkNswk)
+ [Тестирование через скриншоты](https://alexzh.com/jetpack-compose-testing-animations/)
+ [Библиотека от гугла для анимации от 2019 года](https://developer.android.com/develop/ui/views/animations/spring-animation)