package pl.polsl.sensordatacollector.data.models

data class SensorDataEntry(
    val sensorId: Int,
    val timestamp: Long,
    val values: FloatArray) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SensorDataEntry

        if (sensorId != other.sensorId) return false
        if (timestamp != other.timestamp) return false
        if (!values.contentEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sensorId
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + values.contentHashCode()
        return result
    }
}
