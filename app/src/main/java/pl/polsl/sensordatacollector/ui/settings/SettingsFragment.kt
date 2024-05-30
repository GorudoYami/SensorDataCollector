package pl.polsl.sensordatacollector.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import pl.polsl.sensordatacollector.databinding.FragmentSettingsBinding
import pl.polsl.sensordatacollector.preferences.ApplicationPreferences

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var _spProfile: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        loadData()
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSpinner() {
        _spProfile = binding.spProfile
        val options = arrayOf("Profile 1", "Profile 2", "Profile 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        _spProfile.adapter = adapter
        _spProfile.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                saveData()
            }
            _spProfile.performClick()
        }

        _spProfile.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                loadData()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun saveData() {
        val applicationPreferences = ApplicationPreferences(requireActivity())
        applicationPreferences.databaseAddress = binding.etDatabaseAddress.text.toString()
        applicationPreferences.databaseName = binding.etDatabaseName.text.toString()
        applicationPreferences.databaseLogin = binding.etDatabaseLogin.text.toString()
        applicationPreferences.databasePassword = binding.etDatabasePassword.text.toString()
        applicationPreferences.firstName = binding.etFirstName.text.toString()
        applicationPreferences.lastName = binding.etLastName.text.toString()
        applicationPreferences.profile = binding.spProfile.selectedItemPosition
        applicationPreferences.save()
    }

    private fun loadData() {
        val applicationPreferences = ApplicationPreferences(requireActivity())
        binding.etDatabaseAddress.setText(applicationPreferences.databaseAddress)
        binding.etDatabaseName.setText(applicationPreferences.databaseName)
        binding.etDatabaseLogin.setText(applicationPreferences.databaseLogin)
        binding.etDatabasePassword.setText(applicationPreferences.databasePassword)
        binding.etFirstName.setText(applicationPreferences.firstName)
        binding.etLastName.setText(applicationPreferences.lastName)
        binding.spProfile.setSelection(applicationPreferences.profile)
    }
}
