package com.example.timetable.dayView

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.R
import com.example.timetable.Subject
import com.example.timetable.ViewModel
import com.example.timetable.databinding.FragmentDayBinding

class DayFragment : Fragment() {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var rv: RecyclerView
    private lateinit var rvAdapter: RVAdapter
    private lateinit var subjects: List<Subject>
    private lateinit var dayTimes: MutableMap<String, List<List<String>>>

    private lateinit var viewModel: ViewModel

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
        _binding = FragmentDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.settings -> {
                        openSettings()
                        true
                    } else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun openSettings() {
        requireActivity().findNavController(R.id.navHostFragment).navigate(R.id.action_dayFragment_to_settingsFragment)
    }
}