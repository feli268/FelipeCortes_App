package com.example.felipecortes_app.ui.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object NotificacionesHelper {

    private const val CANAL_ID = "reservas"

    // Desde Android 8 toda notificación debe pertenecer a un "canal"
    private fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL_ID,
                "Reservas",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Avisos sobre el estado de las solicitudes de arriendo"
            }
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(canal)
        }
    }

    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, id: Int, titulo: String, texto: String) {
        // Desde Android 13 hay que tener el permiso concedido; si no, no se muestra nada
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        crearCanal(context)
        val notificacion = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(id, notificacion)
    }
}
