package com.example.pumlab5.utils

import android.content.Context
import android.media.MediaPlayer
import com.example.pumlab5.R

object SoundManager {
    fun playMove(context: Context) {
//        MediaPlayer.create(context, R.raw.move).start()
    }

    fun playShuffle(context: Context) {
//        MediaPlayer.create(context, R.raw.shuffle).start()
    }

    fun playWin(context: Context) {
        MediaPlayer.create(context, R.raw.wygrana).start()
    }
}
