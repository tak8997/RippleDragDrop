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
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import com.tak8997.library.util.toDp
import com.tak8997.library.util.toPx
import kotlin.math.roundToInt


class RippleDragDrop @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_ITEM_COUNT = 0
        private const val DEFAULT_ITEM_SIZE = 0
        private const val DEFAULT_ITEM_GAP = 0
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

    private var itemCount = DEFAULT_ITEM_COUNT
    private var itemSize = DEFAULT_ITEM_SIZE.toDp()
    private var itemGap = DEFAULT_ITEM_GAP.toDp()
    private var selectedIndex: Int = -1
    private var selectedItemPositionY = 0f
    private var onItemSelectedListener: ((index: Int, item: RippleDragDropItem) -> Unit)? = null

    private var layoutMargin = DEFAULT_LAYOUT_MARGIN
    private var borderWidth = DEFAULT_BORDER_WIDTH
    private var borderColor = DEFAULT_BORDER_COLOR

    init {
        setupAttrs(context, attrs, defStyleAttr)
        setupItems()
        setupSelection(selectedIndex)
        setupViews()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        dragBgPaint.color = getDragColor(selectedIndex).color
        dragBgPaint.alpha = 100
        dragBgPaint.isAntiAlias = true
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
            setupSelection(0)
            return true
        } else if (event.y > height) {
            setupSelection(dragDropItems.lastIndex)
            return true
        }

        var rating = event.y / height * dragDropItems.size
        rating = (rating * 100).roundToInt() / 100f
        setupSelection(rating.toInt())

        return true
    }

    fun setItemCount(count: Int) {
        if (itemCount == count) {
            return
        }

        itemCount = count
        setupItems()
        setupSelection(selectedIndex)
        setupViews()
        invalidate()
    }

    fun setItemSize(size: Int) {
        if (itemSize == size) {
            return
        }

        itemSize = size.toPx()
        setupItems()
        setupSelection(selectedIndex)
        setupViews()
        invalidate()
    }

    fun setItemGap(gap: Int) {
        if (itemGap == gap) {
            return
        }

        itemGap = gap.toPx()
        setupItems()
        setupSelection(selectedIndex)
        setupViews()
        invalidate()
    }

    private fun setupAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RippleDragDrop, defStyleAttr, 0)

        itemCount = typedArray.getInt(R.styleable.RippleDragDrop_itemCount, DEFAULT_ITEM_COUNT)
        itemSize = typedArray.getDimensionPixelSize(R.styleable.RippleDragDrop_itemSize, DEFAULT_ITEM_SIZE.toDp())
        itemGap = typedArray.getDimensionPixelSize(R.styleable.RippleDragDrop_itemGap, DEFAULT_ITEM_GAP.toDp())
        tags.addAll(typedArray.getString(R.styleable.RippleDragDrop_tags)?.split(",") ?: mutableListOf())

        typedArray.recycle()
    }

    private fun setupItems() {
        dragDropItems.clear()
        removeAllViews()

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

        selectedIndex = dragDropItems.lastIndex
    }

    private fun setupSelection(index: Int) {
        if (index < 0 || index > dragDropItems.lastIndex) {
            return
        }

        deselectAll()

        val item = dragDropItems[index]
        item.setSelection(true)
        item.setColor(getDragColor(selectedIndex))
        selectedItemPositionY = item.y

        if (selectedIndex != index) {
            selectedIndex = index
        } else {
            onItemSelectedListener?.invoke(index, item)
        }

        invalidate()
    }

    private fun setupViews() {
        if (dragDropItems.size == 0) {
            return
        }

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

                    if (parent is ViewGroup) {
                        val parent = (parent as ViewGroup).parent
                        if (parent is ViewGroup) {
                            parent.clipChildren = false
                            parent.clipToPadding = false
                        }
                    }
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

    private fun getDragColor(selecteIndex: Int): ItemColor {
        return when (val itemSize = dragDropItems.size) {
            1,2 -> return if (selecteIndex == 1) ItemColor.Purple else ItemColor.Red
            3 -> return when (selecteIndex) {
                2 -> ItemColor.Purple
                1 -> ItemColor.Green
                else -> ItemColor.Red
            }
            else -> when {
                selecteIndex >= itemSize * 3 / 4 -> ItemColor.Purple
                selecteIndex >= itemSize * 2 / 4 -> ItemColor.Green
                selecteIndex >= itemSize * 1 / 4 -> ItemColor.Blue
                else -> ItemColor.Red
            }
        }
    }
}
