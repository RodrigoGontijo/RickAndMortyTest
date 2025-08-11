package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.domain.GetCharacterDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase
) : ViewModel() {
    private val _character = MutableStateFlow<Character?>(null)
    val character: StateFlow<Character?> = _character

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchCharacter(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = getCharacterDetailUseCase.invoke(id)
                _character.value = result
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _loading.value = false
            }
        }
    }
}
