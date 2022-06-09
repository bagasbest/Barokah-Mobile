package com.project.barokahmobile.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private var binding : ActivityRegisterBinding ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.registerBtn?.setOnClickListener {
            formValidation()
        }

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

    }

    private fun formValidation() {
        val phone = binding?.phoneNumber?.text.toString().trim()
        val name = binding?.name?.text.toString().trim()
        val email = binding?.email?.text.toString().trim()
        val password = binding?.password?.text.toString().trim()
        val address = binding?.address?.text.toString().trim()

        if(phone.isEmpty()) {
            Toast.makeText(this, "Nomor Handphone tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (phone.length < 9 || phone.length > 13) {
            Toast.makeText(this, "Nomor Handphone terdiri dari 9 - 13 digit", Toast.LENGTH_SHORT).show()
        } else if (name.isEmpty()) {
            Toast.makeText(this, "Nama lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email harus mengandung @ dan diikuti .com", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (password.length < 6) {
            Toast.makeText(this, "Kata sandi minimum 6 karakter", Toast.LENGTH_SHORT).show()
        } else if (address.isEmpty()) {
            Toast.makeText(this, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {



        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}