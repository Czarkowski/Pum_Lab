package com.example.pumlab6

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), Runnable {
    private var thread : Thread = Thread(this)
    private var playing = false
    private val paint = Paint()
    private val ball = Ball()
    private val paddle = Paddle()
    private val blocks = mutableListOf<Block>()
    private var isGameOver = false

    init {
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                resetBlocks()
//                playing = true
//                thread.start()
            }
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
            override fun surfaceDestroyed(holder: SurfaceHolder) { pause() }
        })
    }

    private var score = 0
    private var level = 1
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 60f
    }


    private fun resetBlocks() {
        blocks.clear()
        val rows = if (level == 1) 2 else 6
        val cols = if (level == 1) 3 else 7
        val blockWidth = width / cols
        val blockHeight = 60

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                blocks.add(Block(j * blockWidth, i * blockHeight + 100, blockWidth - 10, blockHeight - 10))
            }
        }
    }

    override fun run() {
        while (playing) {
//            Log.d("GAME", "Game loop running")
            update()
            draw()
            Thread.sleep(16)
        }
    }

    private fun update() {
        ball.update()
        if (RectF.intersects(ball.rect, paddle.rect)) {
            ball.reverseY()
        }

        val iterator = blocks.iterator()
        while (iterator.hasNext()) {
            val block = iterator.next()
            if (RectF.intersects(ball.rect, block.rect)) {
                ball.reverseY()
                iterator.remove()
                score += 10
                break
            }
        }

        if (blocks.isEmpty()) {
            level++
            if (level > 2) {
                playing = false // Gra wygrana
            } else {
                resetBlocks()
                ball.reset()
            }
        }

        if (ball.rect.bottom > height) {
            resetGame() // koniec gry
        }
    }

    private fun draw() {
        if (holder.surface.isValid) {
//            Log.d("GAME", "Drawing frame")
            val canvas = holder.lockCanvas()
            canvas.drawColor(Color.BLACK)

            paint.color = Color.WHITE
            canvas.drawRect(paddle.rect, paint)

            paint.color = Color.RED
            canvas.drawOval(ball.rect, paint)

            paint.color = Color.BLUE
            blocks.forEach { canvas.drawRect(it.rect, paint) }

            canvas.drawText("Wynik: $score", 50f, 80f, textPaint)
            canvas.drawText("Poziom: $level", width - 300f, 80f, textPaint)

            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> paddle.moveTo(event.x)
        }
        return true
    }

    fun pause() {
        playing = false
        try {
            if (thread.isAlive)
                thread.join()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun resume() {
        playing = true
        if (!thread.isAlive) {
            thread = Thread(this)
            thread.start()
        }
    }

    private fun resetGame() {
        score = 0
        level = 1
        resetBlocks()
        ball.reset()
    }

}
