package pl.polsl.sensordatacollector.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import pl.polsl.sensordatacollector.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun saveData() {
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val editTextDatabaseAddress: EditText = binding.editTextDatabaseAddress
        val editTextDatabaseName: EditText = binding.editTextDatabaseName
        val editTextLogin: EditText = binding.editTextLogin
        val editTextPassword: EditText = binding.editTextPassword
        val spinnerGroup: Spinner = binding.spinnerGroup

        editor.putString("database_address", editTextDatabaseAddress.text.toString())
        editor.putString("database_name", editTextDatabaseName.text.toString())
        editor.putString("login", editTextLogin.text.toString())
        editor.putString("password", editTextPassword.text.toString())
        editor.putString("selected_group", spinnerGroup.selectedItem.toString())

        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)

        val editTextDatabaseAddress: EditText = binding.editTextDatabaseAddress
        val editTextDatabaseName: EditText = binding.editTextDatabaseName
        val editTextLogin: EditText = binding.editTextLogin
        val editTextPassword: EditText = binding.editTextPassword
        val spinnerGroup: Spinner = binding.spinnerGroup

        editTextDatabaseAddress.setText(sharedPreferences.getString("database_address", ""))
        editTextDatabaseName.setText(sharedPreferences.getString("database_name", ""))
        editTextLogin.setText(sharedPreferences.getString("login", ""))
        editTextPassword.setText(sharedPreferences.getString("password", ""))

        val options = arrayOf("Group 1", "Group 2", "Group 3")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGroup.adapter = adapter

        val savedSelectedGroup = sharedPreferences.getString("selected_group", "")
        spinnerGroup.setSelection(adapter.getPosition(savedSelectedGroup))
    }
}