# Description
CustomView. Основы

## Navigation
+ [up](../android)
+ [root](../master)

## Details
Пример кастомной view можно посмотреть в классе [BasicCustomView](app/src/main/java/com/artofmainstreams/examples/ui/view/BasicCustomView.kt)

### Жизненный цикл
![LifeCycle View](https://habrastorage.org/getpro/habr/upload_files/938/dd5/157/938dd5157bc6242a7a1d2367cfe28e6d.png "https://habr.com/ru/articles/727744/")

Методы, которые можем использовать:
![LifeCycle View Available](https://habrastorage.org/getpro/habr/upload_files/ec3/496/516/ec34965166cad6fa0392b422391c6480.png "https://habr.com/ru/articles/727744/")

### Конструктор
Чтобы не переопределять все конструкторы, достаточно использовать аннотацию:
```kotlin
class BasicCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) { }
```

### Методы View
#### onAttachToWindow
View прикрепляется к экрану и знает о наличии других View

#### onMeasure
Вычисляем размер. Аргументы содержат доступный размер и измерение:
+ EXACTLY (`android:layout_width=8dp`)
+ AT_MOST (`layout_width="wrap_content"`)
+ UNSPECIFIED (любой размер)
```kotlin
override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 200 // Предполагаемая ширина View
        val desiredHeight = 200 // Предполагаемая высота View

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize // Задан конкретный размер для ширины
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize) // Размер не должен превышать заданный размер
            else -> desiredWidth // Задать предпочтительный размер, если точного или максимального размера не задано
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize // Задан конкретный размер для высоты
            MeasureSpec.AT_MOST -> min(desiredHeight, heightSize) // Размер не должен превышать заданный размер
            else -> desiredHeight // Задать предпочтительный размер, если точного или максимального размера не задано
        }

        setMeasuredDimension(width, height) // Устанавливаем фактический размер View
    }
```

#### onLayout
Нужен для размещения View. Обычно переопределяется при наличии дочерних View

#### onDraw
Метод для отрисовки

#### onSizeChanged
Метод вызывается при изменении размеров. В нём мы указываем новые параметры, которые нужны для отрисовки

#### onSaveInstanceState и onRestoreInstanceState
Методы для сохранения и восстановления состояния
```kotlin
override fun onSaveInstanceState(): Parcelable {
    val bundle = Bundle()
    bundle.putString("text", textView.text.toString())
    bundle.putParcelable("instanceState", super.onSaveInstanceState())
    return bundle
}

override fun onRestoreInstanceState(state: Parcelable?) {
    val bundle = state as Bundle
    textView.text = bundle.getString("text")
    super.onRestoreInstanceState(bundle.getParcelable("instanceState"))
}
```

#### onTouchEvent
Обработка касаний, `true` если обработано. Для поддержки accessibility нужно переопределить `performClick`
```kotlin
override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when(event?.action) {
            MotionEvent.ACTION_DOWN -> true
            MotionEvent.ACTION_UP -> {
                performClick()
                true
            }
            MotionEvent.ACTION_MOVE -> true
            MotionEvent.ACTION_OUTSIDE -> true
            else -> super.onTouchEvent(event)
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
        return true
    }
```

#### invalidate и requestLayout
`invalidate()` Нужен для перерисовки View, а `requestLayout()` для перерасчёта размеров (при этом не гарантируется вызов
метода `invalidate`, поэтому сначала его вызываем). Метод `forceLayout()` очищает кеш измерения у детей

### Рисование

#### Основные сущности
+ Canvas
+ Paint
+ Rect/RectF
+ Point
+ Path
+ Matrix
+ Bitmap

#### Canvas
+ `drawColor(color: Int)` залить область указанным цветом
+ `drawRGB(int r, int g, int b)` залить область указанным цветом
+ `drawLine(startX: Float, startY: Float, stopX: Float, stopY: Float, paint: Paint)` нарисовать линию
+ `drawRect(left: Float, top: Float, right: Float, bottom: Float, paint: Paint)` нарисовать прямоугольник
+ `drawRoundRect(float left, float top, float right, float bottom, float rx, float ry, Paint paint)` нарисовать прямоугольник с закруглёнными краями
+ `drawCircle(cx: Float, cy: Float, radius: Float, paint: Paint)` нарисовать круг
+ `drawOval(float left, float top, float right, float bottom, Paint paint)` нарисовать овал
+ `drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)` нарисовать овал обрезанный
+ `drawText(text: String, x: Float, y: Float, paint: Paint)` нарисовать текст
+ `drawPoint(float x, float y, Paint paint)` нарисовать точку

#### Paint
+ <code>isAntiAlias</code> включает сглаживание
+ <code>color</code> задаёт цвет
+ <code>style</code> задаёт стиль (<code>STROKE</code>, <code>FILL</code>, <code>FILL_AND_STROKE</code>)
+ <code>strokeWidth</code> задаёт толщину
+ <code>textSize</code> задаёт размер текста
+ <code>textAlign</code> задаёт выравнивание текста относительно точки старта
+ <code>measureText(text)</code> измерить ширину текста
+ <code>getTextBounds(text, 0, text.lastIndex, rect)</code> измерить границы текста
+ <code>xfermode</code> наложение одного слоя на другой, dst - что есть, src - что будет; есть много различных модов
```kotlin
val paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLUE
        style = Paint.Style.FILL_AND_STROKE
        strokeWidth = 10f
        textSize = 36f
        textAlign = Paint.Align.LEFT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }
```

#### Layout (для текста)
+ BoringLayout - простой однострочный текст (едва ли пригодится)
+ StaticLayout - многострочный неизменяемый текст
+ DynamicLayout - изменяемый текст (для очень сложной вёрстки)

#### PorterDuff

### Атрибуты
Можем создать свои атрибуты:
```xml
<declare-styleable name="BasicCustomView">
    <attr name="type" format="enum" >
        <enum name="circle" value="0"/>
        <enum name="line" value="1"/>
        <enum name="point" value="2"/>
    </attr>
</declare-styleable>
```
```xml
    <com.artofmainstreams.examples.ui.view.MyCustomView
        android:id="@+id/my_custom_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:type="circle"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```
```kotlin
class MyCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val type: Type
    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MyCustomView, defStyleAttr, 0)
        type = Type.values()[typedArray.getInt(R.styleable.MyCustomView_type, 0)]
        typedArray.recycle()
    }
    enum class Type {
        CIRCLE,
        LINE,
        POINT
    }
}
```

## TODO
Нужно доделать

## Links
+ [CustomView кратко](https://habr.com/ru/articles/727744/)
+ [Мини-курс от Android Broadcast](https://www.youtube.com/playlist?list=PL0SwNXKJbuNks7zOqvVTFRkM_unoIAC45)