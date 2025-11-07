# Aplicacion de clase (recordatorio de medicamentos)
## Integrantes:
### -Caceres Pari Angel

### -Garcia Apaza Alan Jorge

## Descripcion
Recordatorio de Medicamentos es una aplicación Android desarrollada en Kotlin con Jetpack Compose, que permite registrar y gestionar medicamentos de forma sencilla.
El usuario puede ingresar el nombre del medicamento, la fecha y hora para tomarlo, y la app envía una notificación automática como recordatorio.Recordatorio de Medicamentos es una aplicación Android desarrollada en Kotlin con Jetpack Compose, que permite registrar y gestionar medicamentos de forma sencilla.
El usuario puede ingresar el nombre del medicamento, la fecha y hora para tomarlo, y la app envía una notificación automática como recordatorio.


### Caracteristicas principales
🧾 Formulario de registro: nombre, fecha y hora del medicamento.

⏰ Notificaciones programadas: recordatorios automáticos en la hora seleccionada.

🔤 Control de tamaño de texto: ajustable desde una barra superior.

💾 Persistencia de datos: configuración guardada con DataStore.

🎨 Diseño moderno: interfaz creada con Jetpack Compose y Material 3.

🧪 Datos simulados: el formulario se carga con información de ejemplo para pruebas rápidas.

### Explicaciones adicionales
Para el guardado de preferencias se uso el Data Store debido a
- fue creado directamente para su uso con compose
- utiliza un hilo secundario evitando bloqueos en la UI

Debido a eso reduce las probablidades de que la apliccion falle
