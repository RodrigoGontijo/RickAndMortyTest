package com.example.rickandmorty.viewmodel

import app.cash.turbine.test
import com.example.rickandmorty.data.model.Character
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
    fun `fetchCharacter should update characterDetail`() = runTest {
        val fakeDetail = Character(1, "Rick", "Alive", "Human", "", "Male", "url", mockk(), mockk())
        coEvery { getCharacterDetailUseCase.invoke(1) } returns fakeDetail

        viewModel.fetchCharacter(1)

        viewModel.character.test {
            assertEquals(fakeDetail, awaitItem())
        }
    }

    @Test
    fun `fetchCharacter should handle error state`() = runTest {
        coEvery { getCharacterDetailUseCase.invoke(1) } throws Exception("Network error")

        viewModel.fetchCharacter(1)

        viewModel.error.test {
            val errorMsg = awaitItem()
            assertTrue(errorMsg?.contains("Network error") == true)
        }
    }
}
