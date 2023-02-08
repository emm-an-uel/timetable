package com.example.timetable.dayView

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.timetable.R
import com.example.timetable.Subject
import com.example.timetable.ViewModel
import com.example.timetable.databinding.FragmentDayBinding

class DayFragment : Fragment() {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var editing: Boolean = false
    private var currentDay: Int = 0 // 0 is Monday; 4 is Friday
    private lateinit var subjects: List<Subject>
    private lateinit var dayTimes: MutableMap<Int, ArrayList<ArrayList<String>>>

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
        setupViewPager()
    }

    override fun onResume() {
        super.onResume()
        editing = false
    }

    private fun setupViewPager() {
        viewPagerAdapter = ViewPagerAdapter(requireContext(), subjects, dayTimes, editing)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // do nothing
            }

            override fun onPageSelected(position: Int) {
                currentDay = position
            }

            override fun onPageScrollStateChanged(state: Int) {
                // do nothing
            }
        })
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
                    }

                    R.id.edit -> {
                        if (editing) { // if editing is currently enabled
                            disableEditing()
                            menuItem.setIcon(R.drawable.ic_edit)
                        } else { // if editing is currently disabled
                            enableEditing()
                            menuItem.setIcon(R.drawable.ic_edit_off)
                        }
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun enableEditing() {
        editing = true
    }

    private fun disableEditing() {
        editing = false
    }

    private fun openSettings() {
        requireActivity().findNavController(R.id.navHostFragment).navigate(R.id.action_dayFragment_to_settingsFragment)
    }
}