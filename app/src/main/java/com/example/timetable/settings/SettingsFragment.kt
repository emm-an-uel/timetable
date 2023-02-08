package com.example.timetable.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.timetable.R
import com.example.timetable.Subject
import com.example.timetable.ViewModel
import com.example.timetable.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ViewModel
    private lateinit var subjects: ArrayList<Subject>
    private lateinit var dayTimes: MutableMap<Int, ArrayList<ArrayList<String>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]

        // sync variables with ViewModel
        if (viewModel.subjects == null) {
            viewModel.createSubjects()
        }
        subjects = viewModel.subjects!!

        if (viewModel.dayTimes == null) {
            viewModel.createDayTimes()
        }
        dayTimes = viewModel.dayTimes!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.generalSettings.editSubjects.setOnClickListener {
            openSubjectsSettings()
        }
    }

    private fun openSubjectsSettings() {
        requireActivity().findNavController(R.id.navHostFragment).navigate(R.id.action_settingsFragment_to_subjectsFragment)
    }
}