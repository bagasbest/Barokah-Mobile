package com.project.barokahmobile.ui.menghitung_pakan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.project.barokahmobile.R
import com.project.barokahmobile.authentication.LoginActivity
import com.project.barokahmobile.databinding.FragmentCalculatePakanBinding
import com.project.barokahmobile.databinding.FragmentInfoPakanBinding


class CalculatePakanFragment : Fragment() {


    private var _binding: FragmentCalculatePakanBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCalculatePakanBinding.inflate(inflater, container, false)

        Glide.with(requireContext())
            .load(R.drawable.pakan)
            .into(binding.image)

        return binding.root
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}