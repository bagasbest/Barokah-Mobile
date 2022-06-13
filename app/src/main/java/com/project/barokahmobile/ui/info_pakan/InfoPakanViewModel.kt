package com.project.barokahmobile.ui.info_pakan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class InfoPakanViewModel : ViewModel() {

    private val infoList = MutableLiveData<ArrayList<InfoPakanModel>>()
    private val listItems = ArrayList<InfoPakanModel>()
    private val TAG = InfoPakanViewModel::class.java.simpleName

    fun setListInfo() {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("info")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = InfoPakanModel()
                        model.name = document.data["name"].toString()
                        model.description = document.data["description"].toString()
                        model.date = document.data["date"].toString()
                        model.image = document.data["image"].toString()
                        model.uid = document.data["uid"].toString()

                        listItems.add(model)
                    }
                    infoList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getInfo() : LiveData<ArrayList<InfoPakanModel>> {
        return infoList
    }


}