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

    private val rippleEffect by lazy {
        RippleEffect(context)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.item_ripple_drag_drop, this)
    }

    fun setSelection(selected: Boolean) {
        if (selected) {
            addView(rippleEffect)
            rippleEffect.startRipple()
            layoutSelected.visibility = View.VISIBLE
        } else {
            rippleEffect.stopRipple()
            removeView(rippleEffect)
            layoutSelected.visibility = View.INVISIBLE
        }
    }

    fun getSelection(): Boolean {
        return layoutSelected.visibility == View.VISIBLE
    }

    fun setColor(itemColor: ItemColor) {
        imageSelected.setColorFilter(itemColor.color)
        rippleEffect.setColor(itemColor)
    }
}