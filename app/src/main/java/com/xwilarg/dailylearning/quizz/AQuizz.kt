package com.xwilarg.dailylearning.quizz

import android.content.Intent
import android.graphics.Color
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.xwilarg.dailylearning.R
import com.xwilarg.dailylearning.VocabularyInfo
import kotlin.random.Random

open class AQuizz : AppCompatActivity() {
    fun preload() {
        words = Gson().fromJson(applicationContext.openFileInput("japaneseWords.txt").bufferedReader().use {
            it.readText()
        }, Array<VocabularyInfo>::class.java)
        remainingWords = words.copyOf().toCollection(ArrayList())
        loadQuestion()
    }

    fun checkAnswer(myAnswer: String) {
        findViewById<TextView>(R.id.textAnswerYou).text = myAnswer
        val rightAnswer = if (guessReverse) { current.meaning[0] } else { current.reading }
        findViewById<TextView>(R.id.textAnswerHim).text = if (guessReverse) { current.meaning[0] } else { current.reading }
        val isRight = rightAnswer == myAnswer
        if (isRight) {
            nbRight++
        } else {
            nbWrong++
        }
        findViewById<ConstraintLayout>(R.id.ConstraintLayoutAnswer).setBackgroundColor(
            if (isRight) {
                Color.rgb(200, 255, 200)
            } else {
                Color.rgb(255, 200, 200)
            }
        )
        loadQuestion()
    }

    fun getRandomChoices(): ArrayList<String> {
        val choices = arrayListOf<String>()
        choices.add(if (guessReverse) { current.meaning[0] } else { current.reading })
        while (choices.size < 4 && choices.size != words.size) {
            val random = words[Random.nextInt(0, words.size)]
            val randomChoice = if (guessReverse) { random.meaning[0] } else { random.reading }
            if (!choices.contains(randomChoice)) {
                choices.add(randomChoice)
            }
        }
        choices.shuffle()
        return choices
    }

    fun loadQuestion() {
        if (remainingWords.size == 0) {
            val intent = Intent(applicationContext, QuizzEnd::class.java)
            intent.putExtra("CORRECT", nbRight)
            intent.putExtra("INCORRECT", nbWrong)
            startActivity(intent)
            return
        }

        // Get next question
        val index = Random.nextInt(remainingWords.size)
        current = remainingWords[index]
        remainingWords.removeAt(index)

        guessReverse = Random.nextInt(0, 2) == 0
        // Resize text depending if the question contains only the kanji or a list of words
        findViewById<TextView>(R.id.textQuizz).textSize = if (guessReverse) {
            50f
        } else {
            20f
        }
        // textQuizz is the question and textQuizzHelp is a small text under it displayed as a help
        findViewById<TextView>(R.id.textQuizz).text = if (guessReverse) { current.word } else { current.meaning.joinToString() }
        findViewById<TextView>(R.id.textQuizzHelp).text = if (guessReverse) { current.reading } else { "" }
        loadQuestionAfter()
    }

    open fun loadQuestionAfter() { } // virtual function called after the question was loaded

    var nbRight = 0
    var nbWrong = 0
    lateinit var remainingWords: ArrayList<VocabularyInfo>
    lateinit var words: Array<VocabularyInfo>
    lateinit var current: VocabularyInfo
    var guessReverse = false // If true is given the japanese and must guess the english, else is reversed
}