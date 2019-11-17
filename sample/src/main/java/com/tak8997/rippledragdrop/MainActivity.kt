package com.tak8997.rippledragdrop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(rippleDragDrop) {
            setItemCount(4)
            setItemSize(50)
            setItemGap(50)
        }
    }
}
