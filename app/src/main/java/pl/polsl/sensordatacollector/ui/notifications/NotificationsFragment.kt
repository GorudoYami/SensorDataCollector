package pl.polsl.sensordatacollector.ui.notifications

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import pl.polsl.sensordatacollector.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private var isFirstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
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
        binding.spinnerGroup.adapter = adapter

        binding.spinnerGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        val selectedGroup = binding.spinnerGroup.selectedItem.toString()
        val groupKey = "data_$selectedGroup"

        Log.d("NotificationsFragment", "Saving data for group: $groupKey")

        editor.putString("${groupKey}_database_address", binding.editTextDatabaseAddress.text.toString())
        editor.putString("${groupKey}_database_name", binding.editTextDatabaseName.text.toString())
        editor.putString("${groupKey}_login", binding.editTextLogin.text.toString())
        editor.putString("${groupKey}_password", binding.editTextPassword.text.toString())
        editor.putString("${groupKey}_name", binding.editTextName.text.toString())
        editor.putString("${groupKey}_surname", binding.editTextSurname.text.toString())
        editor.putString("selected_group", selectedGroup)

        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        val selectedGroup = binding.spinnerGroup.selectedItem?.toString() ?: return
        val groupKey = "data_$selectedGroup"

        Log.d("NotificationsFragment", "Loading data for group: $groupKey")

        binding.editTextDatabaseAddress.setText(sharedPreferences.getString("${groupKey}_database_address", ""))
        binding.editTextDatabaseName.setText(sharedPreferences.getString("${groupKey}_database_name", ""))
        binding.editTextLogin.setText(sharedPreferences.getString("${groupKey}_login", ""))
        binding.editTextPassword.setText(sharedPreferences.getString("${groupKey}_password", ""))
        binding.editTextName.setText(sharedPreferences.getString("${groupKey}_name", ""))
        binding.editTextSurname.setText(sharedPreferences.getString("${groupKey}_surname", ""))
    }

    private fun loadSpinnerSelection() {
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val savedSelectedGroup = sharedPreferences.getString("selected_group", "")
        val position = (binding.spinnerGroup.adapter as ArrayAdapter<String>).getPosition(savedSelectedGroup)
        binding.spinnerGroup.setSelection(position)
    }
}
