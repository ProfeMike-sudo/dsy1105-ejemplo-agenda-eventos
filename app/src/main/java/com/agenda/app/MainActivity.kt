package com.agenda.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import com.agenda.app.navigation.AppNavHost

/**
 * Punto de entrada de la app.
 *
 * No dibuja nada por sí mismo: solo llama a [AppNavHost], que define las
 * pantallas y la navegación. Esta separación permite probar y leer cada
 * pieza por separado.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AppNavHost()
            }
        }
    }
}
