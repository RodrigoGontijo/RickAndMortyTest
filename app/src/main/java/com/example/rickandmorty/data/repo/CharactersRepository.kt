package com.example.rickandmorty.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.data.paging.CharactersPagingSource
import kotlinx.coroutines.flow.Flow

class CharactersRepository(
    private val api: RickAndMortyApi
) {
    fun pagedCharacters(): Flow<PagingData<Character>> =
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { CharactersPagingSource(api) }
        ).flow

    suspend fun getAllCharacters(): List<Character> {
        val characters = mutableListOf<Character>()
        var page = 1
        var hasNextPage = true
        while (hasNextPage) {
            val response = api.getCharacters(page)
            characters.addAll(response.results)
            hasNextPage = response.info.next != null
            page++
        }
        return characters
    }

    suspend fun getCharacterById(id: Int): Character = api.getCharacterById(id)
}
