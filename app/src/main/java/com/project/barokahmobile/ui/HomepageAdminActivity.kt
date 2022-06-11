package com.project.barokahmobile.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ismaeldivita.chipnavigation.ChipNavigationBar
import com.project.barokahmobile.R
import com.project.barokahmobile.databinding.ActivityHomepageAdminBinding
import com.project.barokahmobile.ui.database_user.DatabaseFragment
import com.project.barokahmobile.ui.hpp_telur.HppEggFragment
import com.project.barokahmobile.ui.info_pakan.InfoPakanFragment
import com.project.barokahmobile.ui.menghitung_pakan.CalculatePakanFragment

class HomepageAdminActivity : AppCompatActivity() {

    private var binding : ActivityHomepageAdminBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageAdminBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navView = findViewById<ChipNavigationBar>(R.id.nav_view)

        navView.setItemSelected(R.id.navigation_calculate, true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CalculatePakanFragment()).commit()


        bottomMenu(navView)
    }

    @SuppressLint("NonConstantResourceId")
    private fun bottomMenu(navView: ChipNavigationBar) {
        navView.setOnItemSelectedListener { i: Int ->
            var fragment: Fragment? = null
            when (i) {
                R.id.navigation_calculate -> fragment = CalculatePakanFragment()
                R.id.navigation_egg -> fragment = HppEggFragment()
                R.id.navigation_info -> fragment = InfoPakanFragment()
                R.id.navigation_database -> fragment = DatabaseFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    fragment!!
                ).commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}