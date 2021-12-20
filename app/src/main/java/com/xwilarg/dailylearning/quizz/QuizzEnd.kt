package com.xwilarg.dailylearning.quizz

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.xwilarg.dailylearning.R
import com.xwilarg.dailylearning.UpdateInfo
import java.time.LocalDate


class QuizzEnd : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_end)

        // Display pie chart of the answer
        val chart = findViewById<PieChart>(R.id.endChart)
        val entries: ArrayList<PieEntry> = ArrayList()
        chart.description.text = ""
        val correct = intent.getSerializableExtra("CORRECT") as Int
        val incorrect = intent.getSerializableExtra("INCORRECT") as Int
        if (correct > 0) {
            entries.add(PieEntry(correct.toFloat(), getString(R.string.quizz_result_good)))
        }
        if (incorrect > 0) {
            entries.add(PieEntry(incorrect.toFloat(), getString(R.string.quizz_result_bad)))
        }
        val set = PieDataSet(entries, "Results")
        set.colors = if (correct > 0 ) {
            listOf(Color.rgb(200, 255, 200), Color.rgb(255, 200, 200))
        } else { // If the user got nothing correct, we don't add "green" color to the pie chart
            listOf(Color.rgb(255, 200, 200))
        }
        chart.data = PieData(set)
        chart.setEntryLabelColor(Color.BLACK)
        chart.data.setValueTextSize(20f)
        chart.invalidate()

        // Succeed exam mode
        if (incorrect == 0 && intent.getSerializableExtra("IS_EXAM") as Boolean) {
            val lang = UpdateInfo.getLearntLanguage(applicationContext)
            val preferences = applicationContext.getSharedPreferences(lang + "Info", Context.MODE_PRIVATE)
            with (preferences.edit()) {
                val dates = preferences.getStringSet("succeedDates", emptySet())
                val today = LocalDate.now().toString()
                // Nothing to do if we already validated the exam today
                if (!dates!!.contains(today)) {
                    val tmp = dates.toMutableSet()
                    tmp.add(today)
                    putStringSet("succeedDates", tmp)
                    apply()
                }
            }
        }
    }
}