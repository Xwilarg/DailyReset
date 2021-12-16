package com.xwilarg.dailylearning.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.xwilarg.dailylearning.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.time.LocalDate
import kotlin.random.Random

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_home, container, false)
        (v.buttonWordList as Button).setOnClickListener {
            startActivity(Intent(activity, WordList::class.java))
        }
        (v.buttonSentences as Button).setOnClickListener {
            startActivity(Intent(activity, SampleSentence::class.java))
        }
        val preferences = UpdateInfo.updateInfo(resources, requireActivity())
        // Update UI
        v.findViewById<TextView>(R.id.dailyWord).text = preferences.getString("currentWord", "")
        v.findViewById<TextView>(R.id.dailyReading).text = preferences.getString("currentReading", "")
        v.findViewById<TextView>(R.id.dailyMeaning).text = preferences.getString("currentMeanings", "")
        return v
    }
}