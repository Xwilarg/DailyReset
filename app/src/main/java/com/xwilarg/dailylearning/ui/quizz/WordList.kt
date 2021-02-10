package com.xwilarg.dailylearning.ui.quizz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import com.xwilarg.dailylearning.R
import com.xwilarg.dailylearning.VocabularyInfo

class WordList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
        Gson().fromJson(applicationContext.openFileInput("japaneseWords.txt").bufferedReader().use {
            it.readText()
        }, Array<VocabularyInfo>::class.java).forEach {
            findViewById<LinearLayout>(R.id.wordList).addView(TextView(applicationContext).apply {
                text = it.word + " (" + it.reading + ") - " + it.meaning.joinToString()
            })
        }
    }
}