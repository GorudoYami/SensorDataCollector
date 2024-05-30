package pl.polsl.sensordatacollector.preferences

import android.content.Context
import android.util.Log

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
        Log.d(this::class.simpleName, "Loading profile $profile")
        databaseAddress = _preferences.getString("${profile}_databaseAddress", "").toString().trim()
        databaseLogin = _preferences.getString("${profile}_databaseLogin", "").toString().trim()
        databasePassword = _preferences.getString("${profile}_databasePassword", "").toString()
        databaseName = _preferences.getString("${profile}_databaseName", "").toString().trim()
        firstName = _preferences.getString("${profile}_firstName", "").toString().trim()
        lastName = _preferences.getString("${profile}_lastName", "").toString().trim()
    }

    fun save() {
        Log.d(this::class.simpleName, "Saving profile $profile")
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