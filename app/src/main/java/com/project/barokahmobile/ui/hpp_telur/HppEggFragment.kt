package com.project.barokahmobile.ui.hpp_telur

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.FragmentHppEggBinding
import java.text.DecimalFormat


class HppEggFragment : Fragment() {

    private var _binding: FragmentHppEggBinding? = null
    private val binding get() = _binding!!

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitBtn.setOnClickListener {
            formValidation()
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
        val price2Tv: TextView
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_result)

        title = dialog.findViewById(R.id.editText)
        dismissBtn = dialog.findViewById(R.id.dismissBtn)
        hppLL = dialog.findViewById(R.id.hpp)
        combineFeedTv = dialog.findViewById(R.id.combineFeed)
        eggProductionTv = dialog.findViewById(R.id.eggProduction)
        feedPriceTv = dialog.findViewById(R.id.feedPrice)
        otherPriceTv = dialog.findViewById(R.id.otherPrice)
        price2Tv = dialog.findViewById(R.id.price2)

        hppLL.visibility = View.VISIBLE

        val formatter = DecimalFormat("#,###")
        title.text = "Hasil Harga Pokok Produksi (HPP)\nTelur per Kilogram"
        combineFeedTv.text = "Jumlah campuran pakan dalam 1 hari : $combineFeed kg"
        eggProductionTv.text = "Jumlah telur yang dihasilkan 1 hari : $eggProduction kg"
        feedPriceTv.text = "Harga campuran pakan : $feedPrice per kg"
        otherPriceTv.text = "Biaya lain - lain : $otherPrice per kg"


        val fcr : Double = combineFeed / eggProduction
        val formula : Double = ((feedPrice * fcr) + otherPrice)
        price2Tv.text = "Rp${formatter.format(formula)} per kilogram"


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