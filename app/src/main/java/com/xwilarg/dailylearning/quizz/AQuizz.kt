package com.xwilarg.dailylearning.quizz

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.gson.Gson
import com.xwilarg.dailylearning.R
import com.xwilarg.dailylearning.UpdateInfo
import com.xwilarg.dailylearning.UpdateInfo.getLearntLanguage
import com.xwilarg.dailylearning.VocabularyInfo
import kotlinx.android.synthetic.main.fragment_quizz.view.*

open class AQuizz : AppCompatActivity() {
    fun preload() {
        isTraining = intent.getSerializableExtra("IS_TRAINING") as Boolean
        if (!isTraining) {
            val helpButton = findViewById<Button>(R.id.buttonHelp)
            if (helpButton != null) {
                helpButton.isEnabled = false
            }
        }

        val original = Gson().fromJson(applicationContext.openFileInput(getLearntLanguage(applicationContext) + "Words.txt").bufferedReader().use {
            it.readText()
        }, Array<VocabularyInfo>::class.java).copyOf().toCollection(ArrayList())
        val preferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val questionCount = preferences.getString("questionCount", "20")!!.toInt()
        if (original.size <= questionCount) {
            words = original
        } else {
            words = ArrayList()
            while (words.size < questionCount) {
                val index = UpdateInfo.randomNumber(0, original.size)
                words.add(original[index])
                original.removeAt(index)
            }
        }
        remainingWords = words.toCollection(ArrayList())
        remainingWords.shuffle()
        loadQuestion()
    }

    fun displayHelpPopup() {
        didAskForHelp = true
        val builder = AlertDialog.Builder(this)
        builder.setMessage(current.word + "\n" + current.reading + "\n\n" + current.meaning.joinToString())
        builder.create().show()
    }

    fun checkAnswer(myAnswer: List<String>) {
        for(a in myAnswer) {
            if (a == getRightAnswer(a)) {
                checkAnswer(a)
                return
            }
        }
        checkAnswer(myAnswer[0])
    }

    private fun getRightAnswer(myAnswer: String): String? {
        if (guessReverse)
        {
            if (current.meaning.contains(myAnswer)) {
                return myAnswer
            }
            return current.meaning[0]
        }
        else
        {
            if (current.word == myAnswer || current.reading == myAnswer) return myAnswer
            if (current.reading != null) return "${current.word} / ${current.reading}"
            return current.word
        }
    }

    fun checkAnswer(myAnswer: String) {
        findViewById<TextView>(R.id.textAnswerYou).text = myAnswer
        val rightAnswer = getRightAnswer(myAnswer)
        findViewById<TextView>(R.id.textAnswerHim).text = rightAnswer
        var isRight = rightAnswer == myAnswer
        val color: Int = if (isRight) {
            if (didAskForHelp) {
                nbWrong++
                isRight = false
                Color.rgb(255, 255, 200)
            } else {
                nbRight++
                Color.rgb(200, 255, 200)
            }
        } else {
            nbWrong++
            Color.rgb(255, 200, 200)
        }

        // No verification to do on exam mode
        // However for training more we only remove the answer if we are right
        val elem = remainingWords[0]
        remainingWords.removeAt(0)
        if (isTraining && !isRight) {
            if (remainingWords.size == 0) {
                // List is empty so we just add it back since we can't put it behind smth else
                remainingWords.add(elem)
            } else {
                // We move the question a bit later in the list
                remainingWords.add(UpdateInfo.randomNumber(1, remainingWords.size + 1), elem)
            }
        }

        findViewById<ConstraintLayout>(R.id.ConstraintLayoutAnswer).setBackgroundColor(color)
        loadQuestion()
    }

    fun getRandomChoices(): ArrayList<String> {
        val choices = arrayListOf<String>()
        choices.add(if (guessReverse) { current.meaning[0] } else { current.reading ?: current.word })
        while (choices.size < 4 && choices.size != words.size) {
            val random = words[UpdateInfo.randomNumber(0, words.size)]
            val randomChoice = if (guessReverse) { random.meaning[0] } else { random.reading ?: random.word }
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
            intent.putExtra("IS_EXAM", !isTraining)
            startActivity(intent)
            return
        }
        didAskForHelp = false

        // Get next question
        current = remainingWords[0]

        guessReverse = UpdateInfo.randomNumber(0, 2) == 0
        val textQuizz = findViewById<TextView>(R.id.textQuizz)
        // Resize text depending if the question contains only the kanji or a list of words
        textQuizz.textSize = if (guessReverse) {
            50f
        } else {
            20f
        }
        // textQuizz is the question and textQuizzHelp is a small text under it displayed as a help
        textQuizz.text = if (guessReverse) { current.word } else { current.meaning.joinToString() }
        findViewById<TextView>(R.id.textQuizzHelp).text = if (guessReverse) { current.reading } else { "" }
        loadQuestionAfter()
    }

    open fun loadQuestionAfter() { } // virtual function called after the question was loaded

    private var nbRight = 0
    private var nbWrong = 0
    private lateinit var remainingWords: ArrayList<VocabularyInfo>
    private lateinit var words: ArrayList<VocabularyInfo>
    private lateinit var current: VocabularyInfo
    private var isTraining = false
    protected var guessReverse = false // If true is given the japanese and must guess the english, else is reversed
    private var didAskForHelp = false
}