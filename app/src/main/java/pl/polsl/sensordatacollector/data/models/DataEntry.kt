package pl.polsl.sensordatacollector.data.models

data class DataEntry(
    val id: Int,
    val sensorId: Int,
    val timestamp: Long,
    val index: Int,
    val value: Float,
    val userId: Int)
