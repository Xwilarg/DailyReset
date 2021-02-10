package com.xwilarg.dailylearning.ui.quizz

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.xwilarg.dailylearning.R
import kotlinx.android.synthetic.main.fragment_quizz.view.*

class QuizzFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_quizz, container, false)
        (v.buttonWordList as Button).setOnClickListener {
            startDisplayWordList()
        }
        return v
    }

    private fun startDisplayWordList() {
        startActivity(Intent(activity, WordList::class.java))
    }
}