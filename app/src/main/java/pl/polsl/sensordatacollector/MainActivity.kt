package pl.polsl.sensordatacollector

import android.os.Bundle
import android.widget.Button
import android.graphics.Color
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import pl.polsl.sensordatacollector.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var x = 1;
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val button: Button = findViewById(R.id.button);
        button.setBackgroundColor(Color.parseColor("#00FF00"));
        button.setOnClickListener{
            if (x == 0){
                button.setBackgroundColor(Color.parseColor("#00FF00"));
                button.setText("START");
                x++;
            }
            else {
                button.setBackgroundColor(Color.parseColor("#FF0000"));
                button.setText("STOP");
                x--;
            }
        }


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_settings, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }



}

