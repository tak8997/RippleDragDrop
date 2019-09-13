package com.tak8997.library

import android.content.Context
import android.os.Vibrator
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Space
import kotlinx.android.synthetic.main.ripple_drag_drop.view.*

class RippleDragDrop @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val dragDropItems = mutableListOf<RippleDragDropItem>()
    private var index: Int = -1
    private var onItemSelectedListener: ((index: Int, item: RippleDragDropItem) -> Unit)? = null

    init {
        inflate(this.context, R.layout.ripple_drag_drop, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleDragDrop, defStyleAttr, 0)
        val items = typedArray.getString(R.styleable.RippleDragDrop_items)?.split(",") ?: mutableListOf()
        val tags = typedArray.getString(R.styleable.RippleDragDrop_tags)?.split(",") ?: mutableListOf()

        setItems(items, tags)

        if (typedArray.hasValue(R.styleable.RippleDragDrop_selection)) {
            val selection = typedArray.getInt(R.styleable.RippleDragDrop_selection, -1)
            setSelection(selection)
        }

        typedArray.recycle()
    }

    fun setItems(items: List<String>, tags: List<String>) {
        dragDropItems.clear()
        layoutItems.removeAllViews()

        items.zip(tags).forEachIndexed { index, (name, tag) ->
                if (index != 0) {
                    addSpace()
                }

                createRippleDragDropItem(index, name, tag).let {
                    layoutItems.addView(it)
                    dragDropItems.add(it)
                }
            }

        setSelection(-1)
    }

    fun setSelection(index: Int) {
        deselectAll()

        val realIndex = if (index < 0) dragDropItems.lastIndex else index
        val item = dragDropItems[realIndex]
        item.setSelection(true)

        if (this.index != realIndex) {
            this.index = realIndex
        } else {
            onItemSelectedListener?.invoke(realIndex, item)
        }
    }

    private fun createRippleDragDropItem(index: Int, name: String, tag: String): RippleDragDropItem {
        val item = RippleDragDropItem(context).apply {
            title = name
            tagTitle = tag
            setOnClickListener {
                onItemClicked(index)
            }
        }

        return item
    }

    private fun onItemClicked(index: Int) {
        vibrate()
        setSelection(index)
    }

    private fun vibrate() {
        val vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        vibe?.vibrate(30)
    }

    private fun addSpace() {
        val space = Space(context, null, 0)
        layoutItems.addView(space)

        val lParams = space.layoutParams as LinearLayout.LayoutParams
        lParams.weight = 1.0f
    }

    private fun deselectAll() {
        dragDropItems.forEach { it.setSelection(false) }
    }
}