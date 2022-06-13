package com.project.barokahmobile.ui.info_pakan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.barokahmobile.databinding.ActivityInfoPakanDetailBinding

class InfoPakanDetailActivity : AppCompatActivity() {

    private var binding : ActivityInfoPakanDetailBinding?= null
    private var model : InfoPakanModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoPakanDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        model = intent.getParcelableExtra(EXTRA_DATA)


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DATA = "data"
    }
}