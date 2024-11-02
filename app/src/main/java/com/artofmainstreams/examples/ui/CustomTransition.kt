package com.artofmainstreams.examples.ui

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues


class CustomTransition(private val direction: Boolean = true) : Transition() {
    override fun captureStartValues(transitionValues: TransitionValues) {
        //getBounds(transitionValues.view)
        //transitionValues.values["startBounds"] = getAbsoluteBounds(transitionValues.view)
        transitionValues.values["startBounds"] = getBounds(transitionValues.view)
//        if (direction) {
//            transitionValues.values["startBounds"] = getAbsoluteBounds(transitionValues.view)
//        } else {
//            transitionValues.values["startBounds"] = getBounds(transitionValues.view)
//        }
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        //getBounds(transitionValues.view)
//
        transitionValues.values["endBounds"] = getBounds(transitionValues.view)
        //transitionValues.values["endBounds"] = getAbsoluteBounds(transitionValues.view)
//        if (direction) {
//            transitionValues.values["endBounds"] = getBounds(transitionValues.view)
//        } else {
//            transitionValues.values["endBounds"] = getAbsoluteBounds(transitionValues.view)
//        }

    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
        val view = endValues!!.view

        val startBounds = startValues!!.values["startBounds"] as Rect?
        val endBounds = endValues.values["endBounds"] as Rect?

        // Создаем анимацию для перемещения
        view.layout(startBounds!!.left, startBounds.top, startBounds.right, startBounds.bottom)

        // Создаем анимацию для перемещения
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction

            // Вычисляем новые координаты и размеры
            val left = (startBounds.left + fraction * (endBounds!!.left - startBounds.left)).toInt()
            val top = (startBounds.top + fraction * (endBounds.top - startBounds.top)).toInt()
            val right = (startBounds.right + fraction * (endBounds.right - startBounds.right)).toInt()
            val bottom = (startBounds.bottom + fraction * (endBounds.bottom - startBounds.bottom)).toInt()

            // Устанавливаем новые размеры и положение
            view.layout(left, top, right, bottom)
//            view.rootView.layout(left, top, right, bottom)
            //view.layout(0, 0, 1080, 633)
            // Вычисляем новые координаты
//            val translationX = startBounds.left + fraction * (endBounds!!.left - startBounds.left) - view.left
//            val translationY = startBounds.top + fraction * (endBounds.top - startBounds.top) - view.top
//
//            // Устанавливаем новые значения трансляции
//            view.translationX = translationX
//            view.translationY = translationY

        }
        animator.duration = 5000
        return animator
    }

    private fun getBounds(view: View): Rect {
        return Rect(view.left, view.top, view.right, view.bottom)
    }

    private fun getAbsoluteBounds(view: View): Rect {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return Rect(location[0], location[1], location[0] + view.width, location[1] + view.height)
        //return Rect(-location[0] + view.left, -location[1] + view.top, -location[0] + view.left + view.width, -location[1] + view.top + view.height)
    }

}
