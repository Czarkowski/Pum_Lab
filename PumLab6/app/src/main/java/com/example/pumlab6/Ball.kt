package com.example.pumlab6

import android.graphics.RectF

class Ball {
    var rect = RectF(1080f / 2f - 25, 600f, 1080f / 2f - 25 + 50, 650f)
    private var dx = 20f
    private var dy = 20f

    fun update() {
        rect.offset(dx, dy)
        if (rect.left < 0 || rect.right > 1080) dx = -dx
        if (rect.top < 0) dy = -dy
    }

    fun reverseY() {
        dy = -dy
    }

    fun reset() {
        rect.set(1080f / 2f - 25, 600f, 1080f / 2f - 25 + 50, 650f)
        dx = 20f
        dy = 20f
    }
}
