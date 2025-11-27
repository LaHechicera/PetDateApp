package com.example.petdateapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petdateapp.data.datastore.ProfileData
import com.example.petdateapp.data.datastore.ProfileDataStore
import com.example.petdateapp.data.datastore.UserDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class ProfileState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val gender: String = "",
    val imageUri: String = ""
)

class ProfileViewModel(
    private val userDataStore: UserDataStore,
    private val profileDataStore: ProfileDataStore
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    init {
        viewModelScope.launch {
            // Combine flows to get user email and profile data
            userDataStore.userEmailFlow.combine(profileDataStore.profileDataFlow) { email, profileData ->
                ProfileState(
                    name = profileData.name,
                    email = email ?: "",
                    phone = profileData.phone,
                    gender = profileData.gender,
                    imageUri = profileData.imageUri
                )
            }.collect { combinedState ->
                _profileState.value = combinedState
            }
        }
    }

    fun updateProfile(name: String, phone: String, gender: String) {
        viewModelScope.launch {
            val currentProfileData = profileState.value
            val updatedProfileData = ProfileData(
                name = name,
                phone = phone,
                gender = gender,
                imageUri = currentProfileData.imageUri // Keep the existing image URI
            )
            profileDataStore.saveProfile(updatedProfileData)
        }
    }

    fun updateProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            val currentProfileData = profileDataStore.profileDataFlow.first()
            val updatedProfileData = currentProfileData.copy(imageUri = imageUri.toString())
            profileDataStore.saveProfile(updatedProfileData)
        }
    }
}
