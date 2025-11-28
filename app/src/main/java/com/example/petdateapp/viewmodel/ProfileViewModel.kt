package com.example.petdateapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petdateapp.data.datastore.ProfileDataStore
import com.example.petdateapp.data.datastore.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class ProfileState(
    val nombre: String = "",
    val telefono: String = "",
    val genero: String = "",
    val imagenUri: String? = null,
    val email: String = ""
)

class ProfileViewModel(
    private val userDataStore: UserDataStore,
    private val profileDataStore: ProfileDataStore
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        viewModelScope.launch {
            userDataStore.userEmailFlow.flatMapLatest { email ->
                if (email != null) {
                    profileDataStore.getProfileData(email).map { profileData ->
                        ProfileState(
                            nombre = profileData?.nombre ?: "",
                            telefono = profileData?.telefono ?: "",
                            genero = profileData?.genero ?: "",
                            imagenUri = profileData?.imagenUri,
                            email = email
                        )
                    }
                } else {
                    MutableStateFlow(ProfileState())
                }
            }.collect { state ->
                _profileState.value = state
            }
        }
    }

    fun updateProfile(nombre: String, telefono: String, genero: String) {
        viewModelScope.launch {
            val email = userDataStore.userEmailFlow.first()
            if (email != null) {
                profileDataStore.saveProfileData(
                    email = email,
                    nombre = nombre,
                    telefono = telefono,
                    genero = genero,
                    imagenUri = _profileState.value.imagenUri
                )
            }
        }
    }

    fun updateProfileImage(imagePath: String) {
        _profileState.value = _profileState.value.copy(imagenUri = imagePath)
        viewModelScope.launch {
            val email = userDataStore.userEmailFlow.first()
            if (email != null) {
                val currentData = _profileState.value
                profileDataStore.saveProfileData(
                    email = email,
                    nombre = currentData.nombre,
                    telefono = currentData.telefono,
                    genero = currentData.genero,
                    imagenUri = imagePath
                )
            }
        }
    }
}