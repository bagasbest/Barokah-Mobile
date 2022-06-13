package com.project.barokahmobile.ui.menghitung_pakan

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.barokahmobile.R
import com.project.barokahmobile.authentication.LoginActivity
import com.project.barokahmobile.databinding.FragmentCalculatePakanBinding
import java.text.DecimalFormat


class CalculatePakanFragment : Fragment() {

    private var _binding: FragmentCalculatePakanBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()
        checkIfCalculated()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCalculatePakanBinding.inflate(inflater, container, false)

        Glide.with(requireContext())
            .load(R.drawable.pakan)
            .into(binding.image)


        return binding.root
    }

    private fun checkIfCalculated() {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseFirestore
            .getInstance()
            .collection("calculate_pakan")
            .document(uid)
            .get()
            .addOnSuccessListener {

                if(it.exists()) {
                    val concentrate = "" + it.data!!["concentrate"]
                    val corn = "" + it.data!!["corn"]
                    val katul = "" + it.data!!["katul"]
                    val concentratePrice = "" + it.data!!["concentratePrice"]
                    val cornPrice = "" + it.data!!["cornPrice"]
                    val katulPrice = "" + it.data!!["katulPrice"]

                    val f1 = String.format("%.0f", concentrate.toDouble())
                    val f2 = String.format("%.0f", corn.toDouble())
                    val f3 = String.format("%.0f", katul.toDouble())
                    val f4 = String.format("%.0f", concentratePrice.toDouble())
                    val f5 = String.format("%.0f", cornPrice.toDouble())
                    val f6 = String.format("%.0f", katulPrice.toDouble())


                    binding.concentrate.setText(f1)
                    binding.corn.setText(f2)
                    binding.katul.setText(f3)
                    binding.concentratePrice.setText(f4)
                    binding.cornPrice.setText(f5)
                    binding.katulPrice.setText(f6)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            activity?.finish()
        }

        binding.submitBtn.setOnClickListener {
            formValidation()
        }

        binding.location.setOnClickListener {
            startActivity(Intent(activity, LocationActivity::class.java))
        }
    }

    private fun formValidation() {
        val concentrate = binding.concentrate.text.toString().trim()
        val corn = binding.corn.text.toString().trim()
        val katul = binding.katul.text.toString().trim()
        val concentratePrice = binding.concentratePrice.text.toString().trim()
        val cornPrice = binding.cornPrice.text.toString().trim()
        val katulPrice = binding.katulPrice.text.toString().trim()

        if (concentrate.isEmpty() || concentrate.toInt() <= 0) {
            Toast.makeText(activity, "Jumlah konsentrat minimal 1 kg", Toast.LENGTH_SHORT).show()
        } else if (corn.isEmpty() || corn.toInt() <= 0) {
            Toast.makeText(activity, "Jumlah Jagung minimal 1 kg", Toast.LENGTH_SHORT).show()
        } else if (katul.isEmpty() || katul.toInt() <= 0) {
            Toast.makeText(activity, "Jumlah Katul minimal 1 kg", Toast.LENGTH_SHORT).show()
        } else if (concentratePrice.isEmpty() || concentratePrice.toLong() < 0) {
            Toast.makeText(
                activity,
                "Harga konsentrat per kg tidak boleh kosong dan negatif",
                Toast.LENGTH_SHORT
            ).show()
        } else if (cornPrice.isEmpty() || cornPrice.toLong() < 0) {
            Toast.makeText(activity, "Harga Jagung per kg tidak boleh kosong dan negatif", Toast.LENGTH_SHORT)
                .show()
        } else if (katulPrice.isEmpty() || katulPrice.toLong() < 0) {
            Toast.makeText(activity, "Harga katul per kg tidak boleh kosong dan negatif", Toast.LENGTH_SHORT)
                .show()
        } else {

            showDialogPopupResult(
                concentrate.toDouble(),
                corn.toDouble(),
                katul.toDouble(),
                concentratePrice.toDouble(),
                cornPrice.toDouble(),
                katulPrice.toDouble(),
            )

        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDialogPopupResult(
        concentrate: Double,
        corn: Double,
        katul: Double,
        concentratePrice: Double,
        cornPrice: Double,
        katulPrice: Double
    ) {

        val title: TextView
        val dismissBtn: Button
        val calculateLL: LinearLayout
        val concentrateTotalTv: TextView
        val cornTotalTv: TextView
        val katulTotalTv: TextView
        val concentratePriceTv: TextView
        val cornPriceTv: TextView
        val katulPriceTv: TextView
        val price1Tv: TextView
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.popup_result)

        title = dialog.findViewById(R.id.editText)
        dismissBtn = dialog.findViewById(R.id.dismissBtn)
        calculateLL = dialog.findViewById(R.id.calculate)
        concentrateTotalTv = dialog.findViewById(R.id.concentrateTotal)
        cornTotalTv = dialog.findViewById(R.id.cornTotal)
        katulTotalTv = dialog.findViewById(R.id.katulTotal)
        concentratePriceTv = dialog.findViewById(R.id.concentratePrice)
        cornPriceTv = dialog.findViewById(R.id.cornPrice)
        katulPriceTv = dialog.findViewById(R.id.katulPrice)
        price1Tv = dialog.findViewById(R.id.price)

        calculateLL.visibility = View.VISIBLE

        val formatter = DecimalFormat("#,###")
        title.text = "Hasil Harga Campuran\nPakan Ayam per kilogram"
        concentrateTotalTv.text = "Jumlah Konsentrat : $concentrate kg"
        cornTotalTv.text = "Jumlah jagung : $corn kg"
        katulTotalTv.text = "Jumlah katul : $katul kg"
        concentratePriceTv.text = "Harga konsentrat : $concentratePrice per kg"
        cornPriceTv.text = "Harga jagung : $concentratePrice per kg"
        katulPriceTv.text = "Harga katul : $concentratePrice per kg"


        val formula : Double = ((concentrate * concentratePrice) + (corn * cornPrice) + (katul * katulPrice)) / (concentrate + corn + katul)
        price1Tv.text = "Rp${formatter.format(formula)}"

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val result = String.format("%.0f", formula)
        val data = mapOf(
            "uid" to uid,
            "concentrate" to concentrate.toString(),
            "corn" to corn.toString(),
            "katul" to katul.toString(),
            "concentratePrice" to concentratePrice.toString(),
            "cornPrice" to cornPrice.toString(),
            "katulPrice" to katulPrice.toString(),
            "result" to result,
        )

        FirebaseFirestore
            .getInstance()
            .collection("calculate_pakan")
            .document(uid)
            .set(data)

        dismissBtn.setOnClickListener {
            dialog.dismiss()
        }


        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}