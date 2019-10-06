package com.tak8997.library

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.item_ripple_drag_drop.view.*

internal class RippleDragDropItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var title = ""
    var tagTitle = ""

    private val rippleScale = 6.0f
    private val rippleDelay = 300
    private val rippleDurationTime = 3000

//    private val animators by lazy {
//        mutableListOf<ObjectAnimator>().apply {
//            repeat(3) { index ->
//                val scaleXAnimator = ObjectAnimator.ofFloat(this, "ScaleX", 1.0f, rippleScale).apply {
//                    repeatCount = ObjectAnimator.INFINITE
//                    repeatMode = ObjectAnimator.RESTART
//                    startDelay = (index * rippleDelay).toLong()
//                    duration = rippleDurationTime.toLong()
//                }
//                add(scaleXAnimator)
//
//                val scaleYAnimator = ObjectAnimator.ofFloat(this, "ScaleY", 1.0f, rippleScale).apply {
//                    repeatCount = ObjectAnimator.INFINITE
//                    repeatMode = ObjectAnimator.RESTART
//                    startDelay = (index * rippleDelay).toLong()
//                    duration = rippleDurationTime.toLong()
//                }
//                add(scaleYAnimator)
//
//                val alphaAnimator = ObjectAnimator.ofFloat(this, "Alpha", 1.0f, 0f).apply {
//                    repeatCount = ObjectAnimator.INFINITE
//                    repeatMode = ObjectAnimator.RESTART
//                    startDelay = (index * rippleDelay).toLong()
//                    duration = rippleDurationTime.toLong()
//                }
//                add(alphaAnimator)
//            }
//        } as List<ObjectAnimator>
//    }
//
//    private val animatorSet by lazy {
//        AnimatorSet().apply {
//            interpolator = AccelerateDecelerateInterpolator()
//            playTogether(animators)
//        }
//    }
//
//    private val paint = Paint()

    init {
//        paint.isAntiAlias = true
//        paint.style = Paint.Style.FILL

        LayoutInflater.from(context).inflate(R.layout.item_ripple_drag_drop, this)
    }

//    override fun dispatchDraw(canvas: Canvas?) {
//        super.dispatchDraw(canvas)
//
//        val radius = width.coerceAtMost(height) / 2
//        canvas?.drawCircle(radius.toFloat(), radius.toFloat(), (radius - 2.toPx()).toFloat(), paint)
//    }

    fun setSelection(selected: Boolean) {
        layoutSelected.visibility = if (selected) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    fun getSelection(): Boolean {
        return layoutSelected.visibility == View.VISIBLE
    }
}