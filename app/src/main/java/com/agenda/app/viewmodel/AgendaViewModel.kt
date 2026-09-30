package com.agenda.app.viewmodel

import androidx.lifecycle.ViewModel
import com.agenda.app.model.ErroresEvento
import com.agenda.app.model.Evento
import com.agenda.app.model.FormularioEventoEstado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * El ViewModel: la "fuente de verdad" de los datos y de las reglas de negocio.
 *
 * Responsabilidades:
 *  - Guardar el estado del formulario y la lista de eventos.
 *  - Validar (las reglas viven AQUÍ, no en la interfaz).
 *  - Exponer el estado como [StateFlow] para que las pantallas lo observen.
 *
 * Convención de nombres: `_x` es privado y mutable (solo el ViewModel lo cambia);
 * `x` es público y de solo lectura (lo que ve la pantalla).
 */
class AgendaViewModel : ViewModel() {

    // ── Estado del formulario ──
    private val _estadoFormulario = MutableStateFlow(FormularioEventoEstado())
    val estadoFormulario: StateFlow<FormularioEventoEstado> = _estadoFormulario.asStateFlow()

    // ── Lista de eventos (compartida entre las dos pantallas) ──
    private val _eventos = MutableStateFlow<List<Evento>>(emptyList())
    val eventos: StateFlow<List<Evento>> = _eventos.asStateFlow()

    // ── Cada campo llama a una función: la UI avisa, el ViewModel decide ──
    fun actualizarTitulo(valor: String) = _estadoFormulario.update { it.copy(titulo = valor) }
    fun actualizarFecha(valor: String) = _estadoFormulario.update { it.copy(fecha = valor) }
    fun actualizarLugar(valor: String) = _estadoFormulario.update { it.copy(lugar = valor) }
    fun actualizarCupos(valor: String) = _estadoFormulario.update { it.copy(cupos = valor) }

    /**
     * Aplica las reglas de negocio y devuelve `true` si todo es válido.
     * Al mismo tiempo actualiza `errores` para que la pantalla pinte los mensajes.
     */
    fun validarFormulario(): Boolean {
        val estado = _estadoFormulario.value
        val cuposNumerico = estado.cupos.toDoubleOrNull()

        val errorTitulo = if (estado.titulo.isBlank()) "El título es obligatorio" else null

        val errorFecha = if (estado.fecha.isBlank()) "La fecha es obligatoria" else null

        val errorCupos = when {
            estado.cupos.isBlank() -> null // cupos es opcional
            cuposNumerico == null -> "Los cupos deben ser un número"
            cuposNumerico < 0 -> "Los cupos no pueden ser negativos"
            else -> null
        }

        _estadoFormulario.update {
            it.copy(errores = ErroresEvento(errorTitulo, errorFecha, errorCupos))
        }

        return errorTitulo == null && errorFecha == null && errorCupos == null
    }

    /**
     * Si el formulario es válido, agrega el evento a la lista y limpia el
     * formulario. Devuelve `true` si se guardó.
     */
    fun guardarEvento(): Boolean {
        if (!validarFormulario()) return false

        val estado = _estadoFormulario.value
        val evento = Evento(
            titulo = estado.titulo.trim(),
            fecha = estado.fecha.trim(),
            lugar = estado.lugar.ifBlank { "Sin lugar" },
            cupos = estado.cupos.toDoubleOrNull()?.toInt() ?: 0
        )

        _eventos.update { it + evento }
        _estadoFormulario.value = FormularioEventoEstado()
        return true
    }
}
