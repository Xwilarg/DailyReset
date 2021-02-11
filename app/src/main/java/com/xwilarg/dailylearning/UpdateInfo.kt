package com.xwilarg.dailylearning

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.google.gson.Gson
import java.lang.Exception
import java.time.LocalDate
import kotlin.random.Random

object UpdateInfo {
    fun getLearntLanguage(context: Context): String? {
        val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return preferences.getString("language", "ja")
    }

    fun updateInfo(resources: Resources, context: Context) : SharedPreferences {
        val lang = getLearntLanguage(context)
        val preferences = context.getSharedPreferences(lang + "Info", Context.MODE_PRIVATE)
        if (LocalDate.parse(preferences.getString("lastDaily", "1970-01-01")).plusDays(1) < LocalDate.now()) { // Update the current word if last one was taken more than 1 day ago

            val voc = when (lang) {
                "ja" -> {
                    updateJapaneseInfo(resources)
                }
                "kr" -> {
                    updateKoreanInfo(resources)
                }
                else -> {
                    throw Exception("Invalid language $lang")
                }
            }
            // Update what we took in the storage
            with (preferences.edit()) {
                putString("lastDaily", java.time.LocalDate.now().toString())
                putString("currentWord", voc.word)
                putString("currentReading", voc.reading)
                putString("currentMeanings", voc.meaning.joinToString())
                apply()
            }

            val savedData = if (context.fileList().contains(lang + "Words.txt")) {
                Gson().fromJson(context.openFileInput(lang + "Words.txt").bufferedReader().use {
                    it.readText()
                }, Array<VocabularyInfo>::class.java)
            } else {
                arrayOf()
            }

            val list = savedData.toMutableList()
            if (!savedData.any{ it.word == voc.word }) // We didn't already saved this word
            {
                val addedData = VocabularyInfo(date = LocalDate.now(), word = voc.word, meaning = voc.meaning, reading = voc.reading)
                list.add(addedData)
            }
            context.openFileOutput(lang + "Words.txt", Context.MODE_PRIVATE).use { itWrite ->
                itWrite.bufferedWriter().use {
                    it.write(Gson().toJson(list.toTypedArray()).toString())
                }
            }
        }
        return preferences
    }

    fun updateKoreanInfo(resources: Resources) : VocabularyInfo {
        val content = Gson().fromJson(resources.openRawResource(R.raw.korean).bufferedReader().use { it.readText() }, Array<VocabularyInfo>::class.java)
        return content[Random.nextInt(content.size)]
    }

    fun updateJapaneseInfo(resources: Resources) : VocabularyInfo {
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
        return content[Random.nextInt(content.size)]
    }
}