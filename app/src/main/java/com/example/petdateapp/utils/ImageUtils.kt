package com.example.petdateapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

object ImageUtils {

    /**
     * Convierte una Uri de una imagen a un String en formato Base64.
     * Comprime la imagen a JPEG con calidad del 80% para reducir su tamaño.
     */
    fun uriToBase64(context: Context, uri: Uri): String? {
        return try {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }

            val outputStream = ByteArrayOutputStream()
            // Comprimimos la imagen para que no sea excesivamente pesada
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            val byteArray = outputStream.toByteArray()

            // Codificamos el array de bytes a un string Base64
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Guarda una imagen desde una Uri en el almacenamiento interno de la app.
     * @param context Contexto de la aplicación.
     * @param uri La Uri de la imagen a guardar.
     * @return La Uri del archivo guardado en el almacenamiento interno, o null si falla.
     */
    fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null

            // Usamos un nombre de archivo único para evitar colisiones
            val fileName = "pet_image_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)

            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()

            // Devolvemos la Uri del archivo creado
            Uri.fromFile(file)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
