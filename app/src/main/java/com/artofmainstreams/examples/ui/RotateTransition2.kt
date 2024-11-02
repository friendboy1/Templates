package com.artofmainstreams.examples.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues

class RotateTransition2 : Transition() {
    private val PROPNAME_ROTATION = "customtransition:rotation"

    override fun captureStartValues(transitionValues: TransitionValues) {
        transitionValues.values[PROPNAME_ROTATION] = transitionValues.view.rotation
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        transitionValues.values[PROPNAME_ROTATION] = transitionValues.view.rotation + 90f
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (startValues == null || endValues == null) return null

        val view = endValues.view
        val startRotation = startValues.values[PROPNAME_ROTATION] as Float
        val endRotation = endValues.values[PROPNAME_ROTATION] as Float

        return ObjectAnimator.ofFloat(view, View.ROTATION, startRotation, endRotation)
    }
}