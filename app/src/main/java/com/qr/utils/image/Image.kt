package com.qr.utils.image

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream
import java.util.*

object Image {

    fun getRandomImgPath(): String {
        return "QRImage/" + UUID.randomUUID() + ".png"
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    fun bitmapToUri(activity: Activity,bitmap: Bitmap) : Uri {
        val bitmapPath = MediaStore.Images.Media.insertImage(activity.contentResolver, bitmap, "title", null)
        return Uri.parse(bitmapPath)
    }
}