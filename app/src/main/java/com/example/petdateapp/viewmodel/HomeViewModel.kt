package com.example.petdateapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petdateapp.data.datastore.UserDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val userDataStore: UserDataStore) : ViewModel() {

    val ownerName: StateFlow<String> = userDataStore.userEmailFlow.map { email ->
        // Aquí asumimos que el nombre de usuario es la parte del email antes del @
        // Puedes ajustar esta lógica si guardas el nombre de otra forma
        email?.split("@")?.get(0) ?: ""
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )
}
