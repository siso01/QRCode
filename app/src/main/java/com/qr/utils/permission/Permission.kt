package com.qr.utils.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


object Permission {

   val write_external_Store : String = Manifest.permission.WRITE_EXTERNAL_STORAGE

    fun isPermissionAvailable(thisActivity: Activity) : Boolean{
        if(ContextCompat.checkSelfPermission(thisActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        {
           return true
        }
        return false
    }

    fun grantPermission(permission: String, requestCode: Int, activity: Activity) {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            activity, arrayOf(permission),
                            requestCode)
        } else {
            Toast
                    .makeText(activity,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show()
        }
    }
}