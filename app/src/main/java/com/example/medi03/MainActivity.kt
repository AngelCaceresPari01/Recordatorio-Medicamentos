package com.example.medi03

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.medi03.ui.theme.Medi03Theme
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*

val Context.dataStore by preferencesDataStore("user_prefs")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            Medi03Theme {
                AppContent()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Medicamentos"
            val descriptionText = "Canal para recordatorios de medicamentos"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("med_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // DataStore key y flujo
    val FONT_SIZE_KEY = floatPreferencesKey("font_size")
    val fontSizeFlow = context.dataStore.data.map { prefs -> prefs[FONT_SIZE_KEY] ?: 18f }
    val fontSize by fontSizeFlow.collectAsState(initial = 18f)

    var medicamento by remember { mutableStateOf("Paracetamol 500mg") }
    var fecha by remember { mutableStateOf("2025-11-07") }
    var hora by remember { mutableStateOf("08:30") }
    var showSlider by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recordatorio de Medicamentos") },
                actions = {
                    IconButton(onClick = { showSlider = !showSlider }) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustar tamaño de letra")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Ingrese los datos del medicamento:",
                fontSize = fontSize.sp
            )

            OutlinedTextField(
                value = medicamento,
                onValueChange = { medicamento = it },
                label = { Text("Medicamento") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(fontSize = fontSize.sp)
            )

            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(fontSize = fontSize.sp)
            )

            OutlinedTextField(
                value = hora,
                onValueChange = { hora = it },
                label = { Text("Hora (HH:mm)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = LocalTextStyle.current.copy(fontSize = fontSize.sp)
            )

            Button(
                onClick = {
                    enviarNotificacion(context, medicamento, fecha, hora)
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Programar Recordatorio", fontSize = fontSize.sp)
            }
        }

        if (showSlider) {
            AlertDialog(
                onDismissRequest = { showSlider = false },
                confirmButton = {
                    TextButton(onClick = { showSlider = false }) {
                        Text("Cerrar")
                    }
                },
                title = { Text("Tamaño de letra") },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${fontSize.toInt()}sp", fontSize = fontSize.sp)
                        Slider(
                            value = fontSize,
                            onValueChange = {
                                scope.launch {
                                    context.dataStore.edit { prefs ->
                                        prefs[FONT_SIZE_KEY] = it
                                    }
                                }
                            },
                            valueRange = 12f..30f
                        )
                    }
                }
            )
        }
    }
}

fun enviarNotificacion(context: Context, medicamento: String, fecha: String, hora: String) {
    val builder = Notification.Builder(context, "med_channel")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Recordatorio de medicamento")
        .setContentText("Tomar $medicamento el $fecha a las $hora")
        .setPriority(Notification.PRIORITY_DEFAULT)

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.notify(Random().nextInt(), builder.build())
}
