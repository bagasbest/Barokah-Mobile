package com.project.barokahmobile.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.project.barokahmobile.databinding.ActivityLoginBinding
import com.project.barokahmobile.ui.HomepageActivity

class LoginActivity : AppCompatActivity() {

    private var binding : ActivityLoginBinding ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.loginBtn?.setOnClickListener {
            formValidation()
        }

        binding?.registerBtn?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun formValidation() {
        val phone = binding?.phoneNumber?.text.toString().trim()
        if(phone.isEmpty()) {
            Toast.makeText(this, "Nomor Handphone tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (phone.length < 9 || phone.length > 13) {
            Toast.makeText(this, "Nomor Handphone terdiri dari 9 - 13 digit", Toast.LENGTH_SHORT).show()
        } else {

            startActivity(Intent(this, HomepageActivity::class.java))

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}