# FelipeCortes_App

Aplicación móvil Android de **gestión de arriendos y transparencia financiera** para la Junta de Vecinos Población Zaror. Proyecto académico del curso DSY1105 (Desarrollo de Aplicaciones Móviles), DUOC UC.

Es un MVP académico: no procesa pagos reales ni reemplaza los procedimientos formales de la organización. Todos los datos de la app son ficticios.

## Funcionalidades por perfil

| Funcionalidad | Vecino | Directiva | Tesorería | Admin |
|---|---|---|---|---|
| Iniciar sesión y recuperar contraseña | sí | sí | sí | sí |
| Ver detalle de arriendo | sí | sí | sí | sí |
| Solicitar arriendo y ver el estado de sus solicitudes | sí | no | no | sí |
| Revisar solicitudes (aprobar o rechazar con motivo) | no | sí | no | sí |
| Registrar ingresos y gastos | no | no | sí | sí |
| Adjuntar comprobantes con cámara o galería | no | no | sí | sí |
| Informe financiero por período (resumen) | sí | sí | sí | sí |
| Informe financiero: gastos por categoría | no | sí | sí | sí |
| Reportar un problema | sí | sí | no | sí |

Reglas de negocio principales:

- Una solicitud de arriendo nace en estado PENDIENTE y puede pasar a APROBADA, RECHAZADA o CANCELADA.
- No se puede aprobar una reserva que se superponga con otra ya aprobada en la misma dependencia, fecha y horario.
- La hora de término debe ser posterior a la de inicio.
- Los montos deben ser números positivos. Los movimientos anulados no se borran: dejan de contar en los totales.
- Los vecinos solo ven información resumida. El detalle por categoría es de uso interno.
- Las notificaciones locales avisan cuando una solicitud se aprueba o se rechaza.

## Tecnologías

- Kotlin, Jetpack Compose y Material Design 3
- Arquitectura MVVM (ViewModel, Repository, StateFlow)
- Room (SQLite) para la persistencia local
- Navigation Compose
- DataStore para ajustes
- Retrofit para consumir una API REST de ejemplo (pantalla de posts)
- Kotest y MockK para las pruebas unitarias

## Estructura del proyecto

Dentro de app/src/main/java/com/example/felipecortes_app:

- data/local: base de datos Room (entidades y DAOs de reservas, movimientos y comprobantes)
- data/model: modelos (Usuario, Rol, EstadoReserva, Dependencia, Movimiento)
- data/remote: Retrofit y servicio de la API de ejemplo
- repository: reglas de negocio (autenticación, reservas, movimientos, comprobantes)
- viewmodel: estado de cada pantalla
- ui/screens: pantallas en Jetpack Compose
- ui/navigation: grafo de navegación
- ui/utils: utilidades (tamaño de ventana y notificaciones)

## Cuentas de prueba (datos ficticios)

| Perfil | Correo | Contraseña |
|---|---|---|
| Vecino | vecino@jdvzaror.cl | 1234 |
| Directiva | directiva@jdvzaror.cl | directiva123 |
| Tesorería | tesoreria@jdvzaror.cl | tesoreria123 |
| Admin | admin@jdvzaror.cl | admin123 |

## Cómo ejecutar

1. Abrir el proyecto en Android Studio y esperar la sincronización de Gradle.
2. Ejecutar en un emulador o dispositivo con Android 7.0 (API 24) o superior.
3. Para probar la cámara en el emulador, aceptar el permiso cuando se solicite. Usar solo imágenes ficticias o anonimizadas.

Pruebas unitarias:

    ./gradlew :app:testDebugUnitTest

APK de release firmado:

1. Crear una llave de firma con keytool y guardarla fuera del repositorio.
2. Crear en la raíz el archivo keystore.properties con las claves storeFile, storePassword, keyAlias y keyPassword. Este archivo y los .jks están en .gitignore y no se suben al repositorio.
3. Compilar con:

       ./gradlew :app:assembleRelease

   El APK queda en app/build/outputs/apk/release/app-release.apk. Sin keystore.properties, el proyecto compila en debug sin problemas.

## Decisiones de alcance del MVP

- **Autenticación simplificada:** los usuarios y contraseñas están definidos localmente en la app, solo para la demostración. No es una solución de seguridad productiva. En una versión real, la autenticación y la autorización vivirían en el backend y las contraseñas se guardarían con hash seguro.
- **Permisos por perfil:** hoy se aplican en la interfaz (qué pantallas y acciones ve cada rol). Deben validarse también en el backend.
- **Pagos informativos:** la app registra ingresos y gastos como información. No procesa pagos ni se conecta a bancos o sistemas contables.
- **Notificaciones locales:** los avisos salen en el mismo dispositivo donde se aprueba o rechaza la solicitud. Un envío al teléfono del vecino requeriría un servidor de notificaciones.
- **Comprobantes:** la ruta de cada imagen es local al dispositivo y no debe asumirse disponible en otro teléfono.
- **Recuperar contraseña:** es simulado. No se envía ningún correo y el mensaje es el mismo exista o no la cuenta.
- **Base de datos local:** al cambiar la estructura, Room recrea la base de datos y borra los datos de prueba.
- **Plataforma:** el alcance es Android nativo. iOS requeriría otro cliente.

## Trabajo pendiente

- Backend Java con Spring Boot (microservicios de reservas, finanzas y administración) y conexión desde Retrofit.
- Sincronización entre la base local y el backend.
- Recordatorios asociados a las fechas de reserva.
- Hash de contraseñas y autenticación con token.
