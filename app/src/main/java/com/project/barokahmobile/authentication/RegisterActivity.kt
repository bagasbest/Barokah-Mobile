package com.project.barokahmobile.authentication

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshurawat.hasher.HashType
import com.himanshurawat.hasher.Hasher
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.ActivityRegisterBinding
import java.util.concurrent.TimeUnit


class RegisterActivity : AppCompatActivity() {

    private var binding: ActivityRegisterBinding? = null
    private var phone: String? = null
    private var getBackendOtp: String? = null
    private var timeLeftInMillis: Long = 0
    private var countDownTimer: CountDownTimer? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.registerBtn?.setOnClickListener {
            formValidation()
        }

        binding?.backButton?.setOnClickListener {
            if (phone != null) {
                countDownTimer?.cancel()
            }
            onBackPressed()
        }

        binding?.nextBtn?.setOnClickListener {
            phone = binding?.phoneNumber1?.text.toString().trim()
            if (phone == null) {
                Toast.makeText(this, "Nomor Handphone wajib diisi!", Toast.LENGTH_SHORT).show();
            } else if (phone.toString()[0] != '+') {
                Toast.makeText(this, "Nomor handphone wajib diawali +62", Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding?.layout1?.visibility = View.GONE
                binding?.layout2?.visibility = View.VISIBLE

                binding?.phoneTxt?.text = "Nomor Handphone anda adalah:\n$phone"

                sendOTP()
                moveToNext()
                countdownTimer()
            }
        }



        binding?.nextBtn2?.setOnClickListener { formValidationOTP() }

        binding?.countdownTimer?.setOnClickListener {
            sendOTP()
            countdownTimer()
        }
    }

    private fun moveToNext() {
        binding!!.otp1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().trim { it <= ' ' }.isNotEmpty()) {
                    binding!!.otp2.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding!!.otp2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().trim { it <= ' ' }.isNotEmpty()) {
                    binding!!.otp3.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding!!.otp3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().trim { it <= ' ' }.isNotEmpty()) {
                    binding!!.otp4.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding!!.otp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().trim { it <= ' ' }.isNotEmpty()) {
                    binding!!.otp5.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding!!.otp5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.toString().trim { it <= ' ' }.isNotEmpty()) {
                    binding!!.otp6.requestFocus()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }


    private fun formValidation() {
        val name = binding?.name?.text.toString().trim()
        val email = binding?.email?.text.toString().trim()
        val password = binding?.password?.text.toString().trim()
        val address = binding?.address?.text.toString().trim()

        if (phone == null) {
            Toast.makeText(this, "Nomor Handphone tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (name.isEmpty()) {
            Toast.makeText(this, "Nama lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email harus mengandung @ dan diikuti .com", Toast.LENGTH_SHORT)
                .show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else if (password.length < 6) {
            Toast.makeText(this, "Kata sandi minimum 6 karakter", Toast.LENGTH_SHORT).show()
        } else if (address.isEmpty()) {
            Toast.makeText(this, "Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {

            binding!!.progressBar.visibility = View.VISIBLE
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        saveDataToDB(phone!!, name, email, address, password)
                    } else {
                        binding!!.progressBar.visibility = View.GONE
                        try {
                            Log.w("TAG", "signInWithEmail:failed", task.exception)
                        } catch (error: FirebaseAuthUserCollisionException) {
                            showFailureDialog("This email already registered, pick another email")
                        } catch (error: Exception) {
                            Log.e("TAG", error.toString())
                        }
                    }
                }

        }
    }

    private fun saveDataToDB(
        phone: String,
        name: String,
        email: String,
        address: String,
        password: String,
    ) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val data = mapOf(
            "uid" to uid,
            "phone" to phone,
            "name" to name,
            "email" to email,
            "password" to Hasher.hash(password, HashType.SHA_256),
            "address" to address,
            "role" to "user",
        )

        FirebaseFirestore
            .getInstance()
            .collection("users")
            .document(uid)
            .set(data)
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    binding!!.progressBar.visibility = View.GONE
                    showSuccessDialog()
                } else {
                    binding!!.progressBar.visibility = View.GONE
                    showFailureDialog("Ups, koneksi internet anda sedang bermasalah, silahkan coba beberapa saat lagi!")
                }
            }
    }

    /// munculkan dialog ketika gagal registrasi
    private fun showFailureDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Gagal Registrasi")
            .setMessage(message)
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OK") { dialogInterface, _ -> dialogInterface.dismiss() }
            .show()
    }

    /// munculkan dialog ketika sukses registrasi
    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Success Register")
            .setMessage("Please Login")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OK") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }


    private fun formValidationOTP() {
        val et1 = binding!!.otp1.text.toString().trim()
        val et2 = binding!!.otp2.text.toString().trim()
        val et3 = binding!!.otp3.text.toString().trim()
        val et4 = binding!!.otp4.text.toString().trim()
        val et5 = binding!!.otp5.text.toString().trim()
        val et6 = binding!!.otp6.text.toString().trim()
        if (et1.isEmpty() || et2.isEmpty() || et3.isEmpty() || et4.isEmpty() || et5.isEmpty() || et6.isEmpty()) {
            Toast.makeText(this, "Kolom OTP tidak boleh kosong", Toast.LENGTH_SHORT).show()
        } else {
            val getUserOtp = et1 + et2 + et3 + et4 + et5 + et6
            if (getBackendOtp != null) {
                binding?.nextBtn2?.isEnabled = false
                val phoneAuthCredential = PhoneAuthProvider.getCredential(getBackendOtp!!, getUserOtp)
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                    .addOnCompleteListener { task: Task<AuthResult?> ->
                        if (task.isSuccessful) {
                            binding?.phoneNumber?.setText(phone)
                            binding?.phoneNumberView?.isEnabled = false
                            binding?.layout2?.visibility = View.GONE
                            binding?.layout3?.visibility = View.VISIBLE
                        } else {
                            binding?.nextBtn2?.isEnabled = true
                            Toast.makeText(
                                this@RegisterActivity,
                                "Masukkan Kode Verifikasi yang sesuai!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
            }
        }
    }

    private fun sendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phone!!,
            60,
            TimeUnit.SECONDS,
            this@RegisterActivity,
            object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {}
                override fun onVerificationFailed(e: FirebaseException) {}
                override fun onCodeSent(
                    backendOtp: String,
                    forceResendingToken: ForceResendingToken
                ) {
                    getBackendOtp = backendOtp
                    Toast.makeText(
                        this@RegisterActivity,
                        "Silahkan tunggu kode verifikasi!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }

    private fun countdownTimer() {
        timeLeftInMillis = 60000
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(l: Long) {
                timeLeftInMillis = l
                updateTimer()
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                binding!!.countdownTimer.isEnabled = true
                binding!!.countdownTimer.text = "Resend OTP"
            }
        }.start()
    }

    private fun updateTimer() {
        val second = timeLeftInMillis.toInt() / 1000
        val time = "00:$second"
        binding!!.countdownTimer.text = time
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (phone != null) {
                countDownTimer!!.cancel()
            }
            onBackPressed()
            return true
        }
        return false
    }


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}