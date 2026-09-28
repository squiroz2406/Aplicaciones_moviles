package com.example.appteca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: AppAdapter
    private var soloFavoritas = false
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("soloFavoritas", soloFavoritas)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        soloFavoritas = savedInstanceState?.getBoolean("soloFavoritas", false) ?: false

        adapter = AppAdapter(
            onAppClick = { app ->
                val intent = Intent(this, DetalleActivity::class.java)
                intent.putExtra("appId", app.id)
                startActivity(intent)
            },
            onFavoritoClick = { app ->
                Catalogo.toggleFavorita(app.id)
                aplicarFiltros()
            })
        val rv = findViewById<RecyclerView>(R.id.rvApps)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter
        findViewById<EditText>(R.id.etBuscar).addTextChangedListener {
            aplicarFiltros()
        }
        findViewById<Button>(R.id.btnSoloFav).setOnClickListener {
            soloFavoritas = !soloFavoritas
            aplicarFiltros()
        }
        aplicarFiltros()
    }
    override fun onResume() {
        super.onResume()
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        val q = findViewById<EditText>(R.id.etBuscar).text.toString().trim()
        var lista: List<App> = Catalogo.apps
        if (q.isNotEmpty()) lista = lista.filter {
            it.nombre.contains(q, true) || it.categoria.contains(q, true)
        }
        if (soloFavoritas) lista = lista.filter { it.esFavorita }
        lista = lista.sortedByDescending { it.esFavorita }   // Desafio opcional
        adapter.submitList(lista)
        findViewById<Button>(R.id.btnSoloFav).text =
            if (soloFavoritas) "★ Solo favoritas" else "☆ Todas"
    }
}