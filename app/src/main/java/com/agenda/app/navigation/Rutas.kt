package com.agenda.app.navigation

/**
 * Rutas de la app declaradas como una `sealed class`.
 *
 * ¿Por qué no usar strings sueltos como `"lista"`? Porque un error de tipeo
 * (`"lista"` vs `"lista "`) solo se descubre en tiempo de ejecución.
 * Con una `sealed class`, el compilador valida las rutas y el `when` queda
 * exhaustivo: si agrego una pantalla nueva, el compilador me avisa dónde falta.
 */
sealed class Rutas(val ruta: String) {
    object Lista : Rutas("lista")
    object Agregar : Rutas("agregar")
}
