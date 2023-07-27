package com.xwilarg.dailylearning.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.xwilarg.dailylearning.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_home, container, false)
        val preferences = UpdateInfo.updateInfo(resources, requireActivity())
        (v.buttonWordList as Button).setOnClickListener {
            startActivity(Intent(activity, WordList::class.java))
        }
        (v.buttonSentences as Button).setOnClickListener {
            startActivity(Intent(activity, SampleSentence::class.java))
        }
        (v.buttonWebsite as Button).setOnClickListener {
            val word = preferences.getString("currentWord", "")

            val target = if (word != "") { word } else { preferences.getString("currentReading", "") }

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://jisho.org/word/$target"))
            startActivity(browserIntent)
        }
        v.findViewById<Button>(R.id.buttonWebsite).visibility = if (UpdateInfo.getLearntLanguage(requireContext()) == "ja") { View.VISIBLE } else { View.GONE }
        // Update UI
        v.findViewById<TextView>(R.id.dailyWord).text = preferences.getString("currentWord", "")
        v.findViewById<TextView>(R.id.dailyReading).text = preferences.getString("currentReading", "")
        v.findViewById<TextView>(R.id.dailyMeaning).text = preferences.getString("currentMeanings", "")
        return v
    }
}