package pl.polsl.sensordatacollector.preferences

import android.content.Context

class ApplicationPreferences(context: Context) {
    var profile: Int
    var databaseAddress: String
    var databaseLogin: String
    var databasePassword: String
    var databaseName: String
    var firstName: String
    var lastName: String

    private val _preferencesName = "SensorDataCollector.Settings"
    private val _preferences = context.getSharedPreferences(_preferencesName, Context.MODE_PRIVATE)

    init {
        profile = _preferences.getInt("profile", 0)
        databaseAddress = _preferences.getString("${profile}_databaseAddress", "").toString()
        databaseLogin = _preferences.getString("${profile}_databaseLogin", "").toString()
        databasePassword = _preferences.getString("${profile}_databasePassword", "").toString()
        databaseName = _preferences.getString("${profile}_databaseName", "").toString()
        firstName = _preferences.getString("${profile}_firstName", "").toString()
        lastName = _preferences.getString("${profile}_lastName", "").toString()
    }

    fun save() {
        val editor = _preferences.edit()
        editor.putString("${profile}_databaseAddress", databaseAddress)
        editor.putString("${profile}_databaseLogin", databaseLogin)
        editor.putString("${profile}_databasePassword", databasePassword)
        editor.putString("${profile}_databaseName", databaseName)
        editor.putString("${profile}_firstName", firstName)
        editor.putString("${profile}_lastName", lastName)
        editor.putInt("profile", profile)
        editor.apply()
    }
}