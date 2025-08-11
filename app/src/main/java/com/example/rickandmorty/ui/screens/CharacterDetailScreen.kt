package com.example.rickandmorty.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import com.example.rickandmorty.viewmodel.CharacterDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(id: Int, onBack: () -> Unit) {
    val vm: CharacterDetailViewModel = koinViewModel()
    val character by vm.character.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    LaunchedEffect(id) {
        vm.fetchCharacter(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(character?.name ?: "Character") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
            when {
                loading -> {
                    CircularProgressIndicator()
                }
                error != null -> {
                    Text("Erro: $error", color = MaterialTheme.colorScheme.error)
                }
                character != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(model = character!!.image, contentDescription = character!!.name, modifier = Modifier.size(200.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = character!!.name, style = MaterialTheme.typography.headlineMedium)
                        Text(text = "Status: ${character!!.status}")
                        Text(text = "Espécie: ${character!!.species}")
                        Text(text = "Gênero: ${character!!.gender}")
                        Text(text = "Origem: ${character!!.origin.name}")
                    }
                }
            }
        }
    }
}
