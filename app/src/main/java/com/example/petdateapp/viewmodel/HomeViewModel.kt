package com.example.petdateapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petdateapp.data.PetRepository
import com.example.petdateapp.data.datastore.UserDataStore
import com.example.petdateapp.network.dto.PetDto
import com.example.petdateapp.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PetsUiState {
    data class Success(val pets: List<PetDto>) : PetsUiState
    object Error : PetsUiState
    object Loading : PetsUiState
}

class HomeViewModel(
    private val userDataStore: UserDataStore,
    private val petRepository: PetRepository,
    private val userManager: UserManager
) : ViewModel() {

    val ownerName: StateFlow<String> = userDataStore.userEmailFlow.map { email ->
        email?.split("@")?.get(0) ?: ""
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ""
    )

    private val _petsUiState = MutableStateFlow<PetsUiState>(PetsUiState.Loading)
    val petsUiState: StateFlow<PetsUiState> = _petsUiState.asStateFlow()

    private val _ownerId = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch {
            _ownerId.value = userManager.getUserId()
            fetchPets()
        }
    }

    fun fetchPets() {
        viewModelScope.launch {
            _ownerId.value?.let { ownerId ->
                _petsUiState.value = PetsUiState.Loading
                try {
                    val pets = petRepository.getPetsByOwner(ownerId)
                    _petsUiState.value = PetsUiState.Success(pets)
                } catch (e: Exception) {
                    _petsUiState.value = PetsUiState.Error
                }
            }
        }
    }

    fun createPet(pet: PetDto) {
        viewModelScope.launch {
            try {
                val ownerId = _ownerId.value ?: userManager.getUserId() // Ensure we have ownerId
                petRepository.createPet(pet.copy(ownerId = ownerId))
                fetchPets()
            } catch (e: Exception) {
                // TODO: Manejar el error
            }
        }
    }

    fun updatePet(petId: String, pet: PetDto) {
        viewModelScope.launch {
            try {
                petRepository.updatePet(petId, pet)
                fetchPets()
            } catch (e: Exception) {
                // TODO: Manejar el error
            }
        }
    }

    fun deletePet(petId: String) {
        viewModelScope.launch {
            try {
                petRepository.deletePet(petId)
                fetchPets()
            } catch (e: Exception) {
                // TODO: Manejar el error
            }
        }
    }
}
