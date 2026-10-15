# FelipeCortes_App — Gestión de Arriendos Junta de Vecinos Población Zaror

## Descripción
Aplicación Android desarrollada para el ramo DSY1105 (Desarrollo de Aplicaciones Móviles, DUOC UC).
Resuelve el caso de la Junta de Vecinos "Población Zaror": gestión de arriendos de espacios comunitarios
y transparencia financiera (cuotas, gastos, comprobantes).

Desarrollado en Kotlin con Jetpack Compose, siguiendo el patrón de arquitectura MVVM.

## Estudiante
- Felipe Cortés (proyecto desarrollado de forma individual)

## Funcionalidades
- Pantalla de Inicio con información general y accesos a las demás funciones.
- Pantalla de Detalle de un arriendo.
- Formulario de Solicitud de arriendo (nombre, correo, dirección, número de personas, fecha del
  arriendo, switch de equipo de sonido, aceptación de términos) con validaciones visuales y lógicas,
  y pantalla de Resumen con los datos ingresados.
- Formulario de Reporte de un problema en un espacio común.
- Función de Compartir el resumen de una solicitud (Intent nativo de Android).
- Modo "Tesorero": interruptor persistido localmente con DataStore Preferences, con animaciones de
  color y aparición/desaparición de contenido.
- Diseño adaptable a distintos tamaños de pantalla (celular, tablet mediana, tablet grande) con
  Window Size Classes.
- Navegación entre pantallas con Navigation Compose.
- Tema visual personalizado (colores, tipografía) acorde a la identidad de la Junta de Vecinos.

## Arquitectura
Patrón MVVM, organizado en:
- `model/` — clases de datos (estados de UI, errores).
- `viewmodel/` — lógica de negocio y manejo de estado.
- `ui/screens/` — pantallas (Composables).
- `ui/navigation/` — configuración de Navigation Compose.
- `ui/theme/` — colores y tipografía.
- `ui/utils/` — utilidades (Window Size Classes).
- `repository/` — acceso a datos persistidos (DataStore).

## Cómo ejecutar el proyecto
1. Clonar el repositorio: `git clone https://github.com/feli268/FelipeCortes_App.git`
2. Abrir la carpeta del proyecto en Android Studio.
3. Esperar que Gradle sincronice automáticamente las dependencias.
4. Ejecutar sobre un emulador o dispositivo físico con el botón ▶ Run.