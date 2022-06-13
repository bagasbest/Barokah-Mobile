package com.project.barokahmobile.ui.hpp_telur

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.FragmentHppEggBinding
import java.text.DecimalFormat


class HppEggFragment : Fragment() {

    private var _binding: FragmentHppEggBinding? = null
    private val binding get() = _binding!!
    private var combineFeed = 0.0
    private var eggProduction = 0.0
    private var fcr = 0.0
    private var calculationPakanResult = 0.0

    override fun onResume() {
        super.onResume()
        checkHpp()
    }

    private fun checkHpp() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("hpp")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if(it.exists()) {
                    val combineFeed = "" + it.data!!["combineFeed"]
                    val eggProduction = "" + it.data!!["eggProduction"]
                    val feedPrice = "" + it.data!!["feedPrice"]
                    val otherPrice = "" + it.data!!["otherPrice"]

                    val f1 = String.format("%.0f", combineFeed.toDouble())
                    val f2 = String.format("%.0f", eggProduction.toDouble())
                    val f3 = String.format("%.0f", feedPrice.toDouble())
                    val f4 = String.format("%.0f", otherPrice.toDouble())


                    binding.combineFeed.setText(f1)
                    binding.eggProduction.setText(f2)
                    binding.feedPrice.setText(f3)
                    binding.otherPrice.setText(f4)
                } else {
                    getCalculationPakanResult()
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHppEggBinding.inflate(inflater, container, false)

        Glide.with(requireContext())
            .load(R.drawable.hpp_telur_new)
            .into(binding.image)

        return binding.root

    }

    private fun getCalculationPakanResult() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("calculate_pakan")
            .document(uid)
            .get()
            .addOnSuccessListener {
                if(it.exists()) {
                    val result = "" + it.data!!["result"]
                    binding.feedPrice.setText(result)
                    calculationPakanResult = result.toDouble()
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitBtn.setOnClickListener {
            formValidation()
        }

        binding.combineFeed.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isNotEmpty()) {
                    combineFeed = p0.toString().toDouble()
                    fcr = combineFeed / eggProduction
                    binding.fcr.text = "FCR = ${String.format("%.2f", fcr)}"

                } else {
                    combineFeed = 0.0
                }
                checkFcr()

            }

        })

        binding.eggProduction.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isNotEmpty()) {
                    eggProduction = p0.toString().toDouble()
                    fcr = combineFeed / eggProduction
                    binding.fcr.text = "FCR = ${String.format("%.2f", fcr)}"
                } else {
                    eggProduction = 0.0
                }
                checkFcr()

            }
        })
    }

    private fun checkFcr() {
        if(combineFeed > 0.0 && eggProduction > 0.0) {
            binding.content.visibility = View.VISIBLE
        }
    }

    private fun formValidation() {
        val combineFeed = binding.combineFeed.text.toString().trim()
        val eggProduction = binding.eggProduction.text.toString().trim()
        val feedPrice = binding.feedPrice.text.toString().trim()
        val otherPrice = binding.otherPrice.text.toString().trim()

        if (combineFeed.isEmpty() || combineFeed.toInt() <= 0) {
            Toast.makeText(activity, "Jumlah campuran pakan dalam 1 hari, minimal 1 kg", Toast.LENGTH_SHORT).show()
        } else if (eggProduction.isEmpty() || eggProduction.toInt() <= 0) {
            Toast.makeText(activity, "Jumlah telur yang dihasilkan 1 hari, minimal 1 kg", Toast.LENGTH_SHORT).show()
        } else if (feedPrice.isEmpty() || feedPrice.toInt() < 0) {
            Toast.makeText(activity, "Harga campuran pakan per kg tidak boleh kosong dan negatif", Toast.LENGTH_SHORT).show()
        } else if (otherPrice.isEmpty() || otherPrice.toLong() < 0) {
            Toast.makeText(
                activity,
                "Biaya lain - lain tidak boleh kosong dan negatif",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            showDialogPopupResult(
                combineFeed.toDouble(),
                eggProduction.toDouble(),
                feedPrice.toDouble(),
                otherPrice.toDouble(),
            )

        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogPopupResult(
        combineFeed: Double,
        eggProduction: Double,
        feedPrice: Double,
        otherPrice: Double,
    ) {

        val title: TextView
        val dismissBtn: Button
        val hppLL: LinearLayout
        val combineFeedTv: TextView
        val eggProductionTv: TextView
        val feedPriceTv: TextView
        val otherPriceTv: TextView
        val fcrTv: TextView
        val price2Tv: TextView
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_result)

        title = dialog.findViewById(R.id.editText)
        dismissBtn = dialog.findViewById(R.id.dismissBtn)
        hppLL = dialog.findViewById(R.id.hpp)
        combineFeedTv = dialog.findViewById(R.id.combineFeed)
        eggProductionTv = dialog.findViewById(R.id.eggProduction)
        fcrTv = dialog.findViewById(R.id.fcr)
        feedPriceTv = dialog.findViewById(R.id.feedPrice)
        otherPriceTv = dialog.findViewById(R.id.otherPrice)
        price2Tv = dialog.findViewById(R.id.price2)

        hppLL.visibility = View.VISIBLE

        val formatter = DecimalFormat("#,###")
        title.text = "Hasil Harga Pokok Produksi (HPP)\nTelur per Kilogram"
        combineFeedTv.text = "Jumlah campuran pakan dalam 1 hari : $combineFeed kg"
        eggProductionTv.text = "Jumlah telur yang dihasilkan 1 hari : $eggProduction kg"
        fcrTv.text = "FCR : ${String.format("%.2f", fcr)}"
        feedPriceTv.text = "Harga campuran pakan : Rp$feedPrice per kg"
        otherPriceTv.text = "Biaya lain - lain : Rp$otherPrice"

        val formula : Double = ((feedPrice * fcr) + otherPrice)
        price2Tv.text = "Rp${formatter.format(formula)} per kilogram"

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val result = String.format("%.0f", formula)
        val data = mapOf(
            "uid" to uid,
            "combineFeed" to combineFeed.toString(),
            "eggProduction" to eggProduction.toString(),
            "feedPrice" to feedPrice.toString(),
            "otherPrice" to otherPrice.toString(),
            "result" to result,
        )

        FirebaseFirestore
            .getInstance()
            .collection("hpp")
            .document(uid)
            .set(data)


        dismissBtn.setOnClickListener {
            dialog.dismiss()
        }


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}