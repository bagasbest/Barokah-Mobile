package com.project.barokahmobile.ui.hpp_telur

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.FragmentDatabaseBinding
import com.project.barokahmobile.databinding.FragmentHppEggBinding


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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}