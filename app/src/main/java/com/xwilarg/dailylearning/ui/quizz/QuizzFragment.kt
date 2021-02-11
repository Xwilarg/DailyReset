package com.xwilarg.dailylearning.ui.quizz

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.xwilarg.dailylearning.R
import com.xwilarg.dailylearning.UpdateInfo
import com.xwilarg.dailylearning.VocabularyInfo
import com.xwilarg.dailylearning.WordList
import com.xwilarg.dailylearning.quizz.QuizzChoices
import kotlinx.android.synthetic.main.fragment_quizz.view.*
import kotlinx.android.synthetic.main.fragment_quizz.view.quizzError

class QuizzFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_quizz, container, false)
        (v.buttonWordList as Button).setOnClickListener {
            startActivity(Intent(activity, WordList::class.java))
        }

        val quizzButton = (v.buttonQuizz as Button)
        quizzButton.setOnClickListener {
            startActivity(Intent(activity, QuizzChoices::class.java))
        }
        if (Gson().fromJson(requireContext().openFileInput(UpdateInfo.getLearntLanguage(requireContext()) + "Words.txt").bufferedReader().use {
                it.readText()
            }, Array<VocabularyInfo>::class.java).size < 2) {
            quizzButton.isEnabled = false
            v.quizzError.text = getString(R.string.quizz_error)
        }
        return v
    }
}