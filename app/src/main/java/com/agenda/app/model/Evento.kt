package com.agenda.app.model

/**
 * El Modelo de la app: la "forma" de un evento.
 *
 * Es una `data class` con atributos `val` (inmutables): una vez creado, un
 * evento no se modifica; si hay que cambiarlo se crea una copia con `.copy()`.
 * Esta inmutabilidad evita estados compartidos que cambian "por debajo".
 */
data class Evento(
    val titulo: String,
    val fecha: String,
    val lugar: String,
    val cupos: Int
)
