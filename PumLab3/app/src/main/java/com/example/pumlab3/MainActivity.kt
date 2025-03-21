package com.example.pumlab3


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var wordTextView: TextView
    private lateinit var triesTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var letterInput: EditText
    private lateinit var checkButton: Button
    private lateinit var resetButton: Button

    private val TAG = "MainActivity"
    private var wordToGuess = ""
    private var guessedWord = charArrayOf()
    private var triesLeft = 6
    private val guessedLetters = mutableSetOf<Char>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wordTextView = findViewById(R.id.wordTextView)
        triesTextView = findViewById(R.id.triesTextView)
        statusTextView = findViewById(R.id.statusTextView)
        letterInput = findViewById(R.id.letterInput)
        checkButton = findViewById(R.id.checkButton)
        resetButton = findViewById(R.id.resetButton)

        resetGame()

        checkButton.setOnClickListener { checkLetter() }
        resetButton.setOnClickListener { resetGame() }
    }

    private fun resetGame() {
        wordToGuess = WORDS[Random.nextInt(WORDS.size)]
        guessedWord = CharArray(wordToGuess.length) { '_' }
        guessedLetters.clear()
        updateGuessedWord(' ')
        triesLeft = 6
        checkButton.isEnabled = true
        statusTextView.text = ""
        Log.d(TAG, wordToGuess)
        updateWordDisplay()
    }

    private fun updateWordDisplay() {
        val displayedWord = wordToGuess.mapIndexed { index, char ->
            if (char.lowercaseChar() in guessedLetters) char else '_'
        }.joinToString(" ")

        wordTextView.text = displayedWord
        triesTextView.text = "Pozostałe próby: $triesLeft"
    }
    private fun updateGuessedWord(letter: Char) {
        if (!guessedLetters.contains(letter))
        {
            guessedLetters.add(letter)
        }
        if (wordToGuess.lowercase().contains(letter)) {
            for (i in wordToGuess.indices) {
                if (wordToGuess[i] == letter) {
                    guessedWord[i] = letter
                }
            }
        } else {
            triesLeft--
        }
    }

    private fun checkLetter() {
        val input = letterInput.text.toString()
        if (input.isEmpty() || input.length > 1) {
            Toast.makeText(this, "Podaj jedną literę!", Toast.LENGTH_SHORT).show()
            return
        }

        val letter = input[0].lowercaseChar()
        letterInput.text.clear()

        if (letter in guessedLetters) {
            Toast.makeText(this, "Ta litera była już zgadywana!", Toast.LENGTH_SHORT).show()
            return
        }

        updateGuessedWord(letter)
        updateWordDisplay()
        checkGameStatus()
    }

    private fun checkGameStatus() {
        if (String(guessedWord) == wordToGuess.lowercase()) {
            statusTextView.text = "Gratulacje! Odgadłeś słowo!"
            checkButton.isEnabled = false
        } else if (triesLeft == 0) {
            statusTextView.text = "Przegrałeś! Słowo to: $wordToGuess"
            checkButton.isEnabled = false
        }
    }

    private val WORDS = listOf(
        "komputer", "programowanie", "system", "algorytm", "aplikacja", "internet", "dane", "kod", "sieć", "web",
        "serwer", "framework", "grafika", "programista", "strona", "dysk", "RAM", "CPU", "GPU", "baza danych",
        "linux", "Windows", "macOS", "sztuczna inteligencja", "machine learning", "cyberbezpieczeństwo", "komponent",
        "API", "git", "repository", "debugowanie", "skrypt", "hasło", "router", "firewall", "monitor", "cyfrowy",
        "cryptocurrency", "kodowanie", "architektura", "wersjonowanie", "debugowanie", "interfejs", "responsywność",
        "frontend", "backend", "hosting", "cyberatak", "synchronizacja", "dokumentacja", "proxy", "wirus", "malware",
        "VPN", "sterownik", "intranet", "siec neuronowa", "framework", "kryptografia", "algorithm", "program",
        "framework", "obiekt", "serwer", "komponent", "aplikacja mobilna", "rozwiązanie", "komunikacja", "testowanie",
        "konfiguracja", "edycja", "plugin", "archiwum", "algorytm", "adres IP", "usługa", "web design", "klient",
        "terminal", "zaszyfrowany", "deklaracja", "monitoring", "renderowanie", "API key", "kompilator", "hardware",
        "serwer plików", "debugowanie", "rozpoznawanie", "zarządzanie danymi", "zintegrowany", "data mining",
        "sieć społecznościowa", "ciasteczko", "wywołanie", "zabezpieczenie", "operating system", "usługa chmurowa",
        "framework", "transmisja", "responsive design", "open-source", "hosting", "platforma", "komunikat", "serializacja",
        "aplikacja webowa", "cache", "testing", "storage", "asynchroniczny", "rozwiązanie", "algorytmiczny", "rozpoznawanie",
        "klucz API", "synchronizacja", "platforma cyfrowa", "zaszyfrowany", "synchronizacja", "open-source", "profil",
        "cache", "komunikacja", "algorithm", "kompilator", "zabezpieczenie"
        )
    }