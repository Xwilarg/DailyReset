package com.xwilarg.dailylearning.ui.home

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.xwilarg.dailylearning.R
import com.xwilarg.dailylearning.VocabularyInfo
import java.time.LocalDate
import kotlin.random.Random

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_home, container, false)
        val preferences = requireActivity().getPreferences(MODE_PRIVATE)
        if (LocalDate.parse(preferences.getString("lastDaily", "1970-01-01")).plusDays(1) < LocalDate.now()) { // Update the current word if last one was taken more than 1 day ago
            // Get a random word from a random JLPT
            val nb = Random.nextInt(0, 100)
            val jlpt = if (nb > 50) {
                R.raw.jlpt5
            } else if (nb > 25) {
                R.raw.jlpt4
            } else if (nb > 15) {
                R.raw.jlpt3
            } else if (nb > 10) {
                R.raw.jlpt2
            } else {
                R.raw.jlpt1
            }
            val content = Gson().fromJson(resources.openRawResource(jlpt).bufferedReader().use { it.readText() }, Array<VocabularyInfo>::class.java)
            val voc = content[Random.nextInt(content.size)]

            // Update what we took in the storage
            with (preferences.edit()) {
                putString("lastDaily", LocalDate.now().toString())
                putString("currentWord", voc.word)
                putString("currentReading", voc.reading)
                putString("currentMeanings", voc.meaning.joinToString())
                apply()
            }
        }
        // Update UI
        v.findViewById<TextView>(R.id.dailyWord).text = preferences.getString("currentWord", "")
        v.findViewById<TextView>(R.id.dailyReading).text = preferences.getString("currentReading", "")
        v.findViewById<TextView>(R.id.dailyMeaning).text = preferences.getString("currentMeanings", "")
        return v
    }
}