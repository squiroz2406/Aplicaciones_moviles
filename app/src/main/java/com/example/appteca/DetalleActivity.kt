package com.example.appteca

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetalleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)
        val appId = intent.getIntExtra("appId", -1)
        val app = Catalogo.apps.find { it.id == appId }
        if (app == null) { finish(); return }
        findViewById<TextView>(R.id.tvDetNombre).text = app.nombre
        findViewById<TextView>(R.id.tvDetCategoria).text = app.categoria
        findViewById<TextView>(R.id.tvDetDescripcion).text = app.descripcion
    }
}