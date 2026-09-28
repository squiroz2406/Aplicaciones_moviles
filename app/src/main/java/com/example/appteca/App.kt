package com.example.appteca

data class App(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val esFavorita: Boolean = false
)