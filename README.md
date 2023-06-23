# Description
Dagger 2. Основы

## Navigation
+ [up](../di_dagger)
+ [root](../master)

## Details

### Основы 
`@Component` к нему обращаемся и он ищет по графу необходимые зависимости (в нём методы, что достать или куда доставить)</br>
`@Module` предоставляет зависимости</br>
`@Provides` предоставляет зависимость</br>
```kotlin
@Component(modules = [AppModule::class])
interface AppComponent {
    val computer: Computer
}

@Module
object AppModule {
    @Provides
    fun provideComputer(
        processor: Processor,
        motherboard: Motherboard,
        ram: RAM
    ) = Computer(processor, motherboard, ram)

    @Provides
    fun provideProcessor() = Processor()

    @Provides
    fun provideMotherboard() = Motherboard()

    @Provides
    fun provideRAM() = RAM()
}
```
Генерируется `DaggerAppComponent : AppComponent`, через который можно достать зависимости. 
Нужно где-то создать граф зависимостей, чтоб затем можно было к нему обращаться:
```kotlin
class MainApplication : Application() {
    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.create()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApplication -> component
        else -> this.applicationContext.appComponent
    }
```
Однако обычно, чтобы не доставать каждую зависимость из графа отдельно, необходимые вещи (свойства класса,
конструктор, метод) помечают с помощью `@Inject`, достают `appComponent` и помещают себя в него:
```kotlin
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(fragment: MyFragment)
}
class MyFragment : Fragment() {
    @Inject
    lateinit var computer: Computer

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}
```
Модули можно разбивать на несколько:
```kotlin
@Module(includes = [NetworkModule::class, AppBindModule::class])
object AppModule
```
`@Binds` работает как маппинг, при запросе одного типа (возвращаемого), отдаёт другой (из единственного аргумента). 
Можно сделать через абстрактный класс и метод, но лучше через интерфейс:
```kotlin
@Module
interface AppBindModule {
    @Binds
    fun bindSomeRepositoryImpl_to_SomeRepository(
        someRepositoryImpl: SomeRepositoryImpl
    ): SomeRepository
}
```
Можно сделать `@Inject` в конструктор, если создание достаточно просто и вместо:
```kotlin
@Module(includes = [NetworkModule::class, AppBindModule::class])
object AppModule {
    @Provides
    fun provideSomeRepository(
        someService: SomeService,
        analytics: Analytics
    ): SomeRepository {
        return SomeRepositoryImpl(someService, analytics)
    }
}

class SomeRepositoryImpl(
    private val someService: SomeService,
    private val analytics: Analytics,
) : SomeRepository { /**...*/ }
```
Получим:
```kotlin
@Module(includes = [NetworkModule::class, AppBindModule::class])
object AppModule

class SomeRepositoryImpl @Inject constructor(
    private val someService: SomeService,
    private val analytics: Analytics,
) : SomeRepository { /**...*/ }
```
Можно добавить `@Inject` в метод, тогда этот метод запустится во время первого обращения к графу:
```kotlin
class MyFragment : Fragment() {
    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
    
    @Inject
    fun trackScreenShow(analytics: Analytics) {
        analytics.trackScreenShow()
    }
}
```

### Assisted Inject
Если какой-то зависимости нет в графе, например, приходит извне или меняется со временем, то
на помощь приходит Assisted Inject. Было:
```kotlin
class MyViewModel(
    private val someArgument: String,
    private val someRepository: SomeRepository
) : ViewModel() {
    // ...
    class Factory(
        private val someArgument: String,
        private val someRepository: SomeRepository,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MyViewModel(someArgument, someRepository) as T
        }

        class FactoryCreator @Inject constructor(val someRepository: SomeRepository) {
            fun create(someArgument: String): Factory {
                return Factory(someArgument, someRepository)
            }
        }
    }
}
```
Стало:
```kotlin
class MyViewModel(
    private val someArgument: String,
    private val someRepository: SomeRepository
) : ViewModel() {
    // ...
    class Factory @AssistedInject constructor(
        @Assisted("someArgument") private val someArgument: String,
        private val someRepository: SomeRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ViewModel01(someArgument, someRepository) as T
        }

        @AssistedFactory
        interface FactoryCreator {
            fun create(@Assisted("someArgument") someArgument: String): Factory
        }
    }
}
```

### Квалификаторы
Если одинаковые зависимости возвращаются, то можно пометить с помощью `@Named` (или `@param:Named`):
```kotlin
@Module
object AppModule {
    @Provides
    @Named("intel")
    fun provideProcessor(): Processor = IntelProcessor()

    @Provides
    @Named("amd")
    fun provideProcessor(): Processor = AMDProcessor()
}

class MyFragment : Fragment() {
    @Named("intel")
    lateinit var processor: Processor

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}
```
Вместо `@Named` можно создать свои квалификаторы через `@Qualifier`:
```kotlin
@Module
object AppModule {
    /**
     * Реализация по умолчанию, если не указать квалификатор явно
     */
    @Provides
    fun provideProcessor(): Processor = DefaultProcessor()
    
    @Provides
    @Intel("Core i9")
    fun provideProcessor(): Processor = IntelProcessor()

    @Provides
    @AMD
    fun provideProcessor(): Processor = AMDProcessor()
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Intel(val value: String = "Core i7")

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AMD

class MyFragment : Fragment() {
    @Intel
    lateinit var processor: Computer

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
}
```

### Отложенная инициализация
С помощью `Lazy` можем получать зависимость в момент обращения и сохранять её (в следующий раз при обращении не тянем из графа):
```kotlin
class MyFragment : Fragment() {
    @Inject
    lateinit var computer: Lazy<Computer>

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
    
    fun someFunction() {
        Log.i("MyFragment", "${computer.get()}")
    }
}
```
Аналогично `Provide` получает зависимость отложено, но при каждом обращении тянет её из графа:
```kotlin
class MyFragment : Fragment() {
    @Inject
    lateinit var computer: Provide<Computer>

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }
    
    fun someFunction() {
        Log.i("MyFragment", "${computer.get()}")
    }
}
```

### Создание компонента через builder
Если нам нужны зависимости извне (например, context), то для этого у компонента есть builder:
```kotlin
@Component(modules = [AppModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}

@Module
object AppModule {
    @Provides
    fun provideResourceManager(context: Context) = ResourceManager(context)
}

class MainApplication : Application() {
    //...
    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().context(this).build()
    }
}
```
Можно через фабрику:
```kotlin
@Component(modules = [AppModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}

@Module
object AppModule {
    @Provides
    fun provideResourceManager(context: Context) = ResourceManager(context)
}

class MainApplication : Application() {
    //...
    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.factory().create(this)
    }
}
```
Ещё один вариант это указание интерфейса с зависимостями:
```kotlin
@Component(modules = [AppModule::class], dependencies = [AppDependencies::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        fun appDependencies(appDependencies: AppDependencies): Builder
        fun build(): AppComponent
    }
}

interface AppDependencies {
    val context: Context
}

@Module
object AppModule {
    @Provides
    fun provideResourceManager(context: Context) = ResourceManager(context)
}

class MainApplication : Application() {
    //...
    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().appDependencies(AppDependenciesImpl()).build()
    }

    private inner class AppDependenciesImpl : AppDependencies {
        override val context: Context = this@MainApplication
    }
}
```

### Scope
Отвечает за время жизни компонента. Любой компонент должен быть помечен этой аннотацией (можно несколькими) 
и любая зависимость может быть помечена, можно создать и свой:
```kotlin
@Component(modules = [AppModule::class])
@Singleton
interface AppComponent

@Module
object AppModule {
    @Provides
    @AppScope
    fun provideResourceManager(context: Context) = ResourceManager(context)
}

@Scope
annotation class AppScope
```
Имеет смысл помечать аннотацией statefull class, потому что будут висеть в памяти</br>
`@Singleton` используется для хранения на протяжение всей жизни компонента</br>
`@Reusable` используется чтобы в рамках компонента стараться держать зависимости, но без гарантий</br>
Если мы создаём свою аннотацию и помечаем ею сабкомпонент, то объекты из модуля, помеченные 
этой  аннотацией будут синглтонами, пока жив этот сабкомпонент. Если пометить аннотацией из 
родительского компонента (при этом inject конструктора), то время жизни будет как у родителя и храниться там же

### Расширение графа
`@Subcomponent` нужен чтобы один компонент расширил другой, родитель знает про свои сабкомпоненты:
```kotlin
@Module(subcomponents = [FeatureComponent::class])
object AppModule

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun featureComponent(): FeatureComponent.Builder
     
    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}

@Scope
annotation class Feature

@Module
object FeatureModule

@Feature
@Subcomponent(modules = [FeatureModule::class])
interface FeatureComponent { 
    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureComponent
    }
}
```
На сабкомпонентах основан Hilt. Для многомодульного проекта это не лучшее решение

Есть другой способ расширить граф, допустим хотим прокинуть `context`:
```kotlin
@Component(modules = [AppModule::class])
interface AppComponent {
    fun context(): Context
    
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
} 

@Component(dependencies = [AppComponent::class])
interface FeatureComponent {
    @Component.Builder 
    interface Builder {
        fun appComponent(appComponent: AppComponent): Builder
        fun build(): FeatureComponent 
    }
}
```
Но это сработает для явной связи и лучше использовать другой подход:
```kotlin
@Component(modules = [AppModule::class])
interface AppComponent: FeatureDependencies {
    override fun context(): Context
    
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
} 
 
interface FeatureDependencies {
    fun context(): Context
}

@Component(dependencies = [FeatureDependencies::class])
interface FeatureComponent {
    @Component.Builder 
    interface Builder {
        fun featureDependencies(featureDependencies: FeatureDependencies): Builder
        fun build(): FeatureComponent 
    }
}
```

### Multibinding
Для гибкости если мы хотим создать множество одного типа, есть следующий вариант. Допустим у нас есть
несколько наследников, реализующих некий интерфейс и в будущем они будут только добавляться:
```kotlin
interface Animal
class Cat @Inject constructor() : Animal
class Dog @Inject constructor() : Animal
class Mouse @Inject constructor() : Animal
```
В каком-то месте мы собираем это множество и проводим над ним некие действия:
```kotlin
class Zoo @Inject constructor(
    private val animals: Set<@JvmSuppressWildcards Animal>
) {
    fun count() = animals.size
}
```
Зависимости можно предоставлять следующим образом с помощью аннотаций `@IntoSet` (или `@ElementsIntoSet`):
```kotlin
@Component(modules = [AnimalModule::class])
@Singleton
interface ZooComponent {
    val animals: Set<@JvmSuppressWildcards Animal>
    val zoo: Zoo
}

@Module
class AnimalModule {
    @Provides
    @IntoSet
    fun provideCat(): Animal = Cat()

    @Provides
    @ElementsIntoSet
    fun provideMultipleAnimals(
        dog: Dog,
        mouse: Mouse
    ): Set<Animal> = setOf(dog, mouse)
}

fun main() {
    val appComponent: ZooComponent = DaggerZooComponent.create()
    println(appComponent.zoo.count())
}
```

Рассмотрим пример с фабрикой viewmodel'ей и аннотацией `@IntoMap`. У нас есть следующие viewmodel'и:
```kotlin
class MainViewModel @Inject constructor() : ViewModel()
class DetailsViewModel @Inject constructor() : ViewModel()
class SecondViewModel @Inject constructor() : ViewModel()
```
Создадим фабрику:
```kotlin
class MultiViewModelFactory @Inject constructor(
    private val viewModelFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelFactories.getValue(modelClass as Class<ViewModel>).get() as T
    }

    val viewModelsClasses get() = viewModelFactories.keys
}
```
Затем пропишем зависимости:
```kotlin
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Component(modules = [MainBindsModule::class])
@Singletone
interface MainComponent {
    val factory: MultiViewModelFactory
    val secondComponent: SecondComponent.Builder

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
    }
}

@Module(subcomponents = [SecondComponent::class])
interface MainBindsModule {
    @Binds
    @[IntoMap ViewModelKey(MainViewModel::class)]
    fun provideMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(DetailsViewModel::class)]
    fun provideDetailsViewModel(detailsViewModel: DetailsViewModel): ViewModel
}
```
Subcomponent расширяет нашу фабрику своими viewmodel'ями:
```kotlin
@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class FeatureScope

@Subcomponent(modules = [SecondModule::class])
@FeatureScope
interface SecondComponent {
    val factory: MultiViewModelFactory

    @Subcomponent.Builder
    interface Builder {
        fun build(): SecondComponent
    }
}

@Module
interface SecondModule {
    @Binds
    @[IntoMap ViewModelKey(SecondViewModel::class)]
    fun provideSecondViewModel(secondViewModel: SecondViewModel): ViewModel
}
```
Обращение к компоненту происходит следующим образом:
```kotlin
fun main() {
    val appComponent = DaggerAppComponent.create()
    val secondComponent: SecondComponent = appComponent.secondComponent.build()
    val viewModel = secondComponent.factory.create(SecondViewModel::class.java)
}
```

## Links
+ [Мини-курс по Dagger 2 от Android Broadcast](https://www.youtube.com/playlist?list=PL0SwNXKJbuNkYFUda5rlA-odAVyWItRCP)
+ [Статья на хабре от Евгения Мацюка](https://habr.com/ru/companies/kaspersky/articles/422555/)
+ [Статья на хабре: ещё раз про многомодульность](https://habr.com/ru/companies/kaspersky/articles/520766/)
