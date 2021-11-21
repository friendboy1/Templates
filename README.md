# Description
По задумке, данный репозиторий планируется для содержания различных шпаргалок для меня.

## Branches
+ [main](https://github.com/friendboy1/Templates/tree/master)
+ animation/motion-layout/[basic](https://github.com/friendboy1/Templates/tree/animation/motion-layout%2Fbasic) - пример анимации с использованием MotionLayout
+ animation/motion-layout/[examples](https://github.com/friendboy1/Templates/tree/animation/motion-layout/examples) - пример анимации с использованием CoordinatorLayout, DrawerLayout, ViewPager2
+ animation/motion-layout/[keyframes](https://github.com/friendboy1/Templates/tree/animation/motion-layout/keyframes) - более подробное изучение keyframes

## Details
Существует 4 вида типа KeyFrame, каждый независим от другого:</br>
+ KeyPosition
+ KeyAttribute
+ KeyCycle
+ KeyTimeCycle

Все они имеют следующие общие атрибуты:</br>
+ motion:framePosition - в какой момент времени применяется keyframe (0..100)</br>
+ motion:motionTarget - на какой объект влияет этот keyframe</br>
+ motion:transitionEasing - какую кривую пути использовать (по умолчанию линейная)</br>
+ motion:curveFit - spline (default) или linear, какая интерполяционная кривая соответствует keyfram'ам. 
  По умолчанию - монотонная сплайн-кривая (monotonic spline curve), для более плавных переходов. 
  Но можно выбрать линейные сегменты (linear segments) вместо этого</br>
  
#### Position Keyframes
Позволяют указать путь движения. Можно использовать и несколько ConstraintSet вместо, если позволяют установить 
состояния покоя, затем повесить слушателя <code>TransitionListener</code></br>
<code>
<KeyFrameSet>
    <KeyPosition
        motion:keyPositionType="pathRelative"
        motion:percentX="0.75"
        motion:percentY="-0.3"
        motion:framePosition="25"
        motion:target="@id/button"/>
    <KeyPosition
        motion:keyPositionType="pathRelative"
        motion:percentY="-0.4"
        motion:framePosition="50"
        motion:target="@id/button"/>
    <KeyPosition
        motion:keyPositionType="pathRelative"
        motion:percentX="0.25"
        motion:percentY="-0.3"
        motion:framePosition="75"
        motion:target="@id/button"/>
    </KeyFrameSet>
</code>

#### XML Представление
<code>
<Transition ...>
    <KeyFrameSet>
        <KeyPosition
            motion:keyPositionType="parentRelative"
            motion:percentY="0.25"
            motion:framePosition="50"
            motion:target="@+id/button"/>
    </KeyFrameSet>
</Transition>
</code>
+ <code>target</code> - widget, к которому применяется keyframe</br>
+ <code>framePosition</code> - позиция, когда применяется keyframe</br>
+ <code>keyPositionType</code> - координатная система, использующая <code>parentRelative</code>, <code>deltaRelative</code>, <code>pathRelative</code></br>
+ <code>percentX / percentY</code> - координата x/y для указанной системы координат</br>
[parentRelative](https://miro.medium.com/max/1400/1*PgRVDmgacQFedFr9X1oRpQ.png "https://miro.medium.com/max/1400/1*PgRVDmgacQFedFr9X1oRpQ.png") 
[deltaRelative](https://miro.medium.com/max/1400/1*lPeWWsomgV10QRFBT7i9kQ.png "https://miro.medium.com/max/1400/1*lPeWWsomgV10QRFBT7i9kQ.png")
[pathRelative](https://miro.medium.com/max/1400/1*CEBdlTMmXanZBg96IFDhLg.png "https://miro.medium.com/max/1400/1*CEBdlTMmXanZBg96IFDhLg.png")

#### Arc Motion
Для движения по дуге, для начального <code>ConstraintSet</code> нужно установить <code>motion:pathMotionArc</code>
Может принять параметр <code>startHorizontal</code> или <code>startVertical</code>. Влияет на то, будет ли путь выпуклым или вогнутым.
Также можно в середине пути менять направление движения с помощью <code>motion:pathMotionArc</code>. 
Может быть <code>flip</code>, чтобы изменить изгиб дуги, <code>none</code> для линейного движения, либо <code>startHorizontal</code>/<code>startVertical</code>