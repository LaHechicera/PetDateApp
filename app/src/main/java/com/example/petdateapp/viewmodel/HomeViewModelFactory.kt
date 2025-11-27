package com.example.petdateapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petdateapp.data.PetRepository
import com.example.petdateapp.data.datastore.UserDataStore

class HomeViewModelFactory(
    private val userDataStore: UserDataStore,
    private val petRepository: PetRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(userDataStore, petRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
