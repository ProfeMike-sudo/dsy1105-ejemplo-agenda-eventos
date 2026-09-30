package com.agenda.app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.agenda.app.model.Evento

/**
 * Componente reutilizable: la tarjeta que dibuja UN evento.
 * Separarlo en una función propia mantiene la pantalla legible y evita repetir código.
 */
@Composable
fun TarjetaEvento(evento: Evento) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = evento.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Fecha: ${evento.fecha}  ·  Cupos: ${evento.cupos}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Lugar: ${evento.lugar}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
