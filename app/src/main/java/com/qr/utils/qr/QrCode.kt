package com.qr.utils.qr

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Point
import android.view.WindowManager
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.WriterException

object QrCode {

    fun generateQRCode(activity: Activity,textToCode : String) : Bitmap? {
        val windowManager = activity.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager

        val display = windowManager.defaultDisplay

        val point = Point()
        display.getSize(point)

        // getting width and height of a point
        val width = point.x
        val height = point.y
        var dimen : Int? = null

        if (width < height ){
            dimen = width
        }else{
            dimen = height
        }
        dimen = dimen * 3 / 4

        val qrgEncoder = QRGEncoder(
                textToCode,
                null,
                QRGContents.Type.TEXT,
                dimen
        )

        try {
            return qrgEncoder.encodeAsBitmap()
        }catch (e: WriterException){
            e.printStackTrace()
        }
        return null
    }
}