package pl.polsl.sensordatacollector.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class SensorsListener(private val sensorManager: SensorManager) : SensorEventListener {
    private val _sensorDelay = SensorManager.SENSOR_DELAY_NORMAL
    private val _sensorTypes = listOf(Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_LIGHT)
    private val _data: MutableMap<Int, MutableList<FloatArray>> = mutableMapOf()
    fun start() {
        for (sensorType in _sensorTypes) {
            val sensor = sensorManager.getDefaultSensor(sensorType)
            sensorManager.registerListener(this, sensor, _sensorDelay)
        }
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    fun getValues(sensorType: Int): List<FloatArray>? {
        return _data[sensorType]
    }

    fun clearValues() {
        _data.clear()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            appendValues(event.values, event.sensor.type)
            Log.d("SensorData",  "${event.sensor.type}: ${event.values.joinToString(separator = ", ")}")
        }
    }

    private fun appendValues(values: FloatArray, sensorType: Int) {
        if (!_data.containsKey(sensorType)) {
            _data[sensorType] = mutableListOf(values)
        }
        else {
            _data[sensorType]!!.add(values)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}