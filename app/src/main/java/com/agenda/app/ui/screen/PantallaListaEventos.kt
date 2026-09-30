package com.agenda.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.agenda.app.ui.components.TarjetaEvento
import com.agenda.app.viewmodel.AgendaViewModel

/**
 * Pantalla "Eventos": muestra la lista de eventos del ViewModel.
 *
 * Observa `viewModel.eventos` con [collectAsState]: cuando el ViewModel emite
 * un valor nuevo (porque en "Agregar" se guardó un evento), esta pantalla
 * se redibuja sola. No hay que avisarle manualmente.
 *
 * La tarjeta que dibuja cada evento vive en `ui.components` ([TarjetaEvento]),
 * reutilizable desde otras pantallas sin duplicar código.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaListaEventos(viewModel: AgendaViewModel, onIrAAgregar: () -> Unit) {
    val eventos by viewModel.eventos.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Eventos") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onIrAAgregar) {
                Text("+", style = MaterialTheme.typography.titleLarge)
            }
        }
    ) { padding ->
        if (eventos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay eventos todavía. Toca + para agregar el primero.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(eventos) { evento ->
                    TarjetaEvento(evento)
                }
            }
        }
    }
}
