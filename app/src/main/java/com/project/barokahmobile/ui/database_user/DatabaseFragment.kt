package com.project.barokahmobile.ui.database_user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.barokahmobile.databinding.FragmentDatabaseBinding


class DatabaseFragment : Fragment() {

    private var _binding: FragmentDatabaseBinding? = null
    private val binding get() = _binding!!
    private var adapter : DatabaseAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDatabaseBinding.inflate(inflater, container, false)

        initRecyclerView()
        initViewModel()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = DatabaseAdapter()
        binding.recyclerView.adapter = adapter
    }

    private fun initViewModel() {
        val viewModel = ViewModelProvider(this)[DatabaseViewModel::class.java]


        binding.progressBar.visibility = View.VISIBLE
        viewModel.setListUser()
        viewModel.getUser().observe(viewLifecycleOwner) { userList ->
            if (userList.size > 0) {
                adapter!!.setData(userList)
                binding.noData.visibility = View.GONE
            } else {
                binding.noData.visibility = View.VISIBLE
            }
            binding.progressBar.visibility = View.GONE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}