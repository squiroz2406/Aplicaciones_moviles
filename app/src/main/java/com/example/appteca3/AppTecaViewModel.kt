package com.example.appteca3

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppTecaViewModel : ViewModel() {
    private var query = ""
    private var soloFavoritas = false

    // ANTES (3B): MutableLiveData / LiveData — AHORA: StateFlow
    private val _listaVisible = MutableStateFlow<List<App>>(emptyList())
    val listaVisible: StateFlow<List<App>> = _listaVisible

    private val _modoSoloFavoritas = MutableStateFlow(false)
    val modoSoloFavoritas: StateFlow<Boolean> = _modoSoloFavoritas

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
        Catalogo.toggleFavorita(app.id)   // reemplaza la app por una copia, no la muta
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        var lista: List<App> = Catalogo.apps.toList()
        if (query.isNotEmpty()) lista = lista.filter {
            it.nombre.contains(query, true) || it.categoria.contains(query, true)
        }
        if (soloFavoritas) lista = lista.filter { it.esFavorita }
        _listaVisible.value = lista
        _modoSoloFavoritas.value = soloFavoritas
    }

    override fun onCleared() {
        Log.d("VIDA", "ViewModel → onCleared")
    }
}