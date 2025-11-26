package com.example.petdateapp.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object for Pet.
 * This class represents a Pet object received from the backend API.
 * The field names match the JSON keys from the microservice.
 */
data class PetDto(
    @SerializedName("id")
    val id: String?, // El ID que viene de MongoDB

    @SerializedName("ownerId")
    val ownerId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("imageUrl")
    val imageUrl: String?,

    @SerializedName("animalType")
    val animalType: String,

    @SerializedName("weight")
    val weight: Double,

    @SerializedName("allergies")
    val allergies: String,

    @SerializedName("food")
    val food: String,

    @SerializedName("bio")
    val bio: String
)
