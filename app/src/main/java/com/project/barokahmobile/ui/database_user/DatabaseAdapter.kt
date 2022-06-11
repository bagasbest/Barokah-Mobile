package com.project.barokahmobile.ui.database_user

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.project.barokahmobile.databinding.ItemDatabaseUserBinding


class DatabaseAdapter : RecyclerView.Adapter<DatabaseAdapter.ViewHolder>() {


    private val userList = ArrayList<DatabaseModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<DatabaseModel>) {
        userList.clear()
        userList.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemDatabaseUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: DatabaseModel) {
            with(binding) {

                name.text = model.name
                binding.phoneNumber.text = "No.Handphone: ${model.phone}"
                binding.email.text = "Email : ${model.email}"
                binding.address.text = "Alamat: ${model.address}"


                waBtn.setOnClickListener {
                    val number = model.phone
                    val installed: Boolean = isAppInstalled(itemView.context)

                    if(installed) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("https://api.whatsapp.com/send?phone=$number")
                        itemView.context.startActivity(intent)
                    } else {
                        Toast.makeText(itemView.context, "Whatsapp tidak terinstal di handphone anda!", Toast.LENGTH_SHORT).show()
                    }
                }

                phoneBtn.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${model.phone}")
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    private fun isAppInstalled(context: Context): Boolean {
        val pm: PackageManager = context.packageManager
        val appInstalled: Boolean = try {
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return appInstalled
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDatabaseUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int = userList.size
}