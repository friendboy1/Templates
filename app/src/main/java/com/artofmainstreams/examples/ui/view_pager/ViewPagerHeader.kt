package com.artofmainstreams.examples.ui.view_pager

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout

/**
 * Шарик, передвигающийся в зависимости от выбранной страницы
 */
class ViewPagerHeader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {
    var numPages = 3

    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        progress = (position + positionOffset) / (numPages - 1)
    }
}