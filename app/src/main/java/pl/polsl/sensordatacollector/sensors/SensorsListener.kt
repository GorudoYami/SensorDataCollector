package pl.polsl.sensordatacollector.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class SensorsListener : SensorEventListener {
    private val _data: MutableMap<Int, MutableList<FloatArray>> = mutableMapOf()

    fun getValues(sensorType: Int): List<FloatArray>? {
        return _data[sensorType]
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            appendValues(event.values, event.sensor.type)
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