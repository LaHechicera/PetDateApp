package com.example.petdateapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petdateapp.data.PetRepository
import com.example.petdateapp.data.datastore.UserDataStore
import com.example.petdateapp.utils.UserManager

/**
 * Factory para crear instancias de HomeViewModel.
 */
class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            // CORRECCIÓN DEFINITIVA:
            // Crea PetRepository usando su constructor vacío, que es la forma correcta.
            val petRepository = PetRepository()

            // Crea el resto de dependencias que sí necesitan el Context.
            val userDataStore = UserDataStore(context.applicationContext)
            val userManager = UserManager(context.applicationContext)

            @Suppress("UNCHECKED_CAST")
            // Pasa las dependencias al ViewModel.
            return HomeViewModel(userDataStore, petRepository, userManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
