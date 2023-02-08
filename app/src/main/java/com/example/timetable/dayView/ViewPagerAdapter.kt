package com.example.timetable.dayView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.timetable.R
import com.example.timetable.Subject

class ViewPagerAdapter(
    private val context: Context,
    private val subjects: List<Subject>,
    private val dayTimes: Map<Int, ArrayList<ArrayList<String>>>, // map containing lists of start and end times for each day
    private val editing: Boolean
): PagerAdapter() {

    override fun getCount(): Int {
        return dayTimes.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.view_pager_item, container, false)
        val times: ArrayList<ArrayList<String>> = dayTimes[position]!!
        val rv: RecyclerView = view.findViewById(R.id.rvSubjects)

        val rvAdapter = if (editing) {
            RVAdapterEditing(subjects, times)
        } else {
            RVAdapterStatic(subjects, times)
        }

        rv.adapter = rvAdapter

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
