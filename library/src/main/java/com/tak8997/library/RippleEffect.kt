package com.tak8997.library

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tak8997.library.util.toPx

internal class RippleEffect @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val rippleScale = 1.5f
    private var rippleDelay = 200
    private val rippleDurationTime = 1500
    private val rippleAmount = 3

    private val paint = Paint()
    private val animators = mutableListOf<ObjectAnimator>()
    private val animatorSet = AnimatorSet()

    init {
        rippleDelay = rippleDurationTime / rippleAmount

        repeat(rippleAmount) { index ->
            val scaleXAnimator = ObjectAnimator.ofFloat(this@RippleEffect, "ScaleX", 1.0f, rippleScale).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                startDelay = (index * rippleDelay).toLong()
                duration = rippleDurationTime.toLong()
            }
            animators.add(scaleXAnimator)

            val scaleYAnimator = ObjectAnimator.ofFloat(this@RippleEffect, "ScaleY", 1.0f, rippleScale).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                startDelay = (index * rippleDelay).toLong()
                duration = rippleDurationTime.toLong()
            }
            animators.add(scaleYAnimator)

            val alphaAnimator = ObjectAnimator.ofFloat(this@RippleEffect, "Alpha", 1.0f, 0f).apply {
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                startDelay = (index * rippleDelay).toLong()
                duration = rippleDurationTime.toLong()
            }
            animators.add(alphaAnimator)
        }

        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = ContextCompat.getColor(context, R.color.purple)

        animatorSet.apply {
            interpolator = AccelerateDecelerateInterpolator()
            playTogether(animators as Collection<Animator>?)
        }
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        val radius = width.coerceAtMost(height) / 2
        canvas?.drawCircle(radius.toFloat(), radius.toFloat(), (radius - 2.toPx()).toFloat(), paint)
    }

    fun startRipple() {
        animatorSet.start()
    }

    fun stopRipple() {
        animatorSet.end()
    }

    fun setColor(itemColor: ItemColor) {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = itemColor.color
        invalidate()
    }
}