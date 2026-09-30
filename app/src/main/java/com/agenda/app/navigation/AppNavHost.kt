package com.agenda.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.agenda.app.ui.screen.PantallaAgregarEvento
import com.agenda.app.ui.screen.PantallaListaEventos
import com.agenda.app.viewmodel.AgendaViewModel

/**
 * Contiene el [NavHost] y el menú inferior ([NavigationBar]).
 *
 * Punto clave de MVVM + navegación: el [AgendaViewModel] se crea AQUÍ, en el
 * nivel del NavHost, y se pasa por parámetro a ambas pantallas. Así las dos
 * pantallas comparten la MISMA instancia (la misma lista de eventos) y un
 * evento guardado en "Agregar" aparece inmediatamente en "Eventos".
 *
 * Si cada pantalla llamara a `viewModel()` por su cuenta, cada una obtendría su
 * propia copia (una por `NavBackStackEntry`) y NO se verían los datos entre sí.
 */
@Composable
fun AppNavHost(viewModel: AgendaViewModel = viewModel()) {
    val navController = rememberNavController()

    // Ruta de la pantalla que se está mostrando ahora (para marcar el menú).
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = rutaActual == Rutas.Lista.ruta,
                    onClick = { navController.navigate(Rutas.Lista.ruta) { launchSingleTop = true } },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    label = { Text("Eventos") }
                )
                NavigationBarItem(
                    selected = rutaActual == Rutas.Agregar.ruta,
                    onClick = { navController.navigate(Rutas.Agregar.ruta) { launchSingleTop = true } },
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    label = { Text("Agregar") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rutas.Lista.ruta,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rutas.Lista.ruta) {
                PantallaListaEventos(
                    viewModel = viewModel,
                    onIrAAgregar = { navController.navigate(Rutas.Agregar.ruta) }
                )
            }
            composable(Rutas.Agregar.ruta) {
                PantallaAgregarEvento(viewModel = viewModel)
            }
        }
    }
}
