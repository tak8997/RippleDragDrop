package com.tak8997.library

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import com.tak8997.library.util.toPx

internal class RippleEffect @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val paint = Paint()

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        val radius = width.coerceAtMost(height) / 2
        canvas?.drawCircle(
            radius.toFloat(),
            radius.toFloat(),
            (radius - 2.toPx()).toFloat(),
            paint
        )
    }

    fun setColor(itemColor: ItemColor) {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = itemColor.color
        invalidate()
    }
}