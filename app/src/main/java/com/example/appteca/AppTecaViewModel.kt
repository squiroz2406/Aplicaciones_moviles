package com.example.appteca

import android.util.Log
import androidx.lifecycle.ViewModel
class AppTecaViewModel : ViewModel() {
    init {
        Log.d("VIDA", "ViewModel → creado (${hashCode()})")
    }
    override fun onCleared() {
        Log.d("VIDA", "ViewModel → onCleared (destruido de verdad)")
    }
}