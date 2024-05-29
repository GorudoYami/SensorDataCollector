package pl.polsl.sensordatacollector.data.sql

import android.util.Log

class InsertUserCommand {
    fun getSql(firstName: String, lastName: String): String {
        val sql = "INSERT INTO `User` (`first_name`, `last_name`) VALUES ('${firstName}', '${lastName}')"
        Log.d("SQL", sql)
        return sql
    }
}