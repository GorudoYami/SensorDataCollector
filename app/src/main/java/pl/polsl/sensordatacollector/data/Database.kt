package pl.polsl.sensordatacollector.data

import android.util.Log
import pl.polsl.sensordatacollector.data.sql.CreateTablesDDL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.polsl.sensordatacollector.data.models.DataEntry
import pl.polsl.sensordatacollector.data.sql.GetUserQuery
import pl.polsl.sensordatacollector.data.sql.InsertDataCommand
import pl.polsl.sensordatacollector.data.sql.InsertUserCommand
import pl.polsl.sensordatacollector.data.sql.TableCountQuery
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Database(address: String, databaseName: String, user: String, password: String) {
    private val _connectionString: String = "jdbc:mysql://$address/$databaseName"
    private val _user: String = user
    private val _password: String = password

    @Throws(SQLException::class)
    suspend fun checkConnection() {
        withContext(Dispatchers.IO) {
            getConnection().use {  }
        }
    }

    private fun getConnection(): Connection {
        Log.d("Database", "Connecting to $_connectionString as $_user")
        val connection = DriverManager.getConnection(_connectionString, _user, _password)
        if (!tablesExist(connection)) {
            createTables(connection)
        }

        return connection
    }

    private fun tablesExist(connection: Connection): Boolean {
        connection.createStatement().use { statement ->
            statement.executeQuery(TableCountQuery().getSql()).use { resultSet ->
                if (resultSet.next()) {
                    val tableCount = resultSet.getInt("TableCount")
                    return tableCount > 0
                }
                else {
                    throw Exception("Unexpected result")
                }
            }
        }
    }

    private fun createTables(connection: Connection) {
        connection.createStatement().use { statement ->
            CreateTablesDDL().getSql().forEach { batch -> statement.addBatch(batch) }
            statement.executeBatch()
        }
    }

    suspend fun insertDataEntries(dataEntries: Collection<DataEntry>) {
        withContext(Dispatchers.IO) {
            getConnection().use { connection ->
                connection.createStatement().use { statement ->
                    statement.execute(InsertDataCommand().getSql(dataEntries))
                }
            }
        }
    }

    suspend fun getUser(firstName: String, lastName: String): Int? {
        return withContext(Dispatchers.IO) {
            getConnection().use { connection ->
                connection.createStatement().use { statement ->
                    statement.executeQuery(GetUserQuery().getSql(firstName, lastName)).use { resultSet ->
                        if (resultSet.next()) resultSet.getInt("id") else null
                    }
                }
            }
        }
    }

    suspend fun insertUser(firstName: String, lastName: String): Int {
        withContext(Dispatchers.IO) {
            getConnection().use { connection ->
                connection.createStatement().use { statement ->
                    statement.execute(InsertUserCommand().getSql(firstName, lastName))
                }
            }
        }

        return getUser(firstName, lastName)!!
    }

    suspend fun userExists(firstName: String, lastName: String): Boolean {
        return getUser(firstName, lastName) != null
    }
}