package com.qr.utils.firebase

import android.app.Activity
import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.qr.Callback
import com.qr.utils.image.Image

object FirebaseUtil {

    private val fireRef: StorageReference = FirebaseStorage.getInstance().getReference(Image.getRandomImgPath())
    private lateinit var uploadTask: UploadTask

    fun addImageMetaData(data: String): StorageMetadata {
        return StorageMetadata.Builder()
                .setCustomMetadata("QR Encoded text", data)
                .build()
    }

    fun uploadImageBytes(activity: Activity, byteArray: ByteArray, data: String, Callback: Callback<Boolean>) {
        uploadTask = fireRef.putBytes(byteArray, addImageMetaData(data))

        uploadTask.addOnCompleteListener(activity, OnCompleteListener {
            if (it.isComplete) {
                Callback.onSuccess(true)
            }
        })
    }

    fun getImageUri(Callback: Callback<Uri>){
        uploadTask?.continueWithTask { task ->
            if (!task.isSuccessful){
                task.exception?.let {
                    throw it
                }
            }
            fireRef.downloadUrl
        }?.addOnCompleteListener{
            if (it.isSuccessful){
                 Callback.onSuccess(Uri.parse(it.result.toString()))
            }else{
            }
        }
    }
}