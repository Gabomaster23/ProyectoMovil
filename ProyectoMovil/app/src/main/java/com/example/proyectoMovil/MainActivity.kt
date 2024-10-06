package com.example.avance_proyecto

import android.widget.CalendarView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.material3.*

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proyectoMovil.ui.theme.AvanceTheme
import androidx.compose.foundation.shape.RoundedCornerShape


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvanceTheme {
                MainApp()
            }
        }
    }
}

// Función principal que controla la navegación entre pantallas
@Composable
fun MainApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("secondScreen") { SecondScreen(navController) }
        composable("notesTasksScreen/{item}") { backStackEntry ->
            val item = backStackEntry.arguments?.getString("item") ?: "Notas/Tareas"
            TaskDetailsScreen(item, listOf(item), navController)
        }
    }
}

// Pantalla de inicio
@Composable
fun HomeScreen(navController: NavController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        // Campo de búsqueda con ícono
        var searchQuery by remember { mutableStateOf("") }
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Filtros de estado
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatusCard("Todos", 0, Modifier.weight(1f).padding(end = 8.dp), Icons.Default.Home)
            StatusCard("Pendientes", 0, Modifier.weight(1f).padding(start = 8.dp), Icons.Default.Notifications)
        }

        Spacer(modifier = Modifier.height(16.dp))
        StatusCard("Terminados", 0, Modifier.fillMaxWidth(), Icons.Default.Check)

        Spacer(modifier = Modifier.height(16.dp))

        // Título para listas
        Text("MIS LISTAS", style = MaterialTheme.typography.titleLarge)

        // Listas de notas y tareas con íconos
        ListCardWithIcon(
            title = "Notas",
            icon = Icons.Default.Menu,
            items = listOf("NotaP 1", "NotaP 2", "NotaP 3"),
            onItemClick = { item -> navController.navigate("notesTasksScreen/$item") }
        )

        ListCardWithIcon(
            title = "Tareas",
            icon = Icons.Default.Menu,
            items = listOf("Tarea 1", "Tarea 2", "Tarea 3"),
            onItemClick = { item -> navController.navigate("notesTasksScreen/$item") }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón para agregar una nueva tarea o nota
        ListCardWithIcon(
            title = "Agregar nueva",
            icon = Icons.Default.Add,
            items = emptyList(),
            onClick = { navController.navigate("secondScreen") }
        )
    }
}

// Composable que renderiza una tarjeta con un ícono y una lista de elementos
@Composable
fun ListCardWithIcon(
    title: String,
    icon: ImageVector,
    items: List<String>,
    onClick: (() -> Unit)? = null,
    onItemClick: ((String) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { if (onClick != null) onClick() else expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = title)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(title)
                }
                if (items.isNotEmpty()) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Expandir")
                }
            }
            if (expanded && items.isNotEmpty()) {
                Column {
                    items.forEach { item ->
                        Text(
                            text = item,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { onItemClick?.invoke(item) }
                        )
                    }
                }
            }
        }
    }
}

// Tarjeta que muestra un estado general de tareas con un contador
@Composable
fun StatusCard(title: String, count: Int, modifier: Modifier = Modifier, icon: ImageVector) {
    Card(
        modifier = modifier.height(120.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(32.dp))
                Text(text = "$count", style = MaterialTheme.typography.headlineMedium)
            }
            Text(text = title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))
        }
    }
}

// Segunda pantalla para agregar una nueva nota o tarea
@Composable
fun SecondScreen(navController: NavController) {
    var selectedDate by remember { mutableStateOf("Seleccione una fecha") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Cancelar", color = Color.Black)
            }
            Text(text = "Nueva nota", style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = { /* Acción de agregar */ }) {
                Text("Agregar", color = Color.Black)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(text = "TÍTULO", fontSize = 14.sp, color = Color.Black)
                BasicTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color(0xFFE8EAF6)),
                    textStyle = TextStyle(fontSize = 18.sp, color = Color.Black)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Descripción", fontSize = 14.sp, color = Color.Black)
                BasicTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color(0xFFE8EAF6)),
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
                )
            }
        }

        // Controles y selección de fecha
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.DateRange, contentDescription = "Calendario", tint = Color.Black)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.AttachFile, contentDescription = "Archivo", tint = Color.Black)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Cámara", tint = Color.Black)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Mic, contentDescription = "Micrófono", tint = Color.Black)
                }
            }
        }

        // Selector de fecha usando AndroidView
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { CalendarView(it) },
            update = { calendarView ->
                calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    selectedDate = "$dayOfMonth/${month + 1}/$year"
                }
            }
        )
    }
}

// Pantalla que muestra detalles de una nota o tarea específica
@Composable
fun TaskDetailsScreen(title: String, items: List<String>, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = { navController.popBackStack() }) {
            Text("Atrás", color = Color.Black)
        }
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        items.forEach { item ->
            Text(item, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

// Vista previa en el editor
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AvanceTheme {
        MainApp()
    }
}
