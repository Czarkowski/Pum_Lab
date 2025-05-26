package com.example.pumlab9_2

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.random.Random

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private val selectedSensors = mutableListOf<Sensor>()
    private val sensorData = mutableMapOf<Int, MutableList<Entry>>()
    private val charts = mutableMapOf<Int, LineChart>()
    private var threshold: Float = 0f
    private var interval: Long = 1000
    private var lastUpdateTimes = mutableMapOf<Int, Long>()
    private var startTime: Long = 0;


    private val simulatedSensorType = 9999  // sztuczny typ sensora dla symulacji
    private val simulatedEntries = mutableListOf<Entry>()
    private val random = Random(System.currentTimeMillis())
    private val handler = Handler(Looper.getMainLooper())
    private var simulatedXValue = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Przykład: wybór sensorów (zastąp rzeczywistym UI)
        val sensorList = listOfNotNull(
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
            sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        )
        selectedSensors.addAll(sensorList)

        threshold = 10f // przykładowa wartość
        interval = 1000 // 1 sekunda

        for (sensor in selectedSensors) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorData[sensor.type] = mutableListOf()
            setupChart(sensor.type)
            lastUpdateTimes[sensor.type] = 0
        }


//        sensorData[simulatedSensorType] = simulatedEntries
//        setupChart(simulatedSensorType)
//
//        startSimulatedSensorData()

        // Zapis danych przy zamknięciu aplikacji (np. przycisk w UI)
        findViewById<Button>(R.id.saveButton)?.setOnClickListener {
            saveDataToFile()
        }
        startTime = System.currentTimeMillis()
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
        val sensorType = event.sensor.type
        val currentTime = System.currentTimeMillis()

        val lastUpdateTime = lastUpdateTimes[sensorType] ?: 0
        if (currentTime - lastUpdateTime < interval) return
        lastUpdateTimes[sensorType] = currentTime

        val timeSeconds = (currentTime - startTime) / 1000f
        val entries = sensorData[sensorType] ?: return
        entries.add(Entry(timeSeconds, value))
        updateChart(sensorType, entries)

//        if (value > threshold) {
//            Toast.makeText(this, "Alarm: ${event.sensor.name} > $threshold", Toast.LENGTH_SHORT).show()
//            // Dźwiękowy alarm
//            MediaPlayer.create(this, R.raw.alarm)?.start()
//        }
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
        Toast.makeText(this, "Dane zapisane do pliku", Toast.LENGTH_SHORT).show()
    }

    private fun startSimulatedSensorData() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val value = random.nextInt(201) - 100 // losowe od -100 do 100
                val timeSeconds = simulatedXValue
                simulatedEntries.add(Entry(timeSeconds, value.toFloat()))
                updateChart(simulatedSensorType, simulatedEntries)
                simulatedXValue += 1f
                handler.postDelayed(this, interval)
            }
        }, interval)
    }
}