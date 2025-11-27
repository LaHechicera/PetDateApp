package com.example.petdateapp.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class ProfileData(
    val name: String,
    val phone: String,
    val gender: String,
    val imageUri: String
)

class ProfileDataStore(context: Context) {

    private val appContext = context.applicationContext

    private val nameKey = stringPreferencesKey("profile_name")
    private val phoneKey = stringPreferencesKey("profile_phone")
    private val genderKey = stringPreferencesKey("profile_gender")
    private val imageUriKey = stringPreferencesKey("profile_image_uri")

    val profileDataFlow: Flow<ProfileData> = appContext.dataStore.data
        .map {
            val name = it[nameKey] ?: ""
            val phone = it[phoneKey] ?: ""
            val gender = it[genderKey] ?: "Otro"
            val imageUri = it[imageUriKey] ?: ""
            ProfileData(name, phone, gender, imageUri)
        }

    suspend fun saveProfile(profileData: ProfileData) {
        appContext.dataStore.edit {
            it[nameKey] = profileData.name
            it[phoneKey] = profileData.phone
            it[genderKey] = profileData.gender
            it[imageUriKey] = profileData.imageUri
        }
    }

    suspend fun clearProfileData() {
        appContext.dataStore.edit {
            it.remove(nameKey)
            it.remove(phoneKey)
            it.remove(genderKey)
            it.remove(imageUriKey)
        }
    }
}