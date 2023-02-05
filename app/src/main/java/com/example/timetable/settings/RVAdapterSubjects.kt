package com.example.timetable.settings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.R
import com.example.timetable.Subject

class RVAdapterSubjects(
    var subjects: ArrayList<Subject>,
    private val colors: List<Int>
) : RecyclerView.Adapter<RVAdapterSubjects.NewViewHolder>() {

    private var count = 0

    class NewViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val etSubject: EditText = itemView.findViewById(R.id.etSubject)
        val etRoom: EditText = itemView.findViewById(R.id.etRoom)
        val spinnerColor: Spinner = itemView.findViewById(R.id.spinnerColor)
    }

    fun updateSubjects(updatedSubjects: ArrayList<Subject>) {
        subjects = updatedSubjects
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_subjects, parent, false)
        itemView.tag = count
        count++

        return NewViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val subject: Subject = subjects[position]
        if (subject.name != "") { // if subject exists
            val name = subject.name
            val color = subject.color
            val room = subject.room

            val colorIndex = colors.indexOf(color)

            holder.etSubject.setText(name)
            holder.etRoom.setText(room)
            holder.spinnerColor.setSelection(colorIndex) // sets default selection to colorIndex
        }
    }
}
