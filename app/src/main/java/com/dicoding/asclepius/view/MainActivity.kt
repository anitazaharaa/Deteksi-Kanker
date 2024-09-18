package com.dicoding.asclepius.view

import ImageClassifierHelper
import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.lang.Error

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri = "cureentImageUri".toUri()
    private var label_img: String = "label_img"
    private var score_img: String = "score_img"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener { analyzeImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Pick", "No media select")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
            binding.analyzeButton.visibility = View.VISIBLE
        }
    }

    private fun analyzeImage() {
        if (currentImageUri == "currentImageUri".toUri()) {
            showToast("Belum ada foto yang dipilih")
            return
        }
        val imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Log.d(TAG, "ShowImage: ${error}")

                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    results?.let {
                        val Result = it[0]
                        val label = Result.categories[0].label
                        val score = Result.categories[0].score

                        fun Float.formatToString(): String {
                            return String.format("%.2f%%", this * 100)
                        }

                        label_img = label
                        score_img = score.formatToString()
                    }
                }
            }
        )
        imageClassifierHelper.classifyStaticImage(currentImageUri)
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(IMG_CLASS, currentImageUri.toString())
        intent.putExtra(LABEL, label_img)
        intent.putExtra(SCORE, score_img)
        startActivity(intent)
    }


        private fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    companion object {
        const val IMG_CLASS = "IMAGE_CLASS"
        const val LABEL = "LABEL"
        const val SCORE = "SCORE"
    }
}
