package com.project.barokahmobile.ui.info_pakan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.FragmentHppEggBinding
import com.project.barokahmobile.databinding.FragmentInfoPakanBinding


class InfoPakanFragment : Fragment() {

    private var _binding: FragmentInfoPakanBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInfoPakanBinding.inflate(inflater, container, false)

        Glide.with(requireContext())
            .load(R.drawable.info_pakan)
            .into(binding.image)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}