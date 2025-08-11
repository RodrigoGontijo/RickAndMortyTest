package com.example.rickandmorty.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.rickandmorty.data.model.Character
import com.example.rickandmorty.viewmodel.CharactersViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListScreen(
    onCharacterClick: (Int) -> Unit,
    vm: CharactersViewModel = getViewModel()
) {
    val items = vm.characters.collectAsLazyPagingItems()
    val allCharacters by vm.charactersList.collectAsState()
    var showAll by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rick & Morty") },
                actions = {
                    Button(onClick = {
                        showAll = !showAll
                        if (showAll) vm.fetchAllCharacters()
                    }) {
                        Text(
                            if (showAll)
                                "Pagination"
                            else
                                "See all"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (showAll) {
                if (allCharacters.isEmpty()) {
                    Loading()
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 140.dp),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(allCharacters) { character ->
                            CharacterCard(character = character) {
                                onCharacterClick(character.id)
                            }
                        }
                    }
                }
            } else {
                if (items.loadState.refresh is LoadState.Loading) {
                    Loading()
                } else if (items.loadState.refresh is LoadState.Error) {
                    val e = (items.loadState.refresh as LoadState.Error).error
                    ErrorView(message = e.localizedMessage ?: "Unknown error") { items.retry() }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 140.dp),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items.itemCount) { index ->
                            val character = items[index]
                            character?.let {
                                CharacterCard(character = it) {
                                    onCharacterClick(it.id)
                                }
                            }
                        }
                        // Append loading / error
                        when (val state = items.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Loading(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
                            }

                            is LoadState.Error -> {
                                item {
                                    ErrorView(
                                        message = state.error.localizedMessage ?: "Error",
                                        onRetry = { items.retry() }
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterCard(character: Character, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )
        Column(Modifier.padding(12.dp)) {
            Text(
                character.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "${character.species} • ${character.status}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun Loading(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(message)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) { Text("Retry") }
    }
}
