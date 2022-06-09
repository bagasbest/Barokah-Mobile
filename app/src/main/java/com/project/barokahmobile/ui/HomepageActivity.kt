package com.project.barokahmobile.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.ActivityHomepageBinding

class HomepageActivity : AppCompatActivity() {

    private var binding : ActivityHomepageBinding ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}