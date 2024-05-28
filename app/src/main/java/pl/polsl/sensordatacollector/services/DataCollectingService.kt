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
import java.sql.SQLException

class DataCollectingService : Service() {
    @Volatile private var run = false
    private lateinit var sensorsListener: SensorsListener
    private var database = Database("redacted", "SensorDataCollector", "redacted", "redacted")
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sensorsListener = SensorsListener(getSystemService(SENSOR_SERVICE) as SensorManager)
        sensorsListener.start()

        run = true

        GlobalScope.launch(Dispatchers.IO) {
            while (run) {
                try {
                    val dataEntries = sensorsListener.getValues().map { sensorValue ->
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
                        database.insertDataEntries(dataEntries)
                    }

                    sensorsListener.clearValues()
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

    override fun onDestroy() {
        super.onDestroy()
        run = false
        sensorsListener.stop()
    }
}