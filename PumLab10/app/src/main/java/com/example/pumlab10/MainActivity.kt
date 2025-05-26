package com.example.pumlab10

import android.R.attr.left
import android.R.attr.right
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var spritesheetIdle: Bitmap
    private lateinit var spritesheetLeft: Bitmap
    private lateinit var spritesheetRight: Bitmap

    private enum class Direction { IDLE, LEFT, RIGHT }

    private var currentDirection = Direction.IDLE
    private var frameIndex = 0
    private val frameCount = 7
    private val frameWidth = 32
    private val frameHeight = 32

    private lateinit var rockfordImageView: ImageView


    private lateinit var gridLayout: GridLayout
    private val numRows = 10
    private val numCols = 10
    private lateinit var map: Array<CharArray>
    private var rockfordX = 1
    private var rockfordY = 1
    private lateinit var spritesheet: Bitmap
    private lateinit var rockford: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.boardGridLayout)
        gridLayout.rowCount = numRows
        gridLayout.columnCount = numCols

        // Inicjalizacja rockfordImageView:
        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        rockfordImageView = ImageView(this).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = screenWidth / numCols  // jeszcze możesz to poprawić, np. po layout pass
                height = screenWidth / numCols
            }
//            scaleType = ImageView.ScaleType.CENTER
        }

//        gridLayout.addView(rockfordImageView) // Lub ustaw na odpowiedniej pozycji planszy
        animationHandler.post(animationRunnable)

        initMap()
        loadSprites()
        drawMap()

        findViewById<Button>(R.id.btnUp).setOnClickListener { moveRockford(0, -1) }
        findViewById<Button>(R.id.btnDown).setOnClickListener { moveRockford(0, 1) }
        findViewById<Button>(R.id.btnLeft).setOnClickListener { moveRockford(-1, 0) }
        findViewById<Button>(R.id.btnRight).setOnClickListener { moveRockford(1, 0) }
    }

    private fun initMap() {
        map = arrayOf(
            "##########".toCharArray(),
            "#R.......#".toCharArray(),
            "#........#".toCharArray(),
            "#....*...#".toCharArray(),
            "#........#".toCharArray(),
            "#....O...#".toCharArray(),
            "#........#".toCharArray(),
            "#..*.....#".toCharArray(),
            "#........#".toCharArray(),
            "##########".toCharArray()
        )
    }

//    private fun loadSprites() {
//        val options = BitmapFactory.Options().apply {
//            inScaled = false
//        }
//        spritesheet = BitmapFactory.decodeResource(resources, R.drawable.rockford_stand_32x32, options)
//        rockford = Bitmap.createBitmap(spritesheet, 0, 0, 32, 32)
//    }
    private fun loadSprites() {
        val options = BitmapFactory.Options().apply { inScaled = false }
        spritesheetIdle = BitmapFactory.decodeResource(resources, R.drawable.rockford_idle_32x32, options)
        spritesheetLeft = BitmapFactory.decodeResource(resources, R.drawable.rockford_left_32x32, options)
        spritesheetRight = BitmapFactory.decodeResource(resources, R.drawable.rockford_right_32x32, options)
    }


    private fun drawMap() {
        gridLayout.removeAllViews()
        val displayMetrics = Resources.getSystem().displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        val tileWidth = screenWidth / numCols
        val tileHeight = screenHeight / numRows

//        val params = GridLayout.LayoutParams(
//            GridLayout.spec(rockfordY, 1),
//            GridLayout.spec(rockfordX, 1)
//        ).apply {
//            width = tileWidth
//            height = tileHeight
//        }
//        rockfordImageView.layoutParams = params


        for (i in 0 until numRows) {
            for (j in 0 until numCols) {
                if (map[i][j] == 'R' )
                {
                    gridLayout.addView(rockfordImageView)
                    continue
                }
                val imageView = ImageView(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = tileWidth
                        height = tileWidth
                    }
                }

                when (map[i][j]) {
                    '#' -> imageView.setImageResource(R.drawable.wall_32x32)
                    '.' -> imageView.setImageResource(R.drawable.ground_32x32)
                    '*' -> imageView.setImageResource(R.drawable.diamond_32x32)
                    'O' -> imageView.setImageResource(R.drawable.stone_32x32)
                    ' ' -> imageView.setImageResource(R.drawable.empty_32x32)
//                    'R' -> {
//
//                        continue
//                        imageView.setImageBitmap(rockford)
//                        imageView.scaleType = ImageView.ScaleType.CENTER;
//                    }
                }

                gridLayout.addView(imageView)
            }
        }

//        if (!::rockfordImageView.isInitialized) {
//            rockfordImageView = ImageView(this).apply {
//                scaleType = ImageView.ScaleType.CENTER
//            }
//        }
//
//        rockfordImageView.layoutParams = GridLayout.LayoutParams().apply {
//            width = tileWidth
//            height = tileHeight
//            rowSpec = GridLayout.spec(rockfordY, 1)
//            columnSpec = GridLayout.spec(rockfordX, 1)
//        }
//        rockfordImageView.setImageBitmap(rockford)
//
//        gridLayout.addView(rockfordImageView)
    }

    private fun moveRockford(dx: Int, dy: Int) {
        val newX = rockfordX + dx
        val newY = rockfordY + dy

        if (map[newY][newX] != '#') {
            // przesuń Rockforda
            map[rockfordY][rockfordX] = ' ' // stare miejsce
            map[newY][newX] = 'R'           // nowe miejsce
            rockfordX = newX
            rockfordY = newY
            drawMap()
        }

        if (dx < 0) currentDirection = Direction.LEFT
        else if (dx > 0) currentDirection = Direction.RIGHT
        else currentDirection = Direction.IDLE
        frameIndex = 0

    }

    private fun getFrameBitmap(direction: Direction, frame: Int): Bitmap {
        val spritesheet = when(direction) {
            Direction.IDLE -> spritesheetIdle
            Direction.LEFT -> spritesheetLeft
            Direction.RIGHT -> spritesheetRight
        }
        return Bitmap.createBitmap(spritesheet, frame * frameWidth, 0, frameWidth, frameHeight)
    }

    private val animationHandler = android.os.Handler()
    private val animationRunnable = object : Runnable {
        override fun run() {
            Log.d("TAG", frameIndex.toString())
            val frameBitmap = getFrameBitmap(currentDirection, frameIndex)
            rockfordImageView.setImageBitmap(frameBitmap)
            frameIndex = (frameIndex + 1) % frameCount
            animationHandler.postDelayed(this, 100) // co 100ms
        }
    }



//    private fun startAnimation() {
//        val animationRunnable: Runnable = object : Runnable {
//            override fun run() {
//                //moveRockford() // aktualizuj pozycję Rockforda
//
//                val currentSpritesheet: Bitmap
//                when (rockfordDo) {
//                    idle -> currentSpritesheet = spritesheet_rockford_idle
//                    left -> currentSpritesheet = spritesheet_rockford_left
//                    right -> currentSpritesheet = spritesheet_rockford_right
//                    stand -> currentSpritesheet = spritesheet_rockford_stand
//                    else -> currentSpritesheet = spritesheet_rockford_stand
//                }
//
//                val x: Int = frameIndex * frameWidth
//                val frame = Bitmap.createBitmap(
//                    currentSpritesheet,
//                    x,
//                    0,
//                    frameWidth,
//                    currentSpritesheet.height
//                )
//
//                imageView.setImageBitmap(frame)
//
//                frameIndex = (frameIndex + 1) % frameCount
//
//                idleTimer++
//                if (!movingUp && !movingDown && !movingLeft && !movingRight && idleTimer > idletime) {
//                    rockfordDo = RockfordDo.idle
//                }
//
//                handler.postDelayed(this, 100) // animacja co 100ms
//            }
//        }
//
//        handler.post(animationRunnable)
//    }
}