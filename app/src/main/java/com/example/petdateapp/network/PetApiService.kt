package com.example.petdateapp.network

import com.example.petdateapp.network.dto.PetDto
import retrofit2.Response
import retrofit2.http.*

/**
 * Interfaz de Retrofit que define los endpoints de la API de mascotas.
 */
interface PetApiService {

    /**
     * Obtiene todas las mascotas de un dueño específico.
     * Corresponde a: GET /pets/owner/{ownerId}
     */
    @GET("pets/owner/{ownerId}")
    suspend fun getPetsByOwner(@Path("ownerId") ownerId: String): List<PetDto>

    /**
     * Crea una nueva mascota.
     * Corresponde a: POST /pets/post
     */
    @POST("pets/post")
    suspend fun createPet(@Body pet: PetDto): PetDto

    /**
     * Actualiza una mascota existente.
     * Corresponde a: PUT /pets/put/{id}
     */
    @PUT("pets/put/{id}")
    suspend fun updatePet(@Path("id") petId: String, @Body pet: PetDto): PetDto

    /**
     * Elimina una mascota por su ID.
     * Corresponde a: DELETE /pets/delete/{id}
     */
    @DELETE("pets/delete/{id}")
    suspend fun deletePet(@Path("id") petId: String): Response<Void> // Usamos Response<Void> para respuestas sin cuerpo
}
