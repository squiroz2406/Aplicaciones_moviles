package com.example.appteca3

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppTecaViewModel : ViewModel() {
    private var query = ""
    private var soloFavoritas = false

    private val _listaVisible = MutableStateFlow<List<App>>(emptyList())
    val listaVisible: StateFlow<List<App>> = _listaVisible

    private val _modoSoloFavoritas = MutableStateFlow(false)
    val modoSoloFavoritas: StateFlow<Boolean> = _modoSoloFavoritas
    private val _appSeleccionada = MutableStateFlow<App?>(null)
    val appSeleccionada: StateFlow<App?> = _appSeleccionada

    init {
        Log.d("VIDA", "ViewModel → creado (${hashCode()})")
        aplicarFiltros()
    }

    fun buscar(texto: String) {
        query = texto.trim()
        aplicarFiltros()
    }

    fun alternarModo() {
        soloFavoritas = !soloFavoritas
        aplicarFiltros()
    }

    fun alternarFavorita(app: App) {
        Catalogo.toggleFavorita(app.id)
        aplicarFiltros()
    }

    fun seleccionar(app: App) { _appSeleccionada.value = app }
    fun volverALista() { _appSeleccionada.value = null }

    private fun aplicarFiltros() {
        var lista: List<App> = Catalogo.apps.toList()
        if (query.isNotEmpty()) lista = lista.filter {
            it.nombre.contains(query, true) || it.categoria.contains(query, true)
        }
        if (soloFavoritas) lista = lista.filter { it.esFavorita }
        _listaVisible.value = lista
        _modoSoloFavoritas.value = soloFavoritas
        _appSeleccionada.value = _appSeleccionada.value?.let { sel ->
            Catalogo.apps.find { it.id == sel.id }
        }
    }

    override fun onCleared() {
        Log.d("VIDA", "ViewModel → onCleared")
    }
}