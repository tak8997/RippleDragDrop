package com.tak8997.library

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_ripple_drag_drop.view.*

internal class RippleDragDropItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val rippleScale = 3f
    private var rippleDelay = 150
    private val rippleDurationTime = 3000
    private val rippleAmount = 3

    private val paint = Paint()
    private val animatorSet = AnimatorSet()
    private val animators = mutableListOf<Animator>()
    private val rippleEffects = mutableListOf<RippleEffect>()

    var title = ""
    var tagTitle = ""

    init {
        LayoutInflater.from(context).inflate(R.layout.item_ripple_drag_drop, this)

        rippleDelay = rippleDurationTime / rippleAmount
        repeat(rippleAmount) { index ->
            val rippleEffect = RippleEffect(context)
            addView(rippleEffect)

            val scaleXAnimator =
                ObjectAnimator.ofFloat(rippleEffect, "ScaleX", 1.0f, rippleScale).apply {
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART
                    startDelay = (index * rippleDelay).toLong()
                    duration = rippleDurationTime.toLong()
                }
            animators.add(scaleXAnimator)

            val scaleYAnimator =
                ObjectAnimator.ofFloat(rippleEffect, "ScaleY", 1.0f, rippleScale).apply {
                    repeatCount = ObjectAnimator.INFINITE
                    repeatMode = ObjectAnimator.RESTART
                    startDelay = (index * rippleDelay).toLong()
                    duration = rippleDurationTime.toLong()
                }
            animators.add(scaleYAnimator)

            val alphaAnimator = ObjectAnimator.ofFloat(rippleEffect, "Alpha", 1.0f, 0f).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                startDelay = (index * rippleDelay).toLong()
                duration = rippleDurationTime.toLong()
            }
            animators.add(alphaAnimator)

            rippleEffects.add(rippleEffect)
        }

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = ContextCompat.getColor(context, R.color.purple)

        animatorSet.apply {
            interpolator = AccelerateDecelerateInterpolator()
            playTogether(animators)
            start()
        }
    }

    fun setSelection(selected: Boolean) {
        if (selected) {
            showRippleEffects()
            animatorSet.start()
        } else {
            hideRippleEffects()
            animatorSet.pause()
        }
    }

    fun getSelection(): Boolean {
        return layoutSelected.visibility == View.VISIBLE
    }

    fun setColor(itemColor: ItemColor) {
        imageSelected.setColorFilter(itemColor.color)
        rippleEffects.forEach {
            it.setColor(itemColor)
        }
    }

    private fun showRippleEffects() {
        rippleEffects.forEach {
            it.visibility = View.VISIBLE
        }
    }

    private fun hideRippleEffects() {
        rippleEffects.forEach {
            it.visibility = View.INVISIBLE
        }
    }
}