package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.repo.CharactersRepository
import com.example.rickandmorty.domain.GetCharactersUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val repository: CharactersRepository
) : ViewModel() {
    val characters: Flow<PagingData<Character>> = repository.pagedCharacters().cachedIn(viewModelScope)
    private val _charactersList = MutableStateFlow<List<Character>>(emptyList())
    val charactersList: StateFlow<List<Character>> = _charactersList

    private val _showAll = MutableStateFlow(false)
    val showAll: StateFlow<Boolean> = _showAll

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun toggleShowAll() {
        _showAll.value = !_showAll.value
        if (_showAll.value) fetchAllCharacters()
    }

    fun fetchAllCharacters() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val result = getCharactersUseCase.invoke()
                _charactersList.value = result
            } catch (e: Exception) {
                _error.value = e.localizedMessage
            } finally {
                _loading.value = false
            }
        }
    }

    fun retry() {
        if (_showAll.value) fetchAllCharacters()
    }
}
