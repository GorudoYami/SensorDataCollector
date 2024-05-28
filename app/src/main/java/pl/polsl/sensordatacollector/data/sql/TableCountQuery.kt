package pl.polsl.sensordatacollector.data.sql

import android.util.Log

class TableCountQuery {
    fun getSql(): String {
        val sql = """
            SELECT COUNT(*) as TableCount
            FROM `information_schema`.`tables`
            WHERE `table_schema` = DATABASE()
        """.trimIndent()
        Log.d("SQL", sql)
        return sql
    }
}