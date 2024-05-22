package pl.polsl.sensordatacollector.ui.home

import android.graphics.Color
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.polsl.sensordatacollector.R
import pl.polsl.sensordatacollector.databinding.FragmentHomeBinding
import pl.polsl.sensordatacollector.sensors.SensorsListener

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var listener: SensorsListener
    private var isListening = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        listener = SensorsListener(requireContext().getSystemService(SensorManager::class.java))

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val button: Button = binding.button
        button.setBackgroundColor(Color.parseColor("#00FF00"))
        button.setText("START")

        button.setOnClickListener {
            if (isListening) {
                button.setBackgroundColor(Color.parseColor("#00FF00"))
                button.setText("START")
                listener.stop()
            } else {
                button.setBackgroundColor(Color.parseColor("#FF0000"))
                button.setText("STOP")
                listener.start()
            }
            isListening = !isListening
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}