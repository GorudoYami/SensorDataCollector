package pl.polsl.sensordatacollector.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Database(address: String, databaseName: String, user: String, password: String) {
    private val _connectionString: String = "jdbc:mysql://$address"
    private val _databaseName: String = databaseName
    private val _user: String = user
    private val _password: String = password
    private var _connection: Connection? = null

    @Throws(SQLException::class)
    suspend fun connect() {
        withContext(Dispatchers.IO) {
            createConnection()
        }
    }

    private fun doesDatabaseExist(): Boolean {
        return false
    }

    private fun createDatabase() {

    }

    @Throws(SQLException::class)
    suspend fun executeQuery(query: String) {
        withContext(Dispatchers.IO) {

        }
    }

    @Throws(SQLException::class)
    suspend fun executeScalar(query: String) {
        withContext(Dispatchers.IO) {

        }
    }

    @Throws(SQLException::class)
    fun createConnection() {
        if (_connection?.isClosed == false) {
            return
        }

        _connection = DriverManager.getConnection(_connectionString, _user, _password)
        if (!doesDatabaseExist()) {
            createDatabase()
        }
        _connection?.close()
        _connection = DriverManager.getConnection("$_connectionString/$_databaseName", _user, _password)
    }
}