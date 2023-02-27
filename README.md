# Description
Анимация через MotionLayout. CoordinatorLayout, DrawerLayout, ViewPager2

## Navigation
+ [up](https://github.com/friendboy1/Templates/tree/animation_motion-layout)
+ [root](https://github.com/friendboy1/Templates/tree/master)

## Details

### CoordinatorLayout (scene 01)
<img src="https://artofmainstreams.ru/GitHub/Examples/animation/motion-layout/examples/animation.motion-layout.examples.scene01.gif" alt="scene01" width="250"/>
1. Создадим кастомный класс <code>CollapsibleToolbar</code>, в котором будем менять progress в зависимости от состояния <code>AppBarLayout</code></br>
2. Создадим <code>CoordinatorLayout</code>), у которого у элемента <code>AppBarLayout</code> есть вложенный элемент <code>CollapsibleToolbar</code> (вместо <code>CollapsingToolbarLayout</code>)</br>
3. Добавим в <code>motion_coordinatorlayout_header.xml</code> сцену <code>xml/scene_01</code></br>

### DrawerLayout (scene 02)
<img src="https://artofmainstreams.ru/GitHub/Examples/animation/motion-layout/examples/animation.motion-layout.examples.scene02.gif" alt="scene02" width="250"/>
1. Создадим кастомный класс <code>DrawerContent</code>, в котором будем менять progress в зависимости от состояния <code>AppBarLayout</code></br>
2. Создадим <code>DrawerLayout</code>), в котором содержатся <code>DrawerContent</code> для меню и для контента</br>
3. Добавим сцены <code>xml/scene_02_menu</code> и <code>xml/scene_02_content</code></br>

### ViewPager2 (scene 03)
<img src="https://artofmainstreams.ru/GitHub/Examples/animation/motion-layout/examples/animation.motion-layout.examples.scene03.gif" alt="scene03" width="250"/>
1. Создадим кастомный класс <code>ViewPagerHeader</code>, в котором будем менять progress в зависимости от выбранной страницы</br>
2. Создадим <code>ViewPager2</code> в разметке, классы <code>ViewPagerAdapter</code>, <code>PagerViewHolder</code></br>
3. Добавим сцену <code>xml/scene_03</code></br>

## Links
+ [Introduction to MotionLayout (part III)](https://medium.com/google-developers/introduction-to-motionlayout-part-iii-47cd64d51a5)
+ [Примеры](https://github.com/android/views-widgets-samples/tree/master/ConstraintLayoutExamples)