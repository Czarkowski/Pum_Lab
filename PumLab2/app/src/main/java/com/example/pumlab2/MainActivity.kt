package com.example.pumlab2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.util.Log
import android.content.Context

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity
    private val PREFS_NAME = "counterPrefs"  // Nazwa pliku z preferencjami
    private val COUNTER_KEY = "clickCounter" // Klucz, pod którym zapisujemy licznik

    // Zmienna przechowująca stan licznika
    private var clickCount = 0

    // Zapisywanie stanu licznika przy zmianie orientacji lub zamknięciu aplikacji
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(COUNTER_KEY, clickCount)
        logAndAppend("onSaveInstanceState called")
    }

    // Przywracanie stanu licznika po zmianie orientacji lub ponownym otwarciu aplikacji
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        clickCount = savedInstanceState.getInt(COUNTER_KEY, 0)
        logAndAppend("onRestoreInstanceState called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logAndAppend("onCreate called")
        setContentView(R.layout.activity_main)
        // Przywrócenie stanu licznika po przywróceniu aktywności
        if (savedInstanceState != null) {
            clickCount = savedInstanceState.getInt(COUNTER_KEY, 0)
        }
        else{ // Przywrócenie licznika z zapisanych preferencji po zamknięciu aplikacji
            val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            clickCount = sharedPreferences.getInt(COUNTER_KEY, 0) // Domyślnie 0, jeśli brak danych
            logAndAppend("sharedPreferences readed")
        }

        val textViewCounter: TextView = findViewById(R.id.textViewCounter)
        val buttonClick: Button = findViewById(R.id.buttonClick)

        // Ustawienie początkowej wartości licznika
        textViewCounter.text = clickCount.toString()

        // Obsługa kliknięcia przycisku
        buttonClick.setOnClickListener {
            clickCount++  // Zwiększ liczbę kliknięć
            textViewCounter.text = clickCount.toString()  // Zaktualizuj widok
        }
    }

    override fun onStart() {
        super.onStart()
        logAndAppend("onStart called")
    }

    override fun onResume() {
        super.onResume()
        logAndAppend("onResume called")
    }

    override fun onPause() {
        super.onPause()
        logAndAppend("onPause called")
    }

    override fun onStop() {
        super.onStop()
        logAndAppend("onStop called")
    }

    override fun onRestart() {
        super.onRestart()
        logAndAppend("onRestart called")
    }

    override fun onDestroy() {
        super.onDestroy()
        logAndAppend("onDestroy called")

        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt(COUNTER_KEY, clickCount)  // Zapisz stan licznika
            apply()
        }
    }

    private fun logAndAppend(message: String) {
        Log.d(TAG, message)
    }
}
