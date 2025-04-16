package com.example.pumlab6

import android.graphics.RectF

class Paddle {
    val rect = RectF(400f, 1700f, 680f, 1720f)

    fun moveTo(x: Float) {
        val width = rect.width()
        rect.left = x - width / 2
        rect.right = x + width / 2
    }
}
