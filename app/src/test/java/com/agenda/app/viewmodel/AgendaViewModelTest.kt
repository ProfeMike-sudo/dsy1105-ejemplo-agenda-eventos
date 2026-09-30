package com.agenda.app.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Test unitario del [AgendaViewModel].
 *
 * Se prueba SOLO la lógica (validación y guardado), sin emulador ni interfaz.
 * Esto es posible porque el ViewModel no depende de Android: es Kotlin puro.
 */
class AgendaViewModelTest {

    private lateinit var viewModel: AgendaViewModel

    @Before
    fun setUp() {
        viewModel = AgendaViewModel()
    }

    @Test
    fun `titulo vacio produce error de validacion`() {
        viewModel.actualizarTitulo("")
        viewModel.actualizarFecha("2026-10-01")
        viewModel.actualizarCupos("5")

        assertFalse(viewModel.validarFormulario())
        assertEquals("El título es obligatorio", viewModel.estadoFormulario.value.errores.errorTitulo)
    }

    @Test
    fun `fecha vacia produce error de validacion`() {
        viewModel.actualizarTitulo("Charla de Kotlin")
        viewModel.actualizarFecha("")

        assertFalse(viewModel.validarFormulario())
        assertEquals("La fecha es obligatoria", viewModel.estadoFormulario.value.errores.errorFecha)
    }

    @Test
    fun `cupos no numerico produce error`() {
        viewModel.actualizarTitulo("Charla de Kotlin")
        viewModel.actualizarFecha("2026-10-01")
        viewModel.actualizarCupos("abc")

        assertFalse(viewModel.validarFormulario())
        assertEquals("Los cupos deben ser un número", viewModel.estadoFormulario.value.errores.errorCupos)
    }

    @Test
    fun `cupos negativos producen error`() {
        viewModel.actualizarTitulo("Charla de Kotlin")
        viewModel.actualizarFecha("2026-10-01")
        viewModel.actualizarCupos("-1")

        assertFalse(viewModel.validarFormulario())
        assertEquals("Los cupos no pueden ser negativos", viewModel.estadoFormulario.value.errores.errorCupos)
    }

    @Test
    fun `formulario valido guarda el evento en la lista`() {
        viewModel.actualizarTitulo("Charla de Kotlin")
        viewModel.actualizarFecha("2026-10-01")
        viewModel.actualizarLugar("Sala 201")
        viewModel.actualizarCupos("30")

        assertTrue(viewModel.guardarEvento())
        assertEquals(1, viewModel.eventos.value.size)
        assertEquals("Charla de Kotlin", viewModel.eventos.value[0].titulo)
        assertEquals("2026-10-01", viewModel.eventos.value[0].fecha)
        assertEquals(30, viewModel.eventos.value[0].cupos)
    }

    @Test
    fun `formulario invalido NO guarda el evento`() {
        viewModel.actualizarTitulo("")
        viewModel.actualizarFecha("2026-10-01")

        assertFalse(viewModel.guardarEvento())
        assertTrue(viewModel.eventos.value.isEmpty())
    }

    @Test
    fun `cupos en blanco se guarda como cero`() {
        viewModel.actualizarTitulo("Charla de Kotlin")
        viewModel.actualizarFecha("2026-10-01")
        viewModel.actualizarCupos("")

        assertTrue(viewModel.guardarEvento())
        assertEquals(0, viewModel.eventos.value[0].cupos)
    }
}
