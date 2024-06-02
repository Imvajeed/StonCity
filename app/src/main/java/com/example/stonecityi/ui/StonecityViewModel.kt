package com.example.stonecityi.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stonecityi.StoreData
import com.example.stonecityi.roomdatabase.UserData
import com.example.stonecityi.roomdatabase.UserRepository
import com.example.stonecityi.stonecityRecycler.MyStoreRecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class StonecityViewModel(private val repo : UserRepository,val context: Context):ViewModel() {

    //store


    fun getUserInfo():UserData{
        return repo.getUserInformation()
    }
    //firebase
    lateinit var auth : FirebaseAuth
    val db = FirebaseFirestore.getInstance()
    val collectionReference = db.collection("Users")


    fun inserData(data: UserData)= viewModelScope.launch {
        repo.insertData(data)
    }
    fun findUser(id : String):Int{
        return repo.findUser(id)
    }
    fun addUserInfoToDb(data: UserData){

        auth = Firebase.auth
        inserData(data)

    }
    fun deleteAll(){
        repo.deleteAll()
    }
    fun deleteUser() = viewModelScope.launch{
        repo.deleteData(getUserInfo())
    }
    fun updateData(data: UserData) = viewModelScope.launch{
        repo.updataData(data)
    }




}