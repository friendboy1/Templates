# Description
Котлин. Extension. Все примеры в файле ```ExtensionsUnitTest.kt```

## Navigation
+ [up](../kotlin)
+ [root](../master)

## Details
android ktx для упрощения жизни</br>
Контекстные функции:
run, let, apply, also, with</br>
Есть обычные расширения, есть расширения-свойства, есть лямбда-расширения</br>
Расширения можно оборачивать в класс и тогда они будут доступны только в нём. 
Вызываются в этом случае либо через ```apply```, либо через собственный метод.</br>
Происходит конфликт имён, если создать метод такой же, в какой компилируется, поэтому можно обернуть в ```@JVMName```

### Links
+ [Курс от SkillBranch Middle Android Developer](https://skill-branch.ru/middle-android-developer)
+ [Интересные примеры](https://github.com/happy-bracket/skillbanch-extensions/tree/master/app/src/main/java/ru/substancial/extensions)
+ [Android KTX](https://developer.android.com/kotlin/ktx)
+ [Библиотека с интересными расширениями Arrow KT](https://arrow-kt.io/)
