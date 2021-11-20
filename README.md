# Description
Корутины. Основы. Дополнительные примеры в файлах ```CoroutinesTest``` и ```FlowTest```, а также в ```ViewModel01``` и ```Fragment01```.
Не покрыта тема с тестами, подробнее можно изучить в книжке

## Navigation
+ [up](https://github.com/friendboy1/Templates/tree/master)
+ [root](https://github.com/friendboy1/Templates/tree/master)

## Details
Корутины запускаются через coroutine builder's (```runBlocking``` или ```launch```, ```async```)</br>
```suspendCancellableCoroutine``` нужен чтоб прервать корутину (не suspend функцию) и выполнить действие</br>
Когда корутина прерывается, она возвращает ```Continuation``` (хранит ```label```, локальные переменные)</br>
Корутины не выбрасывают исключения, а пробрасывают вверх по иерархии</br>

```suspend fun getUser(): User?```</br>
превращается в </br>
```fun getUser(continuation: Continuation<*>): Any?```</br>
```Any``` потому что функция может вернуть ```COROUTINE_SUSPENDED```; выполнение зависит от ```label```</br>

### Coroutine Builder
Создают свой собственный ```Job```, который является ```CoroutineContext```, но не наследуется от другой корутины 
(```async``` создаёт ```Deffered```, реализует интерфейс ```Job```)</br>
+ ```launch``` - обычный неблокирующий запуск
+ ```async``` - как и ```launch```, но возвращает значение (wrapped with Deferred with method await). 
Если является top-level coroutine и не вызывать ```await```, то исключение не выбросится</br>
Если мы используем в suspend-функции, то нам понадобится ```scope```, 
который можно создать через ```coroutineScope``` (можно обернуть в try-catch)</br>
+ ```runBlocking``` - блокирует поток всякий раз, как прерывается корутина, создаёт свой ```Scope``` (root coroutine) 
и ```dispatcher``` (например, ```main```)

### Coroutine Context
Хранит элемент или индексированное множество ```CoroutineContext.Elements``` (```Job```, ```CoroutineName```, ```CouroutineDispatcher``` и т.д.), 
каждый из которых явлляется ```CoroutineContext```

### Job
```join``` чтобы дождаться выполнения job</br>
```CompletableJob``` расширяет методами:
+ ```complete``` - ожидает завершения детей и завершает корутину, новую не создаёт
+ ```completeExceptionally``` - немедленно завершает выполнение и отменяет остальные корутины</br>

Есть жизненный цикл: new - active - completing - completed - cancelling - cancelled</br>
```SupervisorJob``` игнорирует исключения своих детей</br>
```supervisorScope``` как ```coroutineScope```, только использует ```SupervisorJob```</br>
```CoroutineExceptionHandler``` задаёт правило для обработки всех ошибок</br>

### Cancellation
Выбрасывается ```CancellationException```, можно наследоваться, действует на одну корутину</br>
В первой точке прерывания отменяется (suspend)</br>
Отменяет всех своих детей</br>
Однажды отменённая корутина больше не может быть родителем для своих детей</br>
Если нужно выполнить действия после отмены, то можно использовать ```withContext(NonCancellable)```
или ```job.invokeOnCompletion```</br>
Можно использовать ```yield()``` внутри корутины, чтобы её прервать, или
проверка на ```isActive``` или ```ensureActive```, но этот способ не сработал

### Coroutine Scope
Функция ```coroutineScope``` создаёт корутину (в suspend-функции), но прерывает другую, пока не закончится выполнение</br>
Наследуется от родительского ```Scope```, но переопределяет ```Job```, отмена родителя отменяет детей</br>
```withContext``` это ```coroutineScope```, который модифицирует родительский scope</br>
```withTimeout``` это ```coroutineScope``` с timeout (```TimeoutCancellationException```)</br>
```withTimeoutOrNull``` не выбрасывает исключение</br>
Для аналитики удобно создавать свой Scope и передавать в функции</br>
В библиотеке ktx есть расширения lifecycleScope и viewModelScope

### Dispatchers
+ Default - для вычислений на ЦПУ, количество потоков равно ядрам (но не менее двух)
+ Main - в Android это UI-thread, ```immediate``` чтобы не переключать, если уже на главном
+ IO - для блокирующих операций (зачастую лимит 64)
+ Unconfined - не меняет потоки выполнения</br>
Методы:
+ ```limitedParallelism``` - ограничить количество потоков</br> 
(в случае IO создаётся новый диспатчер с указанным количеством, т.е. можно увеличить,
а в Default ограничить)
+ ```asCoroutineDispatcher``` - превращает Executor в Dispatcher, но нужно потом закрыть

### Channel
Нужен для обмена данными между корутинами; hot</br>
Интерфейс наследуется от ```SendChannel``` (suspend ```send```, ```close```) и ```ReceiveChannel``` (suspend ```receive```, ```cancel```)</br>
Может быть много источников и много потребителей, но информация передаётся один раз</br>
Функция ```produce``` автоматически закрывает канал</br>
Типы:
+ Unlimited - ```send``` никогда не прерывается
+ Buffered - можно установить вместимость, по умолчанию 64
+ Rendezvous (default) - вместимость 0, т.е. обмен происходит только при встрече
+ Conflated - вместимость 1 и каждый новый заменяет предыдущий

```onBufferOverflow```, метод ```produce``` не позволяет, поэтому через ```Channel```:
+ SUSPEND - прервать метод отправки
+ DROP_OLDEST (default) - выкинуть старый элемент
+ DROP_LATEST - выкинуть последний элемент</br>

```onUndeliveredElement``` - когда не можем доставить элемент. Это бывает когда происходит закрытие
или отмена. Но и когда ```send```, ```receive```, ```receiveOrNull``` или ```hasNext``` выбрасывают исключение</br>

```Actor``` работает в одном потоке и общение между ними через сообщения, может:
+ отправлять конечное количество сообщений другим актерам;
+ создать конечное количество новых акторов;
+ определить поведение, которое будет использоваться для следующего сообщения, которое оно
получает

### Flow
Поток значений, выполняющийся асинхронно; cold</br>
```collect``` для Flow как ```forEach``` для коллекций, терминальный оператор</br>
```channelFlow``` - горячий flow + channel</br>
```callbackFlow``` - слушанье не зависит от обработки</br>
Подробнее про Flow (операторы, StateFlow, SharedFlow, stateIn и т.д.) в файле ```FlowTest```</br>

### Structured Concurrency
+ когда scope отменён, все корутины отменяются
+ если suspend-функция завершила свою работу, значит вся работа была сделана (никакие корутины там больше не запускаются)
+ если в корутине произошла ошибка, то scope об этом уведомляется

### Links
+ [Kotlin Coroutines Deep Dive](https://kt.academy/book/coroutines)
+ [Мини-видеокурс по корутинам от Android Broadcast](https://www.youtube.com/playlist?list=PL0SwNXKJbuNmsKQW9mtTSxNn00oJlYOLA)
