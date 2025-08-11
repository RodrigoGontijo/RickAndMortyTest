package com.example.rickandmorty.domain

import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.repo.CharactersRepository

class GetCharactersUseCase(private val repository: CharactersRepository) {
    suspend fun invoke(): List<Character> = repository.getAllCharacters()
}

