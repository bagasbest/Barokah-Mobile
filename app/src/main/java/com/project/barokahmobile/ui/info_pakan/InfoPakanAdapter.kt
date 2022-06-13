package com.project.barokahmobile.ui.info_pakan

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.project.barokahmobile.databinding.ItemInfoBinding

class InfoPakanAdapter : RecyclerView.Adapter<InfoPakanAdapter.ViewHolder>() {


    private val infoList = ArrayList<InfoPakanModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(items: ArrayList<InfoPakanModel>) {
        infoList.clear()
        infoList.addAll(items)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: InfoPakanModel) {
            with(binding) {

                Glide.with(itemView.context)
                    .load(model.image)
                    .into(image)

                name.text = model.name
                description.text = model.description
                date.text = model.date


                cv.setOnClickListener {
                  val intent = Intent(itemView.context, InfoPakanDetailActivity::class.java)
                    intent.putExtra(InfoPakanDetailActivity.EXTRA_DATA, model)
                    itemView.context.startActivity(intent)
                }

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(infoList[position])
    }

    override fun getItemCount(): Int = infoList.size
}