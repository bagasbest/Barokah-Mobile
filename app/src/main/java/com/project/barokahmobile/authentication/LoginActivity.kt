package com.project.barokahmobile.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshurawat.hasher.HashType
import com.himanshurawat.hasher.Hasher
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.ActivityLoginBinding
import com.project.barokahmobile.ui.HomepageActivity
import com.project.barokahmobile.ui.HomepageAdminActivity


class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        autoLogin()

        binding?.loginBtn?.setOnClickListener {
            formValidation()
        }

        binding?.registerBtn?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun autoLogin() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            val prefs = getSharedPreferences(
                "role", Context.MODE_PRIVATE
            )
            val role = prefs.getString("role", "user")
            if (role == "user") {
                startActivity(Intent(this, HomepageActivity::class.java))

            } else {
                startActivity(Intent(this, HomepageAdminActivity::class.java))

            }
        }
    }

    private fun formValidation() {
        var phone = binding?.phoneNumber?.text.toString().trim()
        val password = binding?.password?.text.toString().trim()
        if (phone.isEmpty()) {
            Toast.makeText(this, "Nomor Handphone tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (phone.length < 9 || phone.length > 13) {
            Toast.makeText(this, "Nomor Handphone terdiri dari 9 - 13 digit", Toast.LENGTH_SHORT)
                .show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Kata sandi tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        } else {

            binding!!.progressBar.visibility = View.VISIBLE

            if (!phone.contains("+62")) {
                phone = "+62" + phone.substring(1)
                Log.e("taf", phone)
            }

            FirebaseFirestore
                .getInstance()
                .collection("users")
                .whereEqualTo("phone", phone)
                .whereEqualTo("password", Hasher.hash(password, HashType.SHA_256))
                .limit(1)
                .get()
                .addOnSuccessListener { documents ->

                    if (documents.size() > 0) {
                        for (document in documents) {
                            val email = document.data["email"].toString()
                            val pwd = document.data["password"].toString()
                            val role = document.data["role"].toString()

                            if (pwd == Hasher.hash(password, HashType.SHA_256)) {
                                FirebaseAuth.getInstance()
                                    .signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            binding?.progressBar?.visibility = View.GONE

                                            val prefs = getSharedPreferences(
                                                "role", Context.MODE_PRIVATE
                                            )
                                            prefs.edit().putString("role", role).apply()

                                            if (role == "user") {
                                                startActivity(
                                                    Intent(
                                                        this,
                                                        HomepageActivity::class.java
                                                    )
                                                )
                                            } else {
                                                startActivity(
                                                    Intent(
                                                        this,
                                                        HomepageAdminActivity::class.java
                                                    )
                                                )
                                            }
                                        } else {
                                            binding?.progressBar?.visibility = View.GONE
                                            showFailureDialog()
                                        }
                                    }
                            } else {
                                binding?.progressBar?.visibility = View.GONE
                                showFailureDialog()
                            }
                        }
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                        showFailureDialog()
                    }
                }
        }
    }

    /// jika gagal login, munculkan alert dialog gagal
    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Login")
            .setMessage("1. Periksa Nomor Handphone, pastikan anda sudah mendaftarkan pada aplikasi Barokah Mobile\n\n2. Pastikan koneksi internet anda tidak bermasalah!")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}