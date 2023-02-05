package com.example.timetable.dayView

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.MainActivity
import com.example.timetable.R
import com.example.timetable.Subject

class RVAdapter(
    private val subjects: List<Subject>,
    private val times: List<List<String>>
) : RecyclerView.Adapter<RVAdapter.NewViewHolder>() {

    class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val colorCode: View = itemView.findViewById(R.id.colorCode)
        val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
        val tvRoom: TextView = itemView.findViewById(R.id.tvRoom)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.rv_item, parent, false
        )
        return NewViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val context = holder.tvSubject.context
        val subject: Subject = subjects[position]
        val name = subject.name
        val color = subject.color
        val room = subject.room
        val times: List<String> = times[position]
        val startTime = times[0]
        val endTime = times[1]

        holder.tvSubject.text = name
        holder.colorCode.backgroundTintList = ColorStateList.valueOf(color)
        holder.tvRoom.text = room
        holder.tvTime.text = (context as MainActivity).getString(R.string.start_end_time, startTime, endTime)
    }
}