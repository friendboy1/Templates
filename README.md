# Description
MotionLayout. Keyframes

## Navigation
+ [up](https://github.com/friendboy1/Templates/tree/animation_motion-layout)
+ [root](https://github.com/friendboy1/Templates/tree/master)

## Details

Существует 4 вида типа KeyFrame, каждый независим от другого:</br>

+ KeyPosition
+ KeyAttribute
+ KeyCycle
+ KeyTimeCycle

Все они имеют следующие общие атрибуты:</br>

+ <code>motion:framePosition</code> - в какой момент времени применяется keyframe (0..100)</br>
+ <code>motion:motionTarget</code> - на какой объект влияет этот keyframe</br>
+ <code>motion:transitionEasing</code> - какую кривую пути использовать (по умолчанию линейная)</br>
+ <code>motion:curveFit</code> - spline (default) или linear, какая интерполяционная кривая
  соответствует keyfram'ам. По умолчанию - монотонная сплайн-кривая (monotonic spline curve), для
  более плавных переходов. Но можно выбрать линейные сегменты (linear segments) вместо этого</br>

#### Position Keyframes

Позволяют указать путь движения. Можно использовать и несколько ConstraintSet вместо, если позволяют
установить состояния покоя, затем повесить слушателя <code>TransitionListener</code></br>

```xml

<KeyFrameSet>
    <KeyPosition 
        motion:keyPositionType="pathRelative" 
        motion:percentX="0.75" 
        motion:percentY="-0.3"
        motion:framePosition="25" 
        motion:target="@id/button" />
    <KeyPosition 
        motion:keyPositionType="pathRelative" 
        motion:percentY="-0.4"
        motion:framePosition="50" 
        motion:target="@id/button" />
    <KeyPosition 
        motion:keyPositionType="pathRelative"
        motion:percentX="0.25" 
        motion:percentY="-0.3"
        motion:framePosition="75" 
        motion:target="@id/button" />
</KeyFrameSet>
```

#### XML Представление

```xml

<Transition>
    <KeyFrameSet>
        <KeyPosition 
            motion:keyPositionType="parentRelative" 
            motion:percentY="0.25"
            motion:framePosition="50" 
            motion:target="@+id/button" />
    </KeyFrameSet>
</Transition>
```

+ `target` - widget, к которому применяется keyframe</br>
+ `framePosition` - позиция, когда применяется keyframe</br>
+ `keyPositionType` - координатная система, использующая <code>parentRelative</code>, <code>
  deltaRelative</code>, <code>pathRelative</code></br>
+ `percentX / percentY` - координата x/y для указанной системы координат</br>
  ![parentRelative](https://miro.medium.com/max/1400/1*PgRVDmgacQFedFr9X1oRpQ.png "https://medium.com/google-developers/defining-motion-paths-in-motionlayout-6095b874d37")
  ![deltaRelative](https://miro.medium.com/max/1400/1*lPeWWsomgV10QRFBT7i9kQ.png "https://medium.com/google-developers/defining-motion-paths-in-motionlayout-6095b874d37")
  ![pathRelative](https://miro.medium.com/max/1400/1*CEBdlTMmXanZBg96IFDhLg.png "https://medium.com/google-developers/defining-motion-paths-in-motionlayout-6095b874d37")

#### Arc Motion

Для движения по дуге, для начального <code>ConstraintSet</code> нужно установить <code>motion:
pathMotionArc</code></br>
Может принять параметр <code>startHorizontal</code> или <code>startVertical</code>. Влияет на то,
будет ли путь выпуклым или вогнутым.</br>
Также можно в середине пути менять направление движения с помощью <code>motion:pathMotionArc</code>
.</br>
Может быть <code>flip</code>, чтобы изменить изгиб дуги, <code>none</code> для линейного движения,
либо <code>startHorizontal</code>/<code>startVertical</code>

#### Easing

В зависимости от расстония, на котором виджет, можно управлять временем движения. Но внутри одной
секций скорость одна.</br>
В <code>ConstraintSets</code> или <code>Keyframes</code> можно задать атрибут <code>motion:
transitionEasing</code> со следующими параметрами:</br>

+ <code>cubic(float, float , float, float)</code>, где аргументы x1, y1, x2, y2 являются
  контрольными точками кубической кривой безье от (0,0) до (1,1)
+ <code>standard</code>, <code>accelerate</code>, <code>decelerate</code> - предзаданные кривые.
  Подробности в
  [Material Design definitions](https://material.io/design/motion/speed.html#easing "Material Design definitions")

#### KeyAttribute

Можно менять атрибуты для конкретного момента времени

```xml

<KeyFrameSet>
    <KeyAttribute 
        android:scaleX="2" 
        android:scaleY="2" 
        android:rotation="-45"
        motion:framePosition="50" 
        motion:target="@id/button" />
</KeyFrameSet>
```

#### Supported Attributes

Поддерживаемые атрибуты из коробки:</br>

+ <code>android:visibility</code>
+ <code>android:alpha</code>
+ <code>android:elevation</code> начиная с SDK 21
+ <code>android:rotation</code>
+ <code>android:rotationX</code>
+ <code>android:rotationY</code>
+ <code>android:scaleX</code>
+ <code>android:scaleY</code>
+ <code>android:translationX</code>
+ <code>android:translationY</code>
+ <code>android:translationZ</code> начиная с SDK 21

#### Custom Attributes

Можно использовать кастомные атрибуты в <code>ConstraintSets</code> или <code>Keyframes</code>,
создав child <code><CustomAttribute></code>. Он должен иметь имя <code>attributeName</code> и одно
из следующих значений:</br>

+ <code>customColorValue</code>
+ <code>customColorDrawableValue</code>
+ <code>customIntegerValue</code>
+ <code>customFloatValue</code>
+ <code>customStringValue</code>
+ <code>customDimension</code>
+ <code>customBoolean</code></br>

Например:
```xml
<ConstraintSet android:id="@+id/start">
    <Constraint
        android:id="@+id/button" ...>
        <CustomAttribute
            motion:attributeName="backgroundColor"
            motion:customColorValue="#D81B60"/>
    </Constraint>
</ConstraintSet>
<ConstraintSet android:id="@+id/end">
    <Constraint
        android:id="@+id/button" ...>
        <CustomAttribute
            motion:attributeName="backgroundColor"
            motion:customColorValue="#9999FF"/>
    </Constraint>
</ConstraintSet>
```