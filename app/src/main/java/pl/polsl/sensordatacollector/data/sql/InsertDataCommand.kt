package pl.polsl.sensordatacollector.data.sql

import android.util.Log
import pl.polsl.sensordatacollector.data.models.DataEntry

class InsertDataCommand {
    fun getSql(dataEntries: Collection<DataEntry>): String {
        val values = dataEntries.stream().map { dataEntry ->
            "(${dataEntry.sensorId}, ${dataEntry.userId}, FROM_UNIXTIME(${dataEntry.timestamp}), ${dataEntry.index}, ${dataEntry.value})"
        }.toArray().joinToString(",")
        val sql = "INSERT INTO `Data` (`sensor_id`, `user_id`, `timestamp`, `index`, `value`) VALUES $values"
        Log.d("SQL", sql)
        return sql
    }
}