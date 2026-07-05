package com.vahan.dartagnan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
 import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vahan.dartagnan.data.remote.OllamaApiService
import com.vahan.dartagnan.ui.components.loader.DartagnanLoader
import com.vahan.dartagnan.ui.theme.DartagnanTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val apiService = OllamaApiService()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DartagnanTheme {
                var aiThinking by remember { mutableStateOf(false) }
                var userPrompt by remember { mutableStateOf("") }
                // On passe à une Map pour stocker plusieurs réponses : Modèle -> Réponse
                var aiResponses by remember { mutableStateOf(mapOf<String, String>()) }
                var availableModels by remember { mutableStateOf(listOf<String>()) }
                var selectedModels by remember { mutableStateOf(setOf<String>()) }
                var expanded by remember { mutableStateOf(false) }
                var showSettings by remember { mutableStateOf(false) }
                var tempBaseUrl by remember { mutableStateOf(apiService.baseUrl) }
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    availableModels = apiService.getModels()
                    if (availableModels.isNotEmpty() && selectedModels.isEmpty()) {
                        selectedModels = setOf(availableModels.first())
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("Dartagnan AI", style = MaterialTheme.typography.titleLarge) },
                            actions = {
                                IconButton(onClick = { showSettings = true }) {
                                    Icon(Icons.Default.Settings, contentDescription = "Paramètres")
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    if (showSettings) {
                        AlertDialog(
                            onDismissRequest = { showSettings = false },
                            title = { Text("Configuration Ollama") },
                            text = {
                                Column {
                                    Text("IP de l'hôte (ex: 10.0.2.2 ou 192.168.1.X)")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    OutlinedTextField(
                                        value = tempBaseUrl,
                                        onValueChange = { tempBaseUrl = it },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    apiService.baseUrl = tempBaseUrl
                                    showSettings = false
                                    scope.launch {
                                        availableModels = apiService.getModels()
                                    }
                                }) {
                                    Text("Valider")
                                }
                            }
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Animation réduite pour laisser de la place au texte
                        Box(
                            modifier = Modifier.size(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            DartagnanLoader(thinking = aiThinking, size = 100)
                        }

                        if (availableModels.isNotEmpty()) {
                            Text(
                                text = "Sélectionnez les IA pour la Vision 360°",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                            )
                            // Liste de puces (Chips) pour sélectionner plusieurs modèles
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                availableModels.forEach { model ->
                                    FilterChip(
                                        selected = selectedModels.contains(model),
                                        onClick = {
                                            selectedModels = if (selectedModels.contains(model)) {
                                                if (selectedModels.size > 1) selectedModels - model else selectedModels
                                            } else {
                                                selectedModels + model
                                            }
                                        },
                                        label = { Text(model) }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = userPrompt,
                            onValueChange = { userPrompt = it },
                            label = { Text("Votre requête complexe...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 120.dp, max = 250.dp),
                            enabled = !aiThinking,
                            maxLines = 10
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    scope.launch {
                                        aiThinking = true
                                        val newResponses = mutableMapOf<String, String>()
                                        // On interroge chaque modèle sélectionné l'un après l'autre
                                        selectedModels.forEach { model ->
                                            newResponses[model] = apiService.chat(userPrompt, model)
                                        }
                                        aiResponses = newResponses
                                        aiThinking = false
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !aiThinking && userPrompt.isNotBlank() && selectedModels.isNotEmpty()
                            ) {
                                Text(if (aiThinking) "Consultation des Mousquetaires..." else "Lancer Vision 360°")
                            }

                            OutlinedButton(
                                onClick = { userPrompt = ""; aiResponses = emptyMap() },
                                enabled = !aiThinking
                            ) {
                                Text("Effacer")
                            }
                        }

                        // Affichage des multiples réponses
                        aiResponses.forEach { (model, response) ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Source : $model",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                    Text(
                                        text = response,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DartagnanTheme {
        Greeting("Android")
    }
}