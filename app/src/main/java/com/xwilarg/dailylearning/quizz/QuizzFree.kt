package com.xwilarg.dailylearning.quizz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.xwilarg.dailylearning.DrawingView
import com.xwilarg.dailylearning.R

class QuizzFree : AQuizz() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz_free)
        preload()
    }

    fun answer(view: View) {
        findViewById<DrawingView>(R.id.viewDraw).getContent {
                msg -> if (msg == null) {
            checkAnswer("")
        } else {
            checkAnswer(msg)
        }
            findViewById<DrawingView>(R.id.viewDraw).clear()
        }
    }

    fun clear(view: View) {
        findViewById<DrawingView>(R.id.viewDraw).clear()
    }

    fun help(view: View) {
        displayHelpPopup()
    }
}