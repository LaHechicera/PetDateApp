package com.example.petdateapp.data

import com.example.petdateapp.network.PetApiService
import com.example.petdateapp.network.RetrofitClient
import com.example.petdateapp.network.dto.PetDto

/**
 * Repositorio para manejar las operaciones de datos de las mascotas.
 * Actúa como intermediario entre los ViewModels y las fuentes de datos (API de red).
 */
class PetRepository {

    private val apiService: PetApiService = RetrofitClient.petApiService

    /**
     * Llama a la API para obtener la lista de mascotas de un dueño.
     */
    suspend fun getPetsByOwner(ownerId: String): List<PetDto> {
        return apiService.getPetsByOwner(ownerId)
    }

    /**
     * Llama a la API para crear una nueva mascota.
     */
    suspend fun createPet(pet: PetDto): PetDto {
        return apiService.createPet(pet)
    }

    /**
     * Llama a la API para actualizar una mascota existente.
     */
    suspend fun updatePet(petId: String, pet: PetDto): PetDto {
        return apiService.updatePet(petId, pet)
    }

    /**
     * Llama a la API para eliminar una mascota.
     */
    suspend fun deletePet(petId: String) {
        apiService.deletePet(petId)
    }
}
