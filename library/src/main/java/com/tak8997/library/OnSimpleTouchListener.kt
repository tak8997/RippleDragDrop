package com.tak8997.library

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

open class OnSimpleTouchListener(context: Context) : View.OnTouchListener {

    companion object {
        private const val SWIPE_THRESHOLD = 300
        private const val SWIPE_VELOCITY_THRESHOLD = 300
    }

    private val gestureDetector: GestureDetector = GestureDetector(context, GestureListener())

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            Log.d("MY_LOG", "onDown: ${e.y}")
            return true
        }
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean { return true }
        override fun onLongPress(e: MotionEvent) {}
        override fun onDoubleTap(e: MotionEvent): Boolean { return true }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            var result = false
            try {
                val diffY = e2.y - e1.y
                Log.d("MY_LOG", "onFling : $diffY")
                if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }
    }

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}
}