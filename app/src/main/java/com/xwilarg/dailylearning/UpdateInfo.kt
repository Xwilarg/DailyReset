package com.xwilarg.dailylearning

import android.content.SharedPreferences
import android.content.res.Resources
import com.google.gson.Gson
import java.time.LocalDate
import kotlin.random.Random

object  UpdateInfo {
    fun updateJapaneseInfo(resources: Resources, preferences: SharedPreferences) {
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
                putString("lastDaily", java.time.LocalDate.now().toString())
                putString("currentWord", voc.word)
                putString("currentReading", voc.reading)
                putString("currentMeanings", voc.meaning.joinToString())
                apply()
            }
        }
    }
}