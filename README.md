# Description
По задумке, данный репозиторий планируется для содержания различных шпаргалок для меня.

## Branches
+ [main](https://github.com/friendboy1/Templates/tree/master)
+ animation/motion-layout/[basic](https://github.com/friendboy1/Templates/tree/animation/motion-layout%2Fbasic) - пример анимации с использованием MotionLayout
+ animation/motion-layout/[examples](https://github.com/friendboy1/Templates/tree/animation/motion-layout/examples) - пример анимации с использованием CoordinatorLayout, DrawerLayout, ViewPager2
+ animation/motion-layout/[keyframes](https://github.com/friendboy1/Templates/tree/animation/motion-layout/keyframes) - более подробное изучение keyframes

## Details

#### Основные сущности
+ Paint
+ Rect/RectF
+ Point
+ Path
+ Matrix
+ Bitmap

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
