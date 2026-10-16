package com.example.felipecortes_app.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UsuarioViewModelTest {

    @Test
    fun `formulario vacio no es valido`() {
        val viewModel = UsuarioViewModel()

        val esValido = viewModel.validarFormulario()

        assertFalse(esValido)
        assertEquals("El nombre es obligatorio", viewModel.uiState.value.errores.nombre)
    }

    @Test
    fun `formulario completo y correcto es valido`() {
        val viewModel = UsuarioViewModel()
        viewModel.onNombreChange("Felipe Cortés")
        viewModel.onCorreoChange("felipe@correo.com")
        viewModel.onDireccionChange("Calle Falsa 123")
        viewModel.onNumeroPersonasChange("10")
        viewModel.onFechaArriendoChange("20/10/2026")
        viewModel.onAceptaTerminosChange(true)

        val esValido = viewModel.validarFormulario()

        assertTrue(esValido)
        assertEquals(null, viewModel.uiState.value.errores.nombre)
    }

    @Test
    fun `correo sin arroba es invalido`() {
        val viewModel = UsuarioViewModel()
        viewModel.onCorreoChange("correo-sin-arroba")

        viewModel.validarFormulario()

        assertEquals("Correo inválido", viewModel.uiState.value.errores.correo)
    }
}