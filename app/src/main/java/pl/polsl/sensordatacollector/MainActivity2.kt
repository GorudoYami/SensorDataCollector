package pl.polsl.sensordatacollector

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val editTextDatabaseAddress: EditText = findViewById(R.id.editTextDatabaseAddress)
        val editTextDatabaseName: EditText = findViewById(R.id.editTextDatabaseName)
        val editTextLogin: EditText = findViewById(R.id.editTextLogin)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)

        val textDatabaseAddress: String = editTextDatabaseAddress.text.toString()
        val textDatabaseName: String = editTextDatabaseName.text.toString()
        val textLogin: String = editTextLogin.text.toString()
        val textPassword: String = editTextPassword.text.toString()

        // Ustawianie wartości pola tekstowego
        editTextDatabaseAddress.setText("Adres bazy")
        editTextDatabaseName.setText("Nazwa bazy")
        editTextLogin.setText("Login")
        editTextPassword.setText("Hasło")

        val spinner: Spinner = findViewById(R.id.spinnerGroup)

        val options = arrayOf("Group 1", "Group 2", "Group 3")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        // Obsługa wyboru elementu z listy rozwijanej
        //spinner.setOnItemSelectedListener { , , ,  ->
        //}
    }
}