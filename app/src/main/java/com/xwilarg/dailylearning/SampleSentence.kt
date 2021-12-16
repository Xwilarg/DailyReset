package com.xwilarg.dailylearning

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class SampleSentence : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_sentence)

        val lang = UpdateInfo.getLearntLanguage(applicationContext)
        val preferences = applicationContext.getSharedPreferences(lang + "Info", Context.MODE_PRIVATE)
        findViewById<TextView>(R.id.word).text = preferences.getString("currentWord", "")
        findViewById<TextView>(R.id.sentence).text = getString(R.string.loading)

        GlobalScope.launch(Dispatchers.Main) {
            val file = if (UpdateInfo.getLearntLanguage(applicationContext) == "ja") {
                R.raw.sentences_ja
            } else {
                R.raw.sentences_kr
            }
            val translations = Gson().fromJson(resources.openRawResource(file).bufferedReader().use {
                it.readText()
            }, Array<SentenceInfo>::class.java)
            val filtered = translations.filter { info ->
                preferences.getString("currentWord", "").toString() in info.sentence &&
                        preferences.getString("currentMeanings", "").toString().split(',').any { e ->
                            e.trim() in info.translation
                        }
            }
            if (filtered.isNotEmpty()) {
                val rand = filtered[Random.nextInt(filtered.size)]
                findViewById<TextView>(R.id.sentence).text = rand.sentence
                findViewById<TextView>(R.id.translation).text = rand.translation
            } else {
                findViewById<TextView>(R.id.sentence).text = getString(R.string.quizz_no_sentence)
            }
        }
    }
}