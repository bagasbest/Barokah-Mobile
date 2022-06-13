package com.project.barokahmobile.ui.info_pakan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.ActivityInfoPakanDetailBinding
import com.project.barokahmobile.utils.Resource


class InfoPakanDetailActivity : AppCompatActivity() {

    private var binding : ActivityInfoPakanDetailBinding?= null
    private var model : InfoPakanModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoPakanDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        model = intent.getParcelableExtra(EXTRA_DATA)
        checkRole()
        initView()

        binding?.backButton?.setOnClickListener {
            onBackPressed()
        }

        binding?.edit?.setOnClickListener {
            val intent = Intent(this, InfoPakanEditActivity::class.java)
            intent.putExtra(InfoPakanEditActivity.EXTRA_DATA, model)
            startActivityForResult(intent, REQUEST_CODE_INTENT)
        }
        binding?.delete?.setOnClickListener {
            showConfirmDialog()
        }
    }

    private fun showConfirmDialog() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi Menghapus Informasi")
            .setMessage("Apakah anda yakin ingin menghapus informasi ini ?")
            .setIcon(R.drawable.ic_baseline_warning_24)
            .setPositiveButton("YA") { dialogInterface, _ ->
                dialogInterface.dismiss()
                deleteInformation()
            }
            .setNegativeButton("TIDAK", null)
            .show()
    }

    private fun deleteInformation() {
        FirebaseFirestore
            .getInstance()
            .collection("info")
            .document(model?.uid!!)
            .delete()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Toast.makeText(this, "Berhasil menghapus informasi", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                } else {
                    Toast.makeText(this, "Gagal menghapus informasi", Toast.LENGTH_SHORT).show()
                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        Glide.with(this)
            .load(model?.image)
            .into(binding!!.image)

        binding?.name?.text = model?.name
        binding?.date?.text = "Tanggal : ${model?.date}"
        binding?.description?.text = model?.description
    }

    private fun checkRole() {
        val email = FirebaseAuth.getInstance().currentUser!!.email
        if(email == Resource.adminEmail) {
            binding?.edit?.visibility = View.VISIBLE
            binding?.delete?.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_INTENT) {
            if (resultCode == RESULT_OK) {
                model = data?.getParcelableExtra("result")
                initView()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object {
        const val EXTRA_DATA = "data"
        const val REQUEST_CODE_INTENT = 1
    }
}