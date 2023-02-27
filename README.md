# Description
Анимация через MotionLayout. Основы

## Navigation
+ [up](https://github.com/friendboy1/Templates/tree/animation_motion-layout)
+ [root](https://github.com/friendboy1/Templates/tree/master)

## Details

### Быстрый старт (scene 01)
<img src="https://artofmainstreams.ru/GitHub/Examples/animation/motion-layout/basic/animation.motion-layout.basic.scene01.gif" alt="scene01" width="250"/>
1. Заменим ConstraintLayout на MotionLayout</br>
2. Создадим layout стартовый (<code>motion_01_cl_start.xml</code>) и конечный (<code>motion_01_cl_end.xml</code>)</br>
3. Создадим сцену (xml/scene_01) и добавим в MotionLayout (app:layoutDescription="@xml/scene_01")</br>

#### OnSwipe Handler
![OnSwipe Handler Params](https://miro.medium.com/max/500/1*13hEIVvjhOoiSDRvMkeZ7g.png "https://medium.com/google-developers/introduction-to-motionlayout-part-i-29208674b10d")\
Атрибуты:
+ ```touchAnchorId``` - указываем вьюшку для анимации
+ ```touchAnchorSide``` - сторона вьюшки, которую должны тащить
+ ```dragDirection``` - направление движения

#### MotionLayout содержит следующие атрибуты:
+ ```app:layoutDescription="reference"``` ссылка на xml файл сцены
+ ```app:applyMotionScene="boolean"``` применять или не применять сцену
+ ```app:showPaths="boolean"``` отображать или не отображать путь движения (не для продакшена)
+ ```app:progress="float"``` позволяет указать процесс перехода от 0 до 1 (как я понял, начальное состояние)
+ ```app:currentState="reference"``` указать конкретный ConstraintSet

#### ConstraintSet в MotionScene содержит следующие дополнительные атрибуты:
+ ```alpha```
+ ```visibility```
+ ```rotation```, ```rotation[X/Y]```
+ ```translation[X/Y/Z]```
+ ```scaleX/Y```

### Более правильное решение (scene 02)
<img src="https://artofmainstreams.ru/GitHub/Examples/animation/motion-layout/basic/animation.motion-layout.basic.scene02.gif" alt="scene02" width="250"/>
1. Не будем создавать отдельно разметки для начального и конечного состояния, а занесём всё в сцену <code>xml/scene_02</code></br>
2. В MotionLayout укажем соответствующую сцену <code>app:layoutDescription="@xml/scene_02"</code></br>

### Добавляем кастомные атрибуты (scene 03)
<img src="https://artofmainstreams.ru/GitHub/Examples/animation/motion-layout/basic/animation.motion-layout.basic.scene03.gif" alt="scene03" width="250"/>
1. В <code>xml/scene_03</code> в блоке <code>Constraint</code> добавим кастомный атрибут цвета <code>CustomAttribute</code> для начального и конечного состояния

#### CustomAttribute
![CustomAttribute](https://miro.medium.com/max/504/1*J4wdLZ8zoaTsQ8f6oC3SMA.png "https://miro.medium.com/max/504/1*J4wdLZ8zoaTsQ8f6oC3SMA.png"))

### ImageFilterView меняющаяся картинка (scene 04)
<img src="https://artofmainstreams.ru/GitHub/Examples/animation/motion-layout/basic/animation.motion-layout.basic.scene04.gif" alt="scene04" width="250"/>
В ConstraintLayout 2.0 есть небольшой утилитный класс <code>ImageFilterView</code> (наследник <code>AppCompatImageView</code>)</br>
1. Меняем View на ImageFilterView</br>
2. В <code>xml/scene_04</code> указываем атрибут crossfade с 0 до 1</br>

### ImageFilterView затемняющаяся картинка (scene 05)
<img src="https://artofmainstreams.ru/GitHub/Examples/animation/motion-layout/basic/animation.motion-layout.basic.scene05.gif" alt="scene05" width="250"/>
1. В <code>xml/scene_05</code> указываем атрибут saturation с 1 до 0

#### ImageFilterView
+ ```saturation```: 0 = greyscale, 1 = original, 2 = hypersaturated
+ ```contrast```: 0 = unchanged, 1 = grey, 2 = high contrast
+ ```warmth```: 0.5 = cold (blue tint), 1 = neutral, 2 = warm (red tint))
+ ```crossfade```: (with ```app:altSrc```)

### KeyFrames со смещением при передвижении (scene 06)
<img src="https://artofmainstreams.ru/GitHub/Examples/animation/motion-layout/basic/animation.motion-layout.basic.scene06.gif" alt="scene06" width="250"/>
Смещение позиции можно указать различными вариантами: <code>pathRelative</code>, <code>deltaRelative</code>, <code>parentRelative</code></br>
1. Добавляем <code>KeyPosition</code> в <code>KeyFrameSet</code> в <code>xml/scene_06</code>

#### KeyPosition
![KeyPosition](https://miro.medium.com/max/502/1*CT5m53LvQNC_viFZwNRx-w.png "https://miro.medium.com/max/502/1*CT5m53LvQNC_viFZwNRx-w.png")

### KeyFrames с поворотом и увеличением (scene 07)
<img src="https://artofmainstreams.ru/GitHub/Examples/animation/motion-layout/basic/animation.motion-layout.basic.scene07.gif" alt="scene07" width="250"/>
1. Добавляем <code>KeyAttribute</code> в <code>KeyFrameSet</code> в <code>xml/scene_07</code>

#### KeyAttribute
![KeyAttribute](https://miro.medium.com/max/502/1*MHp3ZvHe7ZzHTntS5SurlQ.png "https://miro.medium.com/max/502/1*MHp3ZvHe7ZzHTntS5SurlQ.png")

## Links
+ [Introduction to MotionLayout (part I)](https://medium.com/google-developers/introduction-to-motionlayout-part-i-29208674b10d)
+ [Introduction to MotionLayout (part II)](https://medium.com/google-developers/introduction-to-motionlayout-part-ii-a31acc084f59)
+ [Примеры](https://github.com/android/views-widgets-samples/tree/master/ConstraintLayoutExamples)