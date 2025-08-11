package com.example.rickandmorty.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmorty.data.api.RickAndMortyApi
import com.example.rickandmorty.data.model.Character
import retrofit2.HttpException
import java.io.IOException

class CharactersPagingSource(
    private val api: RickAndMortyApi
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val pageIndex = params.key ?: 1
        return try {
            val response = api.getCharacters(pageIndex)
            val data = response.results
            val nextKey = if (response.info.next == null) null else pageIndex + 1
            val prevKey = if (pageIndex == 1) null else pageIndex - 1
            LoadResult.Page(
                data = data,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        val anchor = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchor) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}
