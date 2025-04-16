package com.example.pumlab5.model

import android.content.Context
import android.widget.GridLayout
import com.example.pumlab5.utils.SoundManager
import java.util.Collections
import kotlin.math.abs

class GameBoard(
    private val context: Context,
    private val gridLayout: GridLayout,
    private val onMove: () -> Unit,
    private val onWin: () -> Unit
) {
    private val tiles = mutableListOf<Tile>()

    fun shuffle() {
        tiles.clear()

        // Utwórz poprawnie ułożoną planszę: 1..15 i jedno puste pole (null)
        val numbers :MutableList<Int?> = (1..15).map { it }.toMutableList()
        numbers.add(null)

        numbers.forEach { number ->
            val tile = Tile(context, number)
            tile.setOnClickListener { tryMove(tile) }
            tiles.add(tile)
        }

        // Wykonaj 1000 losowych, poprawnych ruchów
        repeat(1000) { performRandomMove() }

        updateGrid()
        SoundManager.playShuffle(context)
    }

    private fun performRandomMove() {
        val emptyIndex = tiles.indexOfFirst { it.number == null }
        val row = emptyIndex / 4
        val col = emptyIndex % 4

        val possibleMoves = listOfNotNull(
            if (row > 0) emptyIndex - 4 else null,     // góra
            if (row < 3) emptyIndex + 4 else null,     // dół
            if (col > 0) emptyIndex - 1 else null,     // lewo
            if (col < 3) emptyIndex + 1 else null      // prawo
        )

        val moveIndex = possibleMoves.random()
        Collections.swap(tiles, emptyIndex, moveIndex)
    }


    private fun tryMove(tile: Tile) {
        val index = tiles.indexOf(tile)
        val emptyIndex = tiles.indexOfFirst { it.number == null }
        if (canSwap(index, emptyIndex)) {
            Collections.swap(tiles, index, emptyIndex)
            updateGrid()
            onMove()
            if (isWinning()) onWin()
        }
    }

    private fun canSwap(a: Int, b: Int): Boolean {
        val rowA = a / 4; val colA = a % 4
        val rowB = b / 4; val colB = b % 4
        return (rowA == rowB && abs(colA - colB) == 1) || (colA == colB && abs(rowA - rowB) == 1)
    }

    private fun isWinning(): Boolean {
        return tiles.dropLast(1).withIndex().all { it.value.number == it.index + 1 }
    }

    private fun updateGrid() {
        gridLayout.removeAllViews()
        tiles.forEach { gridLayout.addView(it) }
    }

    fun setTestWinState() {
        tiles.clear()
        val numbers = (1..14).toMutableList() + listOf(null, 15) // tylko jeden ruch do wygranej
        numbers.forEach { number ->
            val tile = Tile(context, number)
            tile.setOnClickListener { tryMove(tile) }
            tiles.add(tile)
        }
        updateGrid()
    }
}
