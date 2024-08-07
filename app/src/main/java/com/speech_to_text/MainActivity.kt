package com.speech_to_text

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.speech_to_text.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == RESULT_OK && data != null){
                    val speakResult: ArrayList<String> = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                    binding.tvTapMicro.text = speakResult[0]
                }
            })

        binding.ivMircoPhone.setOnClickListener {
            convertSpeech()
        }
    }

    private fun convertSpeech() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            val speakResult: ArrayList<String> = data
                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

            binding.tvTapMicro.text = speakResult[0]
            activityResultLauncher.launch(intent)
        }
    }

}