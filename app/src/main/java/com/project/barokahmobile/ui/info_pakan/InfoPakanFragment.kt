package com.project.barokahmobile.ui.info_pakan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.FragmentInfoPakanBinding
import com.project.barokahmobile.utils.Resource


class InfoPakanFragment : Fragment() {

    private var _binding: FragmentInfoPakanBinding? = null
    private val binding get() = _binding!!
    private var adapter : InfoPakanAdapter? = null

    override fun onResume() {
        super.onResume()
        initRecyclerView()
        initViewModel()
    }

    private fun initRecyclerView() {
        binding.rvCustom.layoutManager = LinearLayoutManager(activity)
        adapter = InfoPakanAdapter()
        binding.rvCustom.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[InfoPakanViewModel::class.java]


        binding.progressBar4.visibility = View.VISIBLE
        viewModel.setListInfo()
        viewModel.getInfo().observe(viewLifecycleOwner) { infoList ->
            if (infoList.size > 0) {
                adapter!!.setData(infoList)
                binding.noData.visibility = View.GONE
            } else {
                binding.noData.visibility = View.VISIBLE
            }
            binding.progressBar4.visibility = View.GONE
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentInfoPakanBinding.inflate(inflater, container, false)
        binding.rvCustom.isNestedScrollingEnabled = false

        checkRole()

        Glide.with(requireContext())
            .load(R.drawable.info_pakan)
            .into(binding.image)

        return binding.root

    }

    private fun checkRole() {
        val email = FirebaseAuth.getInstance().currentUser!!.email
        if(email == Resource.adminEmail) {
            binding.add.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.add.setOnClickListener {
            startActivity(Intent(activity, InfoPakanAddActivity::class.java))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}