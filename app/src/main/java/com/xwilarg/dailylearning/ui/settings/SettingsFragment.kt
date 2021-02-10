package com.xwilarg.dailylearning.ui.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.xwilarg.dailylearning.R


class SettingsFragment : Fragment() {
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)

            findPreference<Preference>("version")!!.setOnPreferenceClickListener {
                val builder = AlertDialog.Builder(activity)
                builder.setTitle(R.string.app_name)
                builder.setMessage(getString(R.string.settings_version) + ": " + requireContext().packageManager!!.getPackageInfo(requireContext().packageName, 0)!!.versionName)
                builder.setPositiveButton("OK") { _: DialogInterface, _: Int -> }
                builder.create().show()
                true
            }

            findPreference<Preference>("github")!!.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Xwilarg/DailyLearning")))
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