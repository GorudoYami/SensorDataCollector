package pl.polsl.sensordatacollector.services

import android.app.Service
import android.content.Intent
import android.hardware.SensorManager
import android.os.IBinder
import pl.polsl.sensordatacollector.data.Database
import pl.polsl.sensordatacollector.data.models.DataEntry
import pl.polsl.sensordatacollector.sensors.SensorsListener
import kotlin.concurrent.Volatile
import kotlinx.coroutines.*
import pl.polsl.sensordatacollector.preferences.ApplicationPreferences
import java.lang.Exception
import java.sql.SQLException

class DataCollectingService : Service() {
    @Volatile private var _run = false
    private lateinit var _sensorsListener: SensorsListener
    private lateinit var _database: Database
    private var _userId: Int = 0

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val applicationPreferences = ApplicationPreferences(this)
        initialize(applicationPreferences)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                _userId =
                    if (!_database.userExists(
                            applicationPreferences.firstName,
                            applicationPreferences.lastName
                        )
                    ) {
                        _database.insertUser(
                            applicationPreferences.firstName,
                            applicationPreferences.lastName
                        )
                    } else {
                        _database.getUser(
                            applicationPreferences.firstName,
                            applicationPreferences.lastName
                        )!!
                    }
            }
            catch(ex: Exception) {
                ex.printStackTrace()
            }

            while (_run) {
                try {
                    val dataEntries = _sensorsListener.getValues().map { sensorValue ->
                        var i = 0
                        sensorValue.values.map { value ->
                            DataEntry(
                                0,
                                sensorValue.sensorId,
                                sensorValue.timestamp,
                                i++,
                                value,
                                _userId
                            )
                        }
                    }.flatten()

                    _database.insertDataEntries(dataEntries)
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

    private fun initialize(applicationPreferences: ApplicationPreferences) {
        initializeDatabase(applicationPreferences)
        initializeSensorsListener()
        _run = true
    }

    private fun initializeDatabase(applicationPreferences: ApplicationPreferences) {
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