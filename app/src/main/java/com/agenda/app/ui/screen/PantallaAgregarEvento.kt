package com.agenda.app.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.agenda.app.viewmodel.AgendaViewModel

/**
 * Pantalla "Agregar": un formulario validado.
 *
 * Cada campo escribe en el ViewModel (`onValueChange = viewModel::actualizarTitulo`)
 * y lee de vuelta su valor y su error (`estado.titulo`, `estado.errores.errorTitulo`).
 * La pantalla NO valida: solo pinta lo que el ViewModel le dice.
 *
 * `isError = true` + `supportingText` es lo que hace que Material 3 muestre el
 * campo en rojo con su mensaje de error debajo.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgregarEvento(viewModel: AgendaViewModel) {
    val estado by viewModel.estadoFormulario.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Agregar evento") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = estado.titulo,
                onValueChange = viewModel::actualizarTitulo,
                label = { Text("Título") },
                isError = estado.errores.errorTitulo != null,
                supportingText = { estado.errores.errorTitulo?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.fecha,
                onValueChange = viewModel::actualizarFecha,
                label = { Text("Fecha") },
                isError = estado.errores.errorFecha != null,
                supportingText = { estado.errores.errorFecha?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.lugar,
                onValueChange = viewModel::actualizarLugar,
                label = { Text("Lugar (opcional)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = estado.cupos,
                onValueChange = viewModel::actualizarCupos,
                label = { Text("Cupos (opcional)") },
                isError = estado.errores.errorCupos != null,
                supportingText = { estado.errores.errorCupos?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.guardarEvento() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}
