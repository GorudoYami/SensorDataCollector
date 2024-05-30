package pl.polsl.sensordatacollector.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import pl.polsl.sensordatacollector.data.models.SensorDataEntry
import java.time.Instant

class SensorsListener(private val sensorManager: SensorManager) : SensorEventListener {
    private val _sensorDelay = SensorManager.SENSOR_DELAY_NORMAL
    private val _sensorTypes = listOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_LIGHT)
    private val _data: MutableList<SensorDataEntry> = mutableListOf()
    fun start() {
        for (sensorType in _sensorTypes) {
            val sensor = sensorManager.getDefaultSensor(sensorType)
            sensorManager.registerListener(this, sensor, _sensorDelay)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    @Synchronized
    fun getValues(): List<SensorDataEntry> {
        return _data
    }

    @Synchronized
    fun clearValues() {
        _data.clear()
    }

    @Synchronized
    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            _data.add(SensorDataEntry(event.sensor.type, Instant.now().toEpochMilli() / 1000, event.values))
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}