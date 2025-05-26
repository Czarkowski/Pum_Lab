package com.example.pumlab9

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
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
import com.example.pumlab9.ui.theme.PumLab9Theme

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private val selectedSensors = mutableListOf<Sensor>()
    private val sensorData = mutableMapOf<Int, MutableList<Entry>>()
    private val charts = mutableMapOf<Int, LineChart>()
    private var threshold: Float = 0f
    private var interval: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Przykład: wybór sensorów (zastąp rzeczywistym UI)
        val sensorList = listOf(
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        )
        selectedSensors.addAll(sensorList)

        threshold = 10f // przykładowa wartość
        interval = 1000 // 1 sekunda

        for (sensor in selectedSensors) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorData[sensor.type] = mutableListOf()
            setupChart(sensor.type)
        }
    }

    private fun setupChart(sensorType: Int) {
        val chart = LineChart(this)
        chart.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 600
        )
        findViewById<LinearLayout>(R.id.chartContainer).addView(chart)
        charts[sensorType] = chart
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        val value = event.values[0]
        val currentTime = System.currentTimeMillis().toFloat()

        val entries = sensorData[event.sensor.type] ?: return
        entries.add(Entry(currentTime, value))
        updateChart(event.sensor.type, entries)

        if (value > threshold) {
            Toast.makeText(this, "Alarm: ${event.sensor.name} > $threshold", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateChart(sensorType: Int, entries: List<Entry>) {
        val chart = charts[sensorType] ?: return
        val dataSet = LineDataSet(entries, "Sensor $sensorType")
        chart.data = LineData(dataSet)
        chart.invalidate()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    // Dodaj zapis do pliku
    private fun saveDataToFile() {
        val file = File(getExternalFilesDir(null), "sensor_data.csv")
        file.printWriter().use { out ->
            sensorData.forEach { (type, entries) ->
                entries.forEach { entry ->
                    out.println("$type,${entry.x},${entry.y}")
                }
            }
        }
    }
}