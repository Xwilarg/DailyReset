package com.xwilarg.dailylearning

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class SampleSentence : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_sentence)
        val file = if (UpdateInfo.getLearntLanguage(applicationContext) == "ja") {
            R.raw.sentences_ja
        } else {
            R.raw.sentences_kr
        }
        val translations = Gson().fromJson(resources.openRawResource(file).bufferedReader().use {
            it.readText()
        }, Array<SentenceInfo>::class.java)
        val lang = UpdateInfo.getLearntLanguage(applicationContext)
        val preferences = applicationContext.getSharedPreferences(lang + "Info", Context.MODE_PRIVATE)
        Log.e("", preferences.getString("currentWord", "").toString())
        val filtered = translations.filter { info ->
            preferences.getString("currentWord", "").toString() in info.sentence
        }
        findViewById<TextView>(R.id.sentence).text = filtered[0].sentence
        findViewById<TextView>(R.id.translation).text = filtered[0].translation
    }
}