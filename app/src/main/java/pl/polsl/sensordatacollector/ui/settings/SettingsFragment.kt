package pl.polsl.sensordatacollector.ui.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import pl.polsl.sensordatacollector.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var isFirstLoad = true

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
        val options = arrayOf("Group 1", "Group 2", "Group 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spGroup.adapter = adapter

        binding.spGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (!isFirstLoad) {
                    saveData()
                }
                loadData()
                isFirstLoad = false
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No action needed
            }
        }

        loadSpinnerSelection()
    }

    private fun saveData() {
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val selectedGroup = binding.spGroup.selectedItem.toString()
        val groupKey = "data_$selectedGroup"

        Log.d("NotificationsFragment", "Saving data for group: $groupKey")

        editor.putString("${groupKey}_database_address", binding.etDatabaseAddress.text.toString())
        editor.putString("${groupKey}_database_name", binding.etDatabaseName.text.toString())
        editor.putString("${groupKey}_login", binding.etDatabaseLogin.text.toString())
        editor.putString("${groupKey}_password", binding.etDatabasePassword.text.toString())
        editor.putString("${groupKey}_name", binding.etFirstName.text.toString())
        editor.putString("${groupKey}_surname", binding.etLastName.text.toString())
        editor.putString("selected_group", selectedGroup)

        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        val selectedGroup = binding.spGroup.selectedItem?.toString() ?: return
        val groupKey = "data_$selectedGroup"

        Log.d("NotificationsFragment", "Loading data for group: $groupKey")

        binding.etDatabaseAddress.setText(sharedPreferences.getString("${groupKey}_database_address", ""))
        binding.etDatabaseName.setText(sharedPreferences.getString("${groupKey}_database_name", ""))
        binding.etDatabaseLogin.setText(sharedPreferences.getString("${groupKey}_database_login", ""))
        binding.etDatabasePassword.setText(sharedPreferences.getString("${groupKey}_database_password", ""))
        binding.etFirstName.setText(sharedPreferences.getString("${groupKey}_first_name", ""))
        binding.etLastName.setText(sharedPreferences.getString("${groupKey}_last_name", ""))
    }

    private fun loadSpinnerSelection() {
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val savedSelectedGroup = sharedPreferences.getString("selected_group", "")
        val position = (binding.spGroup.adapter as ArrayAdapter<String>).getPosition(savedSelectedGroup)
        binding.spGroup.setSelection(position)
    }
}
