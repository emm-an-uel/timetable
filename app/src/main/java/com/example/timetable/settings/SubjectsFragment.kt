package com.example.timetable.settings

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.R
import com.example.timetable.Subject
import com.example.timetable.ViewModel
import com.example.timetable.databinding.FragmentSubjectsBinding
import com.google.android.material.snackbar.Snackbar

class SubjectsFragment : Fragment() {

    private var _binding: FragmentSubjectsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ViewModel
    private lateinit var subjects: ArrayList<Subject> // note: don't add or remove items from "subjects" except when user clicks save
    private lateinit var shownSubjects: ArrayList<Subject>
    private lateinit var colors: List<Int>
    private lateinit var rvAdapter: RVAdapterSubjects

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        if (viewModel.subjects == null) {
            viewModel.createSubjects()
        }
        subjects = viewModel.subjects!!
        shownSubjects = arrayListOf() // keep track of the subjects currently shown
        shownSubjects.addAll(subjects)

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
        val shownSubjects = getShownSubjects()
        viewModel.saveSubjects(shownSubjects)
        Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()

        requireActivity().findNavController(R.id.navHostFragment).navigate(R.id.action_subjectsFragment_to_settingsFragment) // go back to SettingsFragment
    }

    private fun setupRV() {
        if (subjects.size == 0) {
            subjects.add(Subject("", ContextCompat.getColor(requireContext(), R.color.subject_blue), ""))
        }
        rvAdapter = RVAdapterSubjects(subjects, colors)
        binding.rvSubjects.adapter = rvAdapter

        // swipe functions
        ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val shownSubjects = getShownSubjects()
                val pos = viewHolder.adapterPosition
                val removedSubject: Subject = shownSubjects[pos]

                // the following was done, instead of simply removing from "subjects", so "subjects" remains unchanged until user clicks save
                shownSubjects.removeAt(pos)
                rvAdapter.updateSubjects(shownSubjects) // pass new list to rvAdapter
                createSnackBar(removedSubject)
            }
        }).attachToRecyclerView(binding.rvSubjects)
    }

    private fun createSnackBar(removedSubject: Subject) { // shows a SnackBar for user to undo removing the subject
        val snack = Snackbar.make(binding.rvSubjects, "", Snackbar.LENGTH_LONG)
        val customView = layoutInflater.inflate(R.layout.custom_snackbar, binding.root, false)
        if (snack.view.background != null) {
            snack.view.setBackgroundColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.mtrl_btn_transparent_bg_color))
        }

        val snackBarLayout: Snackbar.SnackbarLayout = snack.view as Snackbar.SnackbarLayout
        snackBarLayout.setPadding(5, 0, 5, 15)
        snackBarLayout.addView(customView)

        val tvPrimary: TextView = snackBarLayout.findViewById(R.id.tvPrimary)
        val name = removedSubject.name
        tvPrimary.text = "$name removed"

        val btnUndo: Button = snackBarLayout.findViewById(R.id.btnAction)
        btnUndo.text = "undo"
        btnUndo.setOnClickListener {
            cancelDelete(removedSubject)
            snack.dismiss()
        }

        snack.show()
    }

    private fun cancelDelete(restoredSubject: Subject) {
        val updatedSubjects = arrayListOf<Subject>()
        updatedSubjects.addAll(subjects)
        updatedSubjects.add(restoredSubject)

        rvAdapter.updateSubjects(updatedSubjects)
    }

    private fun addNewSubject() {
        if (rvAdapter.enableAddSubject) { // only add new subject if the last subject's name is not blank
            val newSubject = Subject("", ContextCompat.getColor(requireContext(), R.color.subject_blue), "")
            val updatedSubjects = getShownSubjects()
            updatedSubjects.add(newSubject)
            rvAdapter.updateSubjects(updatedSubjects)
        }
    }

    private fun getShownSubjects(): ArrayList<Subject> {
        val shownSubjects = arrayListOf<Subject>()
        for (i in 0 until rvAdapter.itemCount) { // for each itemView in RecyclerView
            val holder = binding.rvSubjects.findViewHolderForAdapterPosition(i)
            if (holder != null) {
                val name = holder.itemView.findViewById<EditText>(R.id.etSubject).text.toString().trim()
                if (name != "") { // only save if its name isn't blank
                    val colorIndex = holder.itemView.findViewById<Spinner>(R.id.spinnerColor).selectedItemPosition
                    val color = colors[colorIndex]
                    val room = holder.itemView.findViewById<EditText>(R.id.etRoom).text.toString().trim()

                    val subject = Subject(name, color, room)
                    shownSubjects.add(subject)
                }
            }
        }
        return shownSubjects
    }
}