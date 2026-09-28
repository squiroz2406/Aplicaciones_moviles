package com.example.appteca

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
class AppAdapter(
    private val onAppClick: (App) -> Unit,
    private val onFavoritoClick: (App) -> Unit
) : ListAdapter<App, AppAdapter.AppViewHolder>(DiffCallback) {
    class AppViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvNombre: TextView = v.findViewById(R.id.tvNombre)
        val tvCategoria: TextView = v.findViewById(R.id.tvCategoria)
        val tvEstrella: TextView = v.findViewById(R.id.tvEstrella)
    }
    // Pregunta 2: ¿cómo se crea una fila vacía? (se llama pocas veces: recicla)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_app, parent, false)
        return AppViewHolder(v)
    }
    // Pregunta 3: ¿cómo se llena la fila con el dato de la posición?
    // (se llama TODO el tiempo: cada reciclado pasa por acá)
    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = getItem(position)
        holder.tvNombre.text = app.nombre
        holder.tvCategoria.text = app.categoria
        holder.tvEstrella.text = if (app.esFavorita) "★" else "☆"
        holder.itemView.setOnClickListener { onAppClick(app)}
        holder.tvEstrella.setOnClickListener { onFavoritoClick(app) }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<App>() {
        override fun areItemsTheSame(old: App, new: App) = old.id == new.id
        override fun areContentsTheSame(old: App, new: App) = old == new
    }
}