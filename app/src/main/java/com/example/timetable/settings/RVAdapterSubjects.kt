package com.example.timetable.settings

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.R
import com.example.timetable.Subject

class RVAdapterSubjects(
    var subjects: ArrayList<Subject>,
    private val colors: List<Int>
) : RecyclerView.Adapter<RVAdapterSubjects.NewViewHolder>() {

    private var count = 0
    var enableAddSubject = true

    class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        val name = subject.name
        val color = subject.color
        val room = subject.room

        val colorIndex = colors.indexOf(color)

        holder.etSubject.apply {
            setText(name)
            addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // do nothing
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // do nothing
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (holder.adapterPosition == subjects.size - 1) { // last item in list
                        enableAddSubject = p0.toString().trim() != "" // enable fabAddSubject only if last item in list contains a "name" attribute
                    }
                }
            })
        }
        holder.etRoom.setText(room)
        holder.spinnerColor.apply {
            adapter = SpinnerAdapter(holder.spinnerColor.context, colors)
            setSelection(colorIndex)
        }
    }

    class SpinnerAdapter(context: Context, colors: List<Int>) :
        ArrayAdapter<Int>(context, 0, colors) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return mView(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return mView(position, convertView, parent)
        }

        private fun mView(position: Int, convertView: View?, parent: ViewGroup): View {
            val color = getItem(position)
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.spinner_item_color, parent, false)

            val colorCircle: ImageView = view.findViewById(R.id.colorCircle)
            colorCircle.imageTintList = if (color != null) {
                ColorStateList.valueOf(color)
            } else {
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.gray))
            }

            return view
        }
    }
}
