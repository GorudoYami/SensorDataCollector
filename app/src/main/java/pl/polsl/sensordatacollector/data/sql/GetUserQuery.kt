package pl.polsl.sensordatacollector.data.sql

import android.util.Log

class GetUserQuery {
    fun getSql(firstName: String?, lastName: String?): String {
        var sql = "SELECT * FROM `User` WHERE 1 = 1";
        if (firstName != null) {
            sql += " AND `first_name` LIKE '${firstName}'"
        }
        if (lastName != null) {
            sql += " AND `last_name` LIKE '${lastName}'"
        }
        Log.d("SQL", sql)
        return sql
    }
}