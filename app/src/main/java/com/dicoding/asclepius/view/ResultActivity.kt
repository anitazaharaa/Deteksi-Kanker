package com.dicoding.asclepius.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val image = intent.extras?.getString(MainActivity.IMG_CLASS).toString()
        val label = intent.extras?.getString(MainActivity.LABEL).toString()
        val score = intent.extras?.getString(MainActivity.SCORE).toString()

        binding.resultImage.setImageURI(image.toUri())
        binding.rsltLabel.text = "Klasifikasi: ${label}"
        binding.rsltScore.text = "Hasil Nilai Klasifikasi: ${score}"

    }
}