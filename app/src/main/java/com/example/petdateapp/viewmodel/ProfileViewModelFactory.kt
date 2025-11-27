package com.example.petdateapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petdateapp.data.datastore.ProfileDataStore
import com.example.petdateapp.data.datastore.UserDataStore

class ProfileViewModelFactory(private val userDataStore: UserDataStore, private val profileDataStore: ProfileDataStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userDataStore, profileDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}