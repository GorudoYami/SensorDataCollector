package pl.polsl.sensordatacollector.data

import pl.polsl.sensordatacollector.data.sql.CreateTablesDDL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.polsl.sensordatacollector.data.models.DataEntry
import pl.polsl.sensordatacollector.data.sql.InsertDataCommand
import pl.polsl.sensordatacollector.data.sql.TableCountQuery
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Database(address: String, databaseName: String, user: String, password: String) {
    private val _connectionString: String = "jdbc:mysql://$address"
    private val _databaseName: String = databaseName
    private val _user: String = user
    private val _password: String = password

    @Throws(SQLException::class)
    suspend fun checkConnection() {
        withContext(Dispatchers.IO) {
            getConnection().use {  }
        }
    }

    private fun getConnection(): Connection {
        val connection = DriverManager.getConnection("$_connectionString/$_databaseName", _user, _password)
        if (!doTablesExist(connection)) {
            createTables(connection)
        }

        return connection
    }

    private fun doTablesExist(connection: Connection): Boolean {
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
}