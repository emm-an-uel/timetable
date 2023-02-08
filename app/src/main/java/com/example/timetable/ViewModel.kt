package com.example.timetable

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import java.io.File
import java.io.StringReader

class ViewModel(private val app: Application) : AndroidViewModel(app) {

    var subjects: ArrayList<Subject>? = null
    var dayTimes: MutableMap<Int, ArrayList<ArrayList<String>>>? = null
    var colors: ArrayList<Int>? = null

    fun createColors() {
        colors = arrayListOf(
            ContextCompat.getColor(app, R.color.subject_blue),
            ContextCompat.getColor(app, R.color.subject_dark_blue),
            ContextCompat.getColor(app, R.color.subject_green),
            ContextCompat.getColor(app, R.color.subject_orange),
            ContextCompat.getColor(app, R.color.subject_pink),
            ContextCompat.getColor(app, R.color.subject_purple),
            ContextCompat.getColor(app, R.color.subject_red),
            ContextCompat.getColor(app, R.color.subject_yellow)
        )
    }

    fun createSubjects() {
        subjects = arrayListOf()

        val file = File(app.filesDir, "subjects")
        if (file.exists()) {
            val fileJson = file.readText()
            JsonReader(StringReader(fileJson)).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        val subject = Klaxon().parse<Subject>(reader)!!
                        subjects!!.add(subject)
                    }
                }
            }
        }
    }

    fun createDayTimes() {
        dayTimes = mutableMapOf()

        val file = File(app.filesDir, "dayTimes")
        if (file.exists()) {
            val fileJson = file.readText()
            JsonReader(StringReader(fileJson)).use { reader ->
                reader.beginArray {
                    while (reader.hasNext()) {
                        val dayTime = Klaxon().parse<DayTime>(reader)!!
                        val day: Int = dayTime.day
                        val times: ArrayList<ArrayList<String>> = dayTime.times

                        dayTimes!![day] = times
                    }
                }
            }
        } else { // file doesn't exist
            for (n in 0 until 5) { // create map with empty lists
                dayTimes!![n] = arrayListOf()
            }
        }
    }

    fun saveSubjects(updatedSubjects: ArrayList<Subject>) {
        subjects = updatedSubjects
        val file = Klaxon().toJsonString(subjects)
        app.openFileOutput("subjects", Context.MODE_PRIVATE).use {
            it.write(file.toByteArray())
        }
    }

    fun saveDayTimes() {
        val listDayTimes: ArrayList<DayTime> = arrayListOf()
        if (dayTimes != null) {
            for ((day, times) in dayTimes!!) {
                val dayTime = DayTime(day, times)
                listDayTimes.add(dayTime)
            }
        }

        val file = Klaxon().toJsonString(listDayTimes)
        app.openFileOutput("dayTimes", Context.MODE_PRIVATE).use {
            it.write(file.toByteArray())
        }
    }

    data class DayTime(
        val day: Int, // 0 is Monday; 4 is Friday
        val times: ArrayList<ArrayList<String>>
    )
}