package com.example.pumlab7

data class Card(val id: Int, val value: Int, var isFaceUp: Boolean = false, var isMatched: Boolean = false)

class MemoryGame(private val pairs: Int) {
    val cards: List<Card>
    private var indexOfSingleSelectedCard: Int? = null

    init {
        val values = (1..pairs).flatMap { listOf(it, it) }.shuffled()
        cards = values.mapIndexed { i, v -> Card(i, v) }
    }

    fun flipCard(position: Int) {
        val card = cards[position]
        if (card.isMatched || card.isFaceUp) return

        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            card.isFaceUp = true
            indexOfSingleSelectedCard = position
        } else {
            val matchedCard = cards[indexOfSingleSelectedCard!!]
            if (matchedCard.value == card.value) {
                matchedCard.isMatched = true
                card.isMatched = true
            }
            card.isFaceUp = true
            indexOfSingleSelectedCard = null
        }
    }

    fun restoreCards() {
        cards.filter { !it.isMatched }.forEach { it.isFaceUp = false }
    }

    fun hasWon(): Boolean = cards.all { it.isMatched }
}
