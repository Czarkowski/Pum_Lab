package com.example.pumlab6

import android.graphics.RectF

class Block(x: Int, y: Int, width: Int, height: Int) {
    val rect = RectF(x.toFloat(), y.toFloat(), (x + width).toFloat(), (y + height).toFloat())
}
