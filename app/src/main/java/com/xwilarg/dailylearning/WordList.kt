package com.xwilarg.dailylearning

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson

class WordList : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
        Gson().fromJson(applicationContext.openFileInput(UpdateInfo.getLearntLanguage(applicationContext) + "Words.txt").bufferedReader().use {
            it.readText()
        }, Array<VocabularyInfo>::class.java).forEach {
            findViewById<LinearLayout>(R.id.wordList).addView(TextView(applicationContext).apply {
                text = it.word + " " + if (it.reading != null) { " (" + it.reading + ") " } else { "" } + " - " + it.meaning.joinToString()
            })
        }
    }
}