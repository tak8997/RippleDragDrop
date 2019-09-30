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

    init {
        LayoutInflater.from(context).inflate(R.layout.item_ripple_drag_drop, this)
    }

    fun setSelection(selected: Boolean) {
        layoutSelected.visibility = if (selected) View.VISIBLE else View.INVISIBLE
    }

    fun getSelection(): Boolean {
        return layoutSelected.visibility == View.VISIBLE
    }
}