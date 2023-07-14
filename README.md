# Description
Котлин. Структуры данных

## Navigation
+ [up](../kotlin)
+ [root](../master)

## Details
Описание будет для Java, потому что котлин использует структуры оттуда. Тут кратенькое описание. 
По-хорошему, нужно перенести в ветку java

Collection -> List, (Dequeue: Queue), Set
Map

В котлине из интерфейсов только List, Set и Map

### ArrayList : List
Динамический массив
```kotlin
val list: MutableList<String> = ArrayList()
```
В созданном массиве присутствует `elementData: Object[]` и `size`, при создании можно указать `capacity`.
Если `capacity` превышает указанный (по умолчанию 10), то текущий массив копируется в новый, который в 1.5 раза больше.
После удаления `capacity` не уменьшается, поэтому можно вручную вызвать `trimToSize`

Основное:
- add(): O(1) в среднем, в худшем случае O(n)
- add(index, element): O(n) в среднем
- get(): O(1)
- remove(): O(n)
- indexOf() / contains: O(n)
- Не синхронизирован

### LinkedList : List, Dequeue
Связный список
```kotlin
val list: MutableList<String> = LinkedList()
```
В созданном списке присутствует `first: Node<E>`, `last: Node<E>` и `size`

Основное:
- add() / addFirst() / addLast(): O(1)
- add(index, element): O(n) в среднем
- get(): O(n)
- removeFirst() / removeLast: O(1)
- remove(index) / remove(element): O(n)
- contains(): O(n)
- Не синхронизирован

### HashMap : Map
Хэш-таблица
```kotlin
val map: MutableMap<Int, String> = HashMap()
```
В созданной мапе присутствует набор корзин `table: Node<K,V>[]`, `loadFactor = 0.75`,
`thresold = capasity * loadFactor` для увеличения размера при заполнении и `size`.
Когда `size` становится больше `thresold`, происходит перераспределение и увеличение размеров в два раза. 
Если ключ `null`, то элемент помещается в начало массива. 
При добавлении проверяется ключ на `null`, затем вычисляется `hash` от `hash` ключа, на основе этого подбирается
индекс в массиве и кладётся элемент в начало корзины

Методы разрешения коллизий:
- открытая адресация (ищем следующую свободную ячейку, для последних двух способов размер массива простое число)
  - линейное пробирование (при удалении ячейка помечается специальным ключом)
  - квадратичное пробирование (поиск следующей ячейки как квадрат шага)
  - двойное хеширование (смещение для ячейки определяется новой хеш-функцией != 0)
- метод цепочек

Основное:
- set(key, element): O(1) при отсутствии коллизий
- get(key) / remove(key): O(1) в лучшем случае и O(log(n)) в худшем, если элементов >8, иначе O(n)
- Не синхронизирован

### LinkedHashMap : (HashMap() : AbstractMap(), Map)
Упорядоченная хэш-таблица
```kotlin
val map: MutableMap<Int, String> = LinkedHashMap()
```
Помимо вещей из `HashMap`, присутствует `head`, `tail` и `accessOrder`, который определяет порядок (доступ или добавление)

### TreeMap : (AbstractMap(), Map)
В отличие от LinkedHashMap, где порядок добавления сохраняется, здесь сохраняется естественный порядок

### HashTable : Dictionary()
Устаревшая версия HashMap синхронизированная 
(для синхронизации лучше посмотреть в сторону ConcurrentHashMap, ConcurrentSkipListMap)

### WeakHashMap
Если на ключ никто жёстко не ссылается, то Garbage Collector удалит этот элемент

### HashSet
```kotlin
val set: MutableSet<String> = HashSet()
```
В созданном множестве присутствует `map: HashMap<E,Object>`. Сложность аналогично `HashMap`

### ConcurrentHashMap vs Collections.synchronizedMap vs ConcurrentSkipListMap
Первая это настоящая многопоточная карта, чтение неблокирующее.
Синхронизированная просто оборачивает существующую, поэтому блокирует полностью на чтение-запись.
Последняя это сбалансированная, поиск основан на списках с пропусками (аналог TreeMap)

### CopyOnWriteArrayList
Список для многопоточки, неблокирующее чтение

## Links
+ [Структуры данных в картинках. ArrayList](https://habr.com/ru/articles/128269/)
+ [Структуры данных в картинках. LinkedList](https://habr.com/ru/articles/127864/)
+ [Структуры данных в картинках. HashMap](https://habr.com/ru/articles/128017/)
+ [Структуры данных в картинках. LinkedHashMap](https://habr.com/ru/articles/129037/)
+ [HashMap и другие полезные объяснения](https://www.baeldung.com/java-hashmap)
+ [Временная сложность](https://for-each.dev/lessons/b/-java-collections-complexity)
+ [Хорошая статья про структуры данных](https://habr.com/ru/articles/696184/#HashMap)
+ [Ещё одна хорошая статья про структуры данных](https://habr.com/ru/articles/237043/)