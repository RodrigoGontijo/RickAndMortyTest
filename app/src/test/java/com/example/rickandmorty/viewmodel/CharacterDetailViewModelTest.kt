package com.example.rickandmorty.viewmodel

import app.cash.turbine.test
import com.example.rickandmorty.data.model.CharacterDetail
import com.example.rickandmorty.domain.GetCharacterDetailUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest {
    private lateinit var getCharacterDetailUseCase: GetCharacterDetailUseCase
    private lateinit var viewModel: CharacterDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        getCharacterDetailUseCase = mockk()
        viewModel = CharacterDetailViewModel(getCharacterDetailUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCharacterDetail should update characterDetail`() = runTest {
        val fakeDetail = CharacterDetail(1, "Rick", "Alive", "Human", "Earth", "Male", "url")
        coEvery { getCharacterDetailUseCase.invoke(1) } returns fakeDetail

        viewModel.fetchCharacterDetail(1)

        viewModel.characterDetail.test {
            assertEquals(fakeDetail, awaitItem())
        }
    }

    @Test
    fun `fetchCharacterDetail should handle error state`() = runTest {
        coEvery { getCharacterDetailUseCase.invoke(1) } throws Exception("Network error")

        viewModel.fetchCharacterDetail(1)

        viewModel.error.test {
            val errorMsg = awaitItem()
            assertTrue(errorMsg?.contains("Network error") == true)
        }
    }
}

