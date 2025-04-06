package com.example.pumlab5.model

import android.view.Gravity
import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton

class Tile(context: Context, val number: Int?) : AppCompatButton(context) {
    init {
        layoutParams = ViewGroup.LayoutParams(200, 200)
        text = number?.toString() ?: ""
        gravity = Gravity.CENTER
        textSize = 24f
    }
}
