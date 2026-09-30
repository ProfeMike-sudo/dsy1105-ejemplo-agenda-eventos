package com.agenda.app.model

/**
 * Estado del formulario de alta de eventos.
 *
 * Los campos de texto (`fecha`, `cupos`) se guardan como `String`, no como
 * número, porque el usuario puede estar escribiendo a la mitad (por ejemplo
 * "12."). Recién al validar se intenta convertir a número; así no le quitamos
 * el control al que escribe.
 *
 * `errores` viaja junto al estado: la pantalla solo lee `errores.errorTitulo`
 * y decide pintar el mensaje, sin saber nada de las reglas de negocio.
 */
data class FormularioEventoEstado(
    val titulo: String = "",
    val fecha: String = "",
    val lugar: String = "",
    val cupos: String = "",
    val errores: ErroresEvento = ErroresEvento()
)

/**
 * Errores de validación por campo. `null` = "ese campo está bien".
 */
data class ErroresEvento(
    val errorTitulo: String? = null,
    val errorFecha: String? = null,
    val errorCupos: String? = null
)
