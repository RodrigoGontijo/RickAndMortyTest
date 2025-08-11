package com.example.rickandmorty.domain

import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.repo.CharactersRepository

class GetCharacterDetailUseCase(private val repository: CharactersRepository) {
    suspend fun invoke(id: Int): Character = repository.getCharacterById(id)
}

