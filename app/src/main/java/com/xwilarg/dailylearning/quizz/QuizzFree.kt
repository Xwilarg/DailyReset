package com.xwilarg.dailylearning.quizz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import com.xwilarg.dailylearning.DrawingView
import com.xwilarg.dailylearning.R
import com.xwilarg.dailylearning.UpdateInfo

class QuizzFree : AQuizz() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_free)
        preload()
    }

    fun answer(view: View) {
        if (guessReverse) {
            checkAnswer(findViewById<EditText>(R.id.ZoneWriteText).text.toString().trim().lowercase())
        } else {
            findViewById<DrawingView>(R.id.viewDraw).getContent {
                msg -> if (msg == null) {
                    checkAnswer("")
                } else {
                    checkAnswer(msg)
                }
            }
        }
    }

    fun clear(view: View) {
        findViewById<DrawingView>(R.id.viewDraw).clear()
        findViewById<EditText>(R.id.ZoneWriteText).setText("")
    }

    fun help(view: View) {
        displayHelpPopup()
    }

    override fun loadQuestionAfter() {
        if (guessReverse) {
            findViewById<EditText>(R.id.ZoneWriteText).setText("")
            findViewById<EditText>(R.id.ZoneWriteText).visibility = View.VISIBLE
            findViewById<DrawingView>(R.id.viewDraw).visibility = View.GONE
        } else {
            findViewById<DrawingView>(R.id.viewDraw).clear()
            findViewById<EditText>(R.id.ZoneWriteText).visibility = View.GONE
            findViewById<DrawingView>(R.id.viewDraw).visibility = View.VISIBLE
        }
    }
}