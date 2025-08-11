package com.example.rickandmorty.viewmodel

import app.cash.turbine.test
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.domain.GetCharactersUseCase
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
class CharactersViewModelTest {
    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var viewModel: CharactersViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        getCharactersUseCase = mockk()
        viewModel = CharactersViewModel(getCharactersUseCase, mockk(relaxed = true))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchAllCharacters should update charactersList`() = runTest {
        val fakeList = listOf(Character(1, "Rick", "Alive", "Human", "", "Male", "url", mockk(), mockk()))
        coEvery { getCharactersUseCase.invoke() } returns fakeList

        viewModel.fetchAllCharacters()

        viewModel.charactersList.test {
            assertEquals(fakeList, awaitItem())
        }
    }

    @Test
    fun `fetchAllCharacters should handle error state`() = runTest {
        coEvery { getCharactersUseCase.invoke() } throws Exception("Network error")

        viewModel.fetchAllCharacters()

        viewModel.error.test {
            val errorMsg = awaitItem()
            assertTrue(errorMsg?.contains("Network error") == true)
        }
    }

    @Test
    fun `toggleShowAll should change showAll state`() = runTest {
        val initial = viewModel.showAll.value
        viewModel.toggleShowAll()
        assertTrue(initial != viewModel.showAll.value)
    }

    @Test
    fun `retry should call fetchAllCharacters when showAll is true`() = runTest {
        coEvery { getCharactersUseCase.invoke() } returns emptyList()
        viewModel.toggleShowAll() // ativa showAll
        viewModel.retry()
        viewModel.charactersList.test {
            assert(awaitItem().isEmpty())
        }
    }
}
