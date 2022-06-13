package com.project.barokahmobile.ui.info_pakan

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.ActivityInfoPakanAddBinding
import com.project.barokahmobile.utils.Resource
import java.text.SimpleDateFormat
import java.util.*

class InfoPakanAddActivity : AppCompatActivity() {

    private var binding : ActivityInfoPakanAddBinding?= null
    private var infoImage: String? = null
    private val REQUEST_IMAGE_GALLERY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoPakanAddBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.imageHint?.setOnClickListener {
            imagePicker()
        }

        binding?.uploadBtn?.setOnClickListener {
            formValidation()
        }

        binding?.image?.setOnClickListener {
            imagePicker()
        }
    }

    private fun formValidation() {
        val name = binding?.name?.text.toString().trim()
        val description = binding?.description?.text.toString().trim()
        if(name.isEmpty()) {
            Toast.makeText(this, "Nama bahan tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        } else if (description.isEmpty()) {
            Toast.makeText(this, "Deskripsi tidak boleh kosong!", Toast.LENGTH_SHORT).show()
        } else {

            binding?.progressBar?.visibility = View.VISIBLE
            val uid = System.currentTimeMillis().toString()

            val c = Calendar.getInstance()
            val df = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val formattedDate: String = df.format(c.time)

            val data = HashMap<String, Any>()
            data["uid"] = uid
            data["name"] = name
            data["description"] = description
            data["date"] = formattedDate
            if(infoImage == null) {
                data["image"] = Resource.defaultInfoImage
            } else {
                data["image"] = infoImage!!
            }

            FirebaseFirestore
                .getInstance()
                .collection("info")
                .document(uid)
                .set(data)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        binding?.progressBar?.visibility = View.GONE
                        showSuccessDialog()
                    } else {
                        binding?.progressBar?.visibility = View.GONE
                        showFailureDialog()
                    }
                }

        }

    }

    private fun showFailureDialog() {
        AlertDialog.Builder(this)
            .setTitle("Gagal Mengunggah Informasi")
            .setMessage("Ups, koneksi internet anda tidak stabil, silahkan coba lagi nanti!")
            .setIcon(R.drawable.ic_baseline_clear_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Sukses Mengunggah Informasi")
            .setMessage("Informasi ini akan tampil sesaat lagi")
            .setIcon(R.drawable.ic_baseline_check_circle_outline_24)
            .setPositiveButton("OKE") { dialogInterface, _ ->
                dialogInterface.dismiss()
                onBackPressed()
            }
            .show()
    }


    private fun imagePicker () {
        ImagePicker.with(this)
            .galleryOnly()
            .compress(1024)
            .start(REQUEST_IMAGE_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY) {
                uploadArticleDp(data?.data)
            }
        }
    }


    /// fungsi untuk mengupload foto kedalam cloud storage
    @SuppressLint("SetTextI18n")
    private fun uploadArticleDp(data: Uri?) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Silahkan tunggu hingga proses selesai...")
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
        val imageFileName = "info/image_" + System.currentTimeMillis() + ".png"
        mStorageRef.child(imageFileName).putFile(data!!)
            .addOnSuccessListener {
                mStorageRef.child(imageFileName).downloadUrl
                    .addOnSuccessListener { uri: Uri ->
                        mProgressDialog.dismiss()
                        infoImage = uri.toString()
                        Glide.with(this)
                            .load(infoImage)
                            .into(binding!!.image)


                        binding?.imageHint?.visibility = View.GONE
                    }
                    .addOnFailureListener { e: Exception ->
                        mProgressDialog.dismiss()
                        Toast.makeText(
                            this,
                            "Gagal mengunggah gambar",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("imageDp: ", e.toString())
                    }
            }
            .addOnFailureListener { e: Exception ->
                mProgressDialog.dismiss()
                Toast.makeText(
                    this,
                    "Gagal mengunggah gambar",
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.d("imageDp: ", e.toString())
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}