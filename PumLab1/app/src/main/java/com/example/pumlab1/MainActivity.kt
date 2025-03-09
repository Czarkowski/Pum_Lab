package com.example.pumlab1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.pumlab1.ui.theme.PumLab1Theme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Ustawienie widoku z XML

        val editTextCelsius = findViewById<EditText>(R.id.editTextCelsius)
        val buttonConvert = findViewById<Button>(R.id.buttonConvert)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)

        buttonConvert.setOnClickListener {
            val celsiusText = editTextCelsius.text.toString()
            if (celsiusText.isNotEmpty()) {
                val celsius = celsiusText.toDoubleOrNull()
                if (celsius != null) {
                    val fahrenheit = (celsius * 9 / 5) + 32
                    textViewResult.text = "Wynik: %.2f °F".format(fahrenheit)
                } else {
                    textViewResult.text = "Niepoprawna wartość!"
                }
            } else {
                textViewResult.text = "Podaj wartość!"
            }
        }
    }
}
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            PumLab1Theme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    PumLab1Theme {
//        Greeting("Android")
//    }
//}