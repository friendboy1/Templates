# Description

Всё про Room

## Navigation
+ [up](../jetpack)
+ [root](../master)

## Details

В качестве реляционной базы данных в Android используется SQLite. Библиотека Room оборачивает работу с ней.

### Подключение

Актуальная версия [здесь](https://developer.android.com/jetpack/androidx/releases/room)

```groovy
dependencies {
    implementation 'androidx.room:room-runtime:2.7.2' // Библиотека "Room"
    ksp "androidx.room:room-compiler:2.7.2" // Кодогенератор
    implementation 'androidx.room:room-ktx:2.7.2' // Дополнительно для Kotlin Coroutines, Kotlin Flows
}
```
```groovy
plugins {
    id 'com.google.devtools.ksp'
}
```

### Основные аннотации

+ `@Database` - объявление базы данных
+ `@Entity` - объявление сущности базы данных
+ `@Dao` - объявление интерфейса, который будет заниматься манипулированием данными базы данных
+ `@PrimaryKey` - объявление первичного ключа сущности
+ `@ColumnInfo` - настройки конкретного столбца сущности
+ `@Query` - выполнить SQL-запрос в методах DAO-интерфейса
+ `@Insert` - выполнить вставку в таблицу базы данных
+ `@Update` - выполнить обновление некоторых строк в таблице базы данных
+ `@Delete` - выполнить удаление некоторых строк в таблице базы данных
+ `@Transaction` - пометить метод в DAO-интерфейсе как транзакция

### Создание таблицы

Простая:
```kotlin
@Entity(tableName = "first_table_name")
data class FirstDbEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "some_name") val name: String
)
```

Чуть посложнее:
```kotlin
@Entity(
    tableName = "relationship",
    indices = [Index("id")], // по какому полю индексацию проводить
    foreignKeys = [ // составные ключи
        ForeignKey(
            entity = FirstDbEntity::class,
            parentColumns = ["id"], // id в первой ДБ
            childColumns = ["first_id"] // id в связующей таблице
        ),
        ForeignKey(
            entity = SecondDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["difficult_id"]
        )
    ]
)
data class RelationshipDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "first_id") val firstId: Long,
    @ColumnInfo(name = "second_id") val secondId: Long,
    val propertyOne: Long,
    val propertyTwo: Long
)
```

### Интерфейс взаимодействия с таблицей

```kotlin
@Dao
interface RelationshipDao {
    @Insert(entity = RelationshipDbEntity::class)
    fun insertNewRelationshipData(relationship: RelationshipDbEntity)

    // пример взят со статьи на хабре, так что запрос лучше самостоятельно придумать
    @Query("SELECT statistic.id, result_name, difficulty_name, mistakes, points FROM statistic\n" +
            "INNER JOIN results ON statistic.result_id = results.id\n" +
            "INNER JOIN difficulty_levels ON statistic.difficult_id = difficulty_levels.id;")
    fun getAllStatisticData(): List<StatisticInfoTuple>

    @Query("DELETE FROM statistic WHERE id = :statisticId")
    fun deleteStatisticDataById(statisticId: Long)
} 
```

### Создание БД

Описание
```kotlin
@Database(
    version = 1,
    entities = [
        RelationshipDbEntity::class,
        FirstDbEntity::class,
        SecondDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getRelationshipDao(): RelationshipDao

}
```

Создание
```kotlin
object Dependencies {

    private lateinit var applicationContext: Context

    fun init(context: Context) {
        applicationContext = context
    }

    private val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .createFromAsset("room_article.db") // если есть (можно создать через DB Browser for SQLite например)
            .build()
    }
}
```

## Links

+ [Документация](https://developer.android.com/training/data-storage/room)
+ [Codelab](https://developer.android.com/codelabs/android-room-with-a-view-kotlin#0)
+ [Вводная статья на хабре](https://habr.com/ru/articles/713518/)
