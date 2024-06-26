package pl.polsl.sensordatacollector.ui.home

import android.content.Intent
import android.graphics.Color
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.polsl.sensordatacollector.databinding.FragmentHomeBinding
import pl.polsl.sensordatacollector.sensors.SensorsListener
import pl.polsl.sensordatacollector.services.DataCollectingService

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var isListening = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val button: Button = binding.button
        button.setBackgroundColor(Color.parseColor("#00FF00"))
        button.setText("START")

        button.setOnClickListener {
            if (isListening) {
                button.setBackgroundColor(Color.parseColor("#00FF00"))
                button.setText("START")
                stopService()
            } else {
                button.setBackgroundColor(Color.parseColor("#FF0000"))
                button.setText("STOP")
                startService()
            }
            isListening = !isListening
        }

        return root
    }

    private fun startService() {
        Log.d("DataCollectingService", "Starting")
        val context = activity?.applicationContext
        context?.startService(Intent(context, DataCollectingService::class.java))
    }

    private fun stopService() {
        Log.d("DataCollectingService", "Stopping")
        val context = activity?.applicationContext
        context?.stopService(Intent(context, DataCollectingService::class.java))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}