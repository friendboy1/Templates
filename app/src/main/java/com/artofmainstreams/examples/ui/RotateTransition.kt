package com.artofmainstreams.examples.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues


class RotateTransition(private val startAngle: Float, private val endAngle: Float) : Transition() {

    override fun captureStartValues(transitionValues: TransitionValues) {
//        captureValues(transitionValues)

        transitionValues.values[ROTATE] = startAngle
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
//        captureValues(transitionValues)
        transitionValues.values[ROTATE] = endAngle
    }

    private fun captureValues(transitionValues: TransitionValues) {
        val view = transitionValues.view
        transitionValues.values[WIDTH] = view.width / view.resources.displayMetrics.density
        transitionValues.values[HEIGHT] = view.height / view.resources.displayMetrics.density
        transitionValues.values[ROTATE] = view.rotation
    }

    override fun createAnimator(sceneRoot: ViewGroup, startValues: TransitionValues?, endValues: TransitionValues?): Animator? {
        if (startValues != null && endValues != null) {
            val view = endValues.view
            // сначала возвращаем во View изначальный размер
            view.rotation = startAngle
            // и анимируем его до требуемого
            return ObjectAnimator.ofFloat(view, "rotation", startAngle, endAngle).apply {
                duration = 5000
            }
        }
        return null
    }

    private companion object {
        const val WIDTH = "widthTransition:width"
        const val HEIGHT = "heightTransition:height"
        const val ROTATE = "rotateTransition:rotate"
    }
}