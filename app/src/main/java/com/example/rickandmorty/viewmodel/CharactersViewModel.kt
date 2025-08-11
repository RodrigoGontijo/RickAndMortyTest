package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.repo.CharactersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CharactersViewModel(
    private val repository: CharactersRepository
) : ViewModel() {
    private val _charactersList = MutableStateFlow<List<Character>>(emptyList())
    val charactersList: StateFlow<List<Character>> = _charactersList

    fun fetchAllCharacters() {
        viewModelScope.launch {
            val result = repository.getAllCharacters()
            _charactersList.value = result
        }
    }

    val characters: Flow<PagingData<Character>> = repository.pagedCharacters().cachedIn(viewModelScope)

    // Se quiser buscar todos os personagens sem paginação:
    suspend fun getAllCharacters(): List<Character> = repository.getAllCharacters()
}
