package com.project.barokahmobile.ui.database_user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class DatabaseViewModel : ViewModel() {

    private val userList = MutableLiveData<ArrayList<DatabaseModel>>()
    private val listItems = ArrayList<DatabaseModel>()
    private val TAG = DatabaseViewModel::class.java.simpleName

    fun setListUser() {
        listItems.clear()


        try {
            FirebaseFirestore.getInstance().collection("users")
                .whereEqualTo("role", "user")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val model = DatabaseModel()
                        model.email = document.data["email"].toString()
                        model.name = document.data["name"].toString()
                        model.password = document.data["password"].toString()
                        model.role = document.data["role"].toString()
                        model.phone = document.data["phone"].toString()
                        model.address = document.data["address"].toString()
                        model.uid = document.data["uid"].toString()
                        model.population = document.data["population"].toString()

                        listItems.add(model)
                    }
                    userList.postValue(listItems)
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    fun getUser() : LiveData<ArrayList<DatabaseModel>> {
        return userList
    }

}