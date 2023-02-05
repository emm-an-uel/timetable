package com.example.timetable.settings

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.example.timetable.R
import com.example.timetable.Subject
import com.example.timetable.ViewModel
import com.example.timetable.databinding.FragmentSubjectsBinding

class SubjectsFragment : Fragment() {

    private var _binding: FragmentSubjectsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ViewModel
    private lateinit var subjects: ArrayList<Subject>
    private lateinit var colors: List<Int>
    private lateinit var rvAdapter: RVAdapterSubjects

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        if (viewModel.subjects == null) {
            viewModel.createSubjects()
        }
        subjects = viewModel.subjects!!

        if (viewModel.colors == null) {
            viewModel.createColors()
        }
        colors = viewModel.colors!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRV()
        setupMenu()

        binding.fabAddSubject.setOnClickListener {
            addNewSubject()
        }
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.subjects_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.btnSave -> {
                        saveSubjects()
                        true
                    } else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveSubjects() {
        var n = 0
        val updatedSubjects = arrayListOf<Subject>()

        while (n < rvAdapter.itemCount) { // for each itemView in RecyclerView
            val itemView: View = binding.rvSubjects.findViewWithTag(n)
            val name = itemView.findViewById<EditText>(R.id.etSubject).text.toString().trim()
            val colorIndex = itemView.findViewById<Spinner>(R.id.spinnerColor).selectedItemPosition
            val color = colors[colorIndex]
            val room = itemView.findViewById<EditText>(R.id.etRoom).text.toString().trim()

            val subject = Subject(name, color, room)
            updatedSubjects.add(subject)
            n++
        }

        viewModel.saveSubjects(updatedSubjects)
    }

    private fun setupRV() {
        rvAdapter = RVAdapterSubjects(subjects, colors)
        binding.rvSubjects.adapter = rvAdapter
    }

    private fun addNewSubject() {
        if (rvAdapter.subjects.last().name != "") { // only add new subject if the last subject is not blank
            val newSubject = Subject("", ContextCompat.getColor(requireContext(), R.color.subject_blue), "")
            val updatedSubjects = arrayListOf<Subject>()
            updatedSubjects.apply {
                addAll(subjects)
                add(newSubject)
            }
            rvAdapter.updateSubjects(updatedSubjects)
        }
    }
}