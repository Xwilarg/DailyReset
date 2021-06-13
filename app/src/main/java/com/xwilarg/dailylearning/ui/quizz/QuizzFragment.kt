package com.xwilarg.dailylearning.ui.quizz

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModel
import com.google.mlkit.vision.digitalink.DigitalInkRecognitionModelIdentifier
import com.xwilarg.dailylearning.*
import com.xwilarg.dailylearning.quizz.QuizzChoices
import com.xwilarg.dailylearning.quizz.QuizzFree
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

        val quizzButtonTraining = (v.buttonQuizzTraining as Button)
        val freeButtonTraining = (v.buttonFreeTraining as Button)
        val quizzButtonExam = (v.buttonQuizzExam as Button)
        val freeButtonExam = (v.buttonFreeExam as Button)
        quizzButtonTraining.setOnClickListener {
            startButtonQuizz(true)
        }
        freeButtonTraining.setOnClickListener {
            startFreeQuizz(true)
        }
        quizzButtonExam.setOnClickListener {
            startButtonQuizz(false)
        }
        freeButtonExam.setOnClickListener {
            startFreeQuizz(false)
        }
        if (Gson().fromJson(requireContext().openFileInput(UpdateInfo.getLearntLanguage(requireContext()) + "Words.txt").bufferedReader().use {
                it.readText()
            }, Array<VocabularyInfo>::class.java).size < 2) {
            quizzButtonTraining.isEnabled = false
            freeButtonTraining.isEnabled = false
            quizzButtonExam.isEnabled = false
            freeButtonExam.isEnabled = false
            v.quizzError.text = getString(R.string.quizz_error)
        }
        return v
    }

    private fun startButtonQuizz(isTraining: Boolean) {
        val intent = Intent(activity, QuizzChoices::class.java)
        intent.putExtra("IS_TRAINING", isTraining)
        startActivity(intent)
    }

    private fun startFreeQuizz(isTraining: Boolean) {
        val intent = Intent(activity, QuizzFree::class.java)
        intent.putExtra("IS_TRAINING", isTraining)

        val modelIdentifier = DigitalInkRecognitionModelIdentifier.fromLanguageTag("ja")
        val model: DigitalInkRecognitionModel =
            DigitalInkRecognitionModel.builder(modelIdentifier!!).build()
        val remoteModelManager = RemoteModelManager.getInstance()

        remoteModelManager.isModelDownloaded(model).addOnSuccessListener { res: Boolean ->
            if (res) { // Model already downloaded
                startActivity(intent)
            } else {
                val builder = AlertDialog.Builder(activity)
                builder.setMessage(getString(R.string.ocr_download))
                builder.setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
                    val builderD = AlertDialog.Builder(activity)
                    builderD.setMessage(getString(R.string.ocr_downloading))
                    val pD = builderD.create()
                    pD.setCanceledOnTouchOutside(false)
                    pD.show()

                    remoteModelManager.download(model, DownloadConditions.Builder().build())
                        .addOnSuccessListener { // Data downloaded
                            startActivity(intent)
                        }
                        .addOnFailureListener { e: Exception ->
                            pD.dismiss()
                            val builderError = AlertDialog.Builder(activity)
                            builderError.setMessage(getString(R.string.ocr_error) + e.message!!)
                            builderError.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                            }
                            val pError = builderError.create()
                            pError.setCanceledOnTouchOutside(false)
                            pError.show()
                        }
                }
                builder.setNegativeButton(getString(R.string.no)) { _: DialogInterface, _: Int -> }
                val p = builder.create()
                p.setCanceledOnTouchOutside(false)
                p.show()
            }
        }.addOnFailureListener { e: Exception ->
            val builderError = AlertDialog.Builder(activity)
            builderError.setMessage(getString(R.string.ocr_error) + e.message!!)
            val pError = builderError.create()
            builderError.setPositiveButton("OK") { _: DialogInterface, _: Int ->
                pError.dismiss()
            }
            pError.setCanceledOnTouchOutside(false)
            pError.show()
        }
    }
}