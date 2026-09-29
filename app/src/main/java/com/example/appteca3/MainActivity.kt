package com.example.appteca3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appteca3.ui.theme.AppTeca3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTeca3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        PantallaAppTeca()
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaAppTeca(vm: AppTecaViewModel = viewModel()) {
    val lista by vm.listaVisible.collectAsStateWithLifecycle()
    val modoFav by vm.modoSoloFavoritas.collectAsStateWithLifecycle()
    var textoBusqueda by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { nuevo ->
                textoBusqueda = nuevo
                vm.buscar(nuevo)
            },
            label = { Text("Buscar por nombre o categoría…") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        Button(
            onClick = { vm.alternarModo() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(if (modoFav) "★ Solo favoritas" else "☆ Todas")
        }
        ListaApps(
            apps = lista,
            onAppClick = { /* Etapa 4 */ },
            onFavoritoClick = { app -> vm.alternarFavorita(app) }
        )
    }
}

@Composable
fun FilaApp(
    app: App,
    onClick: () -> Unit,
    onFavoritoClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(app.nombre, style = MaterialTheme.typography.titleMedium)
            Text(app.categoria, style = MaterialTheme.typography.bodySmall)
        }
        Text(
            text = if (app.esFavorita) "★" else "☆",
            fontSize = 24.sp,
            modifier = Modifier
                .clickable { onFavoritoClick() }
                .padding(8.dp)
        )
    }
}

@Composable
fun ListaApps(
    apps: List<App>,
    onAppClick: (App) -> Unit,
    onFavoritoClick: (App) -> Unit
) {
    LazyColumn {
        items(apps, key = { it.id }) { app ->
            FilaApp(
                app = app,
                onClick = { onAppClick(app) },
                onFavoritoClick = { onFavoritoClick(app) }
            )
        }
    }
}