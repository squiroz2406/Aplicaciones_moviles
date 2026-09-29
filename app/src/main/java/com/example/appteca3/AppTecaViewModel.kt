package com.example.appteca3

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppTecaViewModel(private val state: SavedStateHandle) : ViewModel() {
    val textoBusqueda: StateFlow<String> = state.getStateFlow("texto", "")
    val modoSoloFavoritas: StateFlow<Boolean> = state.getStateFlow("soloFavoritas", false)
    private val _listaVisible = MutableStateFlow<List<App>>(emptyList())
    val listaVisible: StateFlow<List<App>> = _listaVisible
    private val _todas = MutableStateFlow<List<App>>(emptyList())
    val todas: StateFlow<List<App>> = _todas

    init {
        Log.d("VIDA", "ViewModel → creado (${hashCode()}) " +
                "texto='${textoBusqueda.value}' soloFavoritas=${modoSoloFavoritas.value}")
        aplicarFiltros()
    }

    fun buscar(texto: String) {
        state["texto"] = texto
        aplicarFiltros()
    }

    fun alternarModo() {
        state["soloFavoritas"] = !modoSoloFavoritas.value
        aplicarFiltros()
    }

    fun alternarFavorita(app: App) {
        Catalogo.toggleFavorita(app.id)
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        val query = textoBusqueda.value.trim()
        var lista: List<App> = Catalogo.apps.toList()
        lista = lista.sortedByDescending { it.esFavorita }
        if (query.isNotEmpty()) lista = lista.filter {
            it.nombre.contains(query, true) || it.categoria.contains(query, true)
        }
        if (modoSoloFavoritas.value) lista = lista.filter { it.esFavorita }
        _listaVisible.value = lista
        _todas.value = Catalogo.apps.toList()      // NUEVO
    }

    override fun onCleared() {
        Log.d("VIDA", "ViewModel → onCleared")
    }
}