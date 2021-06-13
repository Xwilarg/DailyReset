package com.xwilarg.dailylearning.ui.settings

import android.R.attr.label
import android.app.AlertDialog
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.gson.Gson
import com.xwilarg.dailylearning.R
import com.xwilarg.dailylearning.UpdateInfo.getLearntLanguage


class SettingsFragment : Fragment() {
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            findPreference<Preference>("version")!!.setOnPreferenceClickListener {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle(R.string.app_name)
                builder.setMessage(
                    getString(R.string.settings_version) + ": " + requireContext().packageManager!!.getPackageInfo(
                        requireContext().packageName,
                        0
                    )!!.versionName
                )
                builder.setPositiveButton("OK") { _: DialogInterface, _: Int -> }
                builder.create().show()
                true
            }

            findPreference<Preference>("github")!!.setOnPreferenceClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Xwilarg/DailyLearning")
                    )
                )
                true
            }

            findPreference<Preference>("backup")!!.setOnPreferenceClickListener {
                val lang = getLearntLanguage(requireContext())
                val text = requireContext().openFileInput(lang + "Words.txt").bufferedReader().readText()

                val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
                val clip = ClipData.newPlainText("", text)
                clipboard!!.setPrimaryClip(clip)

                val builder = AlertDialog.Builder(activity)
                builder.setTitle(R.string.app_name)
                builder.setMessage(
                    getString(R.string.settings_explanation)
                )
                builder.setPositiveButton("OK") { _: DialogInterface, _: Int -> }
                builder.create().show()
                true
            }

            val preferences = requireContext().getSharedPreferences(
                "settings",
                Context.MODE_PRIVATE
            )

            val language = findPreference<ListPreference>("language")!!
            language.value = preferences.getString("language", "ja")
            language.setOnPreferenceChangeListener { preference: Preference, any: Any ->
                with(preferences.edit()) {
                    putString("language", any.toString())
                    apply()
                }
                true
            }

            val questionCount = findPreference<EditTextPreference>("questionCount")!!
            questionCount.text = preferences.getString("questionCount", "20")
            questionCount.setOnPreferenceChangeListener { preference: Preference, any: Any ->
                val number = any.toString().toIntOrNull()
                if (number != null && number > 0) {
                    with(preferences.edit()) {
                        putString("questionCount", any.toString())
                        apply()
                    }
                }
                true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_settings, container, false)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        return v
    }
}