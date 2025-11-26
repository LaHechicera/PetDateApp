package com.example.petdateapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton object to provide a configured Retrofit instance.
 */
object RetrofitClient {

    private const val BASE_URL = "https://mascotams-backend.onrender.com/"

    // Create a logging interceptor to see request and response logs in Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Create an OkHttpClient and add the interceptor
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Build the Retrofit instance
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient) // Set the custom OkHttpClient
        .addConverterFactory(GsonConverterFactory.create()) // Use Gson for JSON conversion
        .build()

    /**
     * Lazily create and provide the PetApiService instance.
     */
    val petApiService: PetApiService by lazy {
        retrofit.create(PetApiService::class.java)
    }
}
