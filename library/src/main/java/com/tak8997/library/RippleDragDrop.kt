package com.tak8997.library

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Vibrator
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.tak8997.library.util.toPx
import kotlin.math.roundToInt


class RippleDragDrop @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_LAYOUT_MARGIN = 2
        private const val DEFAULT_BORDER_COLOR = Color.LTGRAY
        private const val DEFAULT_BORDER_WIDTH = 3.0f
    }

    private val rectF by lazy {
        RectF(0f, 0f, width.toFloat(), height.toFloat())
    }
    private val dragRectF by lazy {
        RectF()
    }
    private val bgPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            strokeWidth = borderWidth
            color = Color.parseColor("#f1f1f1")
            style = Paint.Style.FILL
        }
    }
    private val dragBgPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.parseColor("#5f14d3")
            style = Paint.Style.FILL
            alpha = 100
        }
    }
    private val dragDropItems = mutableListOf<RippleDragDropItem>()
    private val tags = mutableListOf<String>()

    private var itemCount = 0
    private var selectedIndex: Int = -1
    private var itemSize = 0
    private var itemGap = 0
    private var selectedItemPositionY = 0f
    private var onItemSelectedListener: ((index: Int, item: RippleDragDropItem) -> Unit)? = null

    private var layoutMargin = DEFAULT_LAYOUT_MARGIN
    private var borderWidth = DEFAULT_BORDER_WIDTH
    private var borderColor = DEFAULT_BORDER_COLOR

    init {
        setAttrs(context, attrs, defStyleAttr)
        setItems()
        setSelection(selectedIndex)
        setViews()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        dragRectF.left = 0f
        dragRectF.top = selectedItemPositionY
        dragRectF.right = width.toFloat()
        dragRectF.bottom = height.toFloat()
        canvas?.drawRoundRect(rectF, (itemSize / 2).toFloat(), (itemSize / 2).toFloat(), bgPaint)
        canvas?.drawRoundRect(dragRectF, (itemSize / 2).toFloat(), (itemSize / 2).toFloat(), dragBgPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        checkNotNull(event?.y)

        if (event!!.y < 0) {
            setSelection(0)
            return true
        } else if (event.y > height) {
            setSelection(dragDropItems.lastIndex)
            return true
        }

        var rating = event.y / height * dragDropItems.size
        rating = (rating * 100).roundToInt() / 100f
        setSelection(rating.toInt())

        return true
    }

    fun setItems() {
        repeat(itemCount) { index ->
            val item = RippleDragDropItem(context)
            val itemParams = LayoutParams(itemSize, itemSize)
            itemParams.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin)
            itemParams.topMargin = if (index == 0) layoutMargin else itemGap
            item.layoutParams = itemParams
            item.setSelection(false)
            addView(item)

            dragDropItems.add(item)
        }
    }

    fun setSelection(index: Int) {
        if (index < 0 || index > dragDropItems.lastIndex) {
            return
        }

        deselectAll()

        val item = dragDropItems[index]
        item.setSelection(true)
        selectedItemPositionY = item.y

        if (selectedIndex != index) {
            selectedIndex = index
        } else {
            onItemSelectedListener?.invoke(index, item)
        }

        invalidate()
    }

    private fun setAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleDragDrop, defStyleAttr, 0)

        itemCount = typedArray.getInt(R.styleable.RippleDragDrop_itemCount, 0)
        tags.addAll(typedArray.getString(R.styleable.RippleDragDrop_tags)?.split(",") ?: mutableListOf())
        selectedIndex = typedArray.getInt(R.styleable.RippleDragDrop_selection, dragDropItems.lastIndex)
        itemSize = typedArray.getDimensionPixelSize(R.styleable.RippleDragDrop_itemSize, 50.toPx())
        itemGap = typedArray.getDimensionPixelSize(R.styleable.RippleDragDrop_itemSize, 30.toPx())

        typedArray.recycle()
    }

    private fun setViews() {
        clipChildren = false
        clipToPadding = false

        orientation = VERTICAL
        gravity = Gravity.CENTER_VERTICAL
        setWillNotDraw(false)

        dragDropItems[dragDropItems.lastIndex].run {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    selectedItemPositionY = dragDropItems[dragDropItems.lastIndex].y
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    }

    private fun vibrate() {
        val vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        vibe?.vibrate(30)
    }

    private fun deselectAll() {
        dragDropItems.forEach { it.setSelection(false) }
    }
}