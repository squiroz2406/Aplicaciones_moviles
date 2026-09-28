package com.example.appteca

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AppTecaViewModelTest {

    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    private lateinit var vm: AppTecaViewModel

    @Before
    fun setUp() {
        // App es inmutable: se resetea reemplazando cada una por una copia
        Catalogo.apps.replaceAll { it.copy(esFavorita = false) }
        vm = AppTecaViewModel()
    }

    @Test
    fun alCrearse_publicaElCatalogoCompleto() {
        assertEquals(Catalogo.apps.size, vm.listaVisible.value!!.size)
    }

    @Test
    fun buscar_filtraPorNombreOCategoria() {
        vm.buscar("spo")
        val lista = vm.listaVisible.value!!
        assertTrue(lista.isNotEmpty())
        assertTrue(lista.all {
            it.nombre.contains("spo", true) || it.categoria.contains("spo", true)
        })
    }

    @Test
    fun buscar_ignoraEspaciosAlrededor() {
        vm.buscar("  spo  ")
        val conEspacios = vm.listaVisible.value
        vm.buscar("spo")
        assertEquals(vm.listaVisible.value, conEspacios)
    }

    @Test
    fun modoSoloFavoritas_sinFavoritas_muestraListaVacia() {
        vm.alternarModo()
        assertTrue(vm.listaVisible.value!!.isEmpty())
        assertEquals(true, vm.modoSoloFavoritas.value)
    }

    @Test
    fun marcarFavorita_yActivarModo_muestraSoloEsa() {
        val id = Catalogo.apps.first().id
        vm.alternarFavorita(Catalogo.apps.first())
        vm.alternarModo()
        // se compara por id: la app publicada es una copia nueva, no la original
        assertEquals(listOf(id), vm.listaVisible.value!!.map { it.id })
    }

    @Test
    fun alternarModoDosVeces_vuelveATodas() {
        vm.alternarModo()
        vm.alternarModo()
        assertEquals(false, vm.modoSoloFavoritas.value)
        assertEquals(Catalogo.apps.size, vm.listaVisible.value!!.size)
    }
}