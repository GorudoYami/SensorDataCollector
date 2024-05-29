package pl.polsl.sensordatacollector.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.IBinder
import android.provider.ContactsContract.Data
import pl.polsl.sensordatacollector.data.Database
import pl.polsl.sensordatacollector.data.models.DataEntry
import pl.polsl.sensordatacollector.sensors.SensorsListener
import kotlin.concurrent.Volatile
import kotlinx.coroutines.*
import pl.polsl.sensordatacollector.preferences.ApplicationPreferences
import java.sql.SQLException

class DataCollectingService : Service() {
    private val _settingsName = "SensorDataCollector.Settings"

    @Volatile private var _run = false
    private lateinit var _sensorsListener: SensorsListener
    private lateinit var _database: Database

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initialize()

        GlobalScope.launch(Dispatchers.IO) {
            while (_run) {
                try {
                    val dataEntries = _sensorsListener.getValues().map { sensorValue ->
                        var i = 0
                        sensorValue.values.map { value ->
                            DataEntry(
                                0,
                                sensorValue.sensorId,
                                1,
                                sensorValue.timestamp,
                                i++,
                                value,
                                1
                            )
                        }
                    }.flatten()

                    runBlocking {
                        _database.insertDataEntries(dataEntries)
                    }

                    _sensorsListener.clearValues()
                    Thread.sleep(5000)
                } catch (ex: SQLException) {
                    ex.printStackTrace()
                } catch (ex: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }

        return START_STICKY
    }

    private fun initialize() {
        initializeDatabase()
        initializeSensorsListener()
        _run = true
    }

    private fun initializeDatabase() {
        val applicationPreferences = ApplicationPreferences(this)
        _database = Database(
            applicationPreferences.databaseAddress,
            applicationPreferences.databaseName,
            applicationPreferences.databaseLogin,
            applicationPreferences.databasePassword
        )
    }

    private fun initializeSensorsListener() {
        _sensorsListener = SensorsListener(getSystemService(SENSOR_SERVICE) as SensorManager)
        _sensorsListener.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _run = false
        _sensorsListener.stop()
    }
}