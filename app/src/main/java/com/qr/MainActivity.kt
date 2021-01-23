package com.qr

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Network
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.storage.FirebaseStorage
import com.qr.databinding.ActivityMainBinding
import com.qr.utils.firebase.FirebaseUtil
import com.qr.utils.image.Image
import com.qr.utils.permission.Permission
import com.qr.utils.qr.QrCode
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var bitmap: Bitmap
    private var mStorageRef: FirebaseStorage? = null
    private var byteArray = byteArrayOf()
    private lateinit var downloadedUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        mStorageRef = FirebaseStorage.getInstance()
        validateInputText()
    }

    private fun validateInputText() {
        binding.idBtnGenerateQR.setOnClickListener {
            if (binding.idEdt.text.isNotEmpty()) {
                if (com.qr.utils.network.Network.isConnected(this)) {
                    generateQRCode()
                    if (this::bitmap.isInitialized)
                        byteArray = Image.bitmapToByteArray(bitmap)
                    uploadImage(binding.idEdt.text.toString())
                } else {
                    Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.empty_error_msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateQRCode() {
        binding.progressImgUpload.visibility = View.VISIBLE
        binding.idBtnGenerateQR.isEnabled = false
        if (QrCode.generateQRCode(this, binding.idEdt.text.toString()) != null) {
            bitmap = QrCode.generateQRCode(this, binding.idEdt.text.toString())!!
            binding.idIVQrcode.setImageBitmap(bitmap)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.share -> {
                shareQRcode()
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareQRcode() {
        try {

            if (Permission.isPermissionAvailable(this)) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, Image.bitmapToUri(this@MainActivity, bitmap))
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            } else {
                Permission.grantPermission(Permission.write_external_Store,
                        Const.WRITE_EXTERNAL_PERMISSION_CODE, this)
            }
        } catch (e: Exception) {
            Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(data: String) {
        FirebaseUtil.uploadImageBytes(this, byteArray, data, object : Callback<Boolean>() {
            override fun onSuccess(response: Boolean?) {
                if (response != null && response) {
                    binding.progressImgUpload.visibility = View.GONE
                    binding.idBtnGenerateQR.isEnabled = true
                    Toast.makeText(this@MainActivity, R.string.img_uploaded, Toast.LENGTH_SHORT).show()
                    clearEdtText()
                }
            }

            override fun onFailure(error: Boolean?) {
                binding.progressImgUpload.visibility = View.GONE
                binding.idBtnGenerateQR.isEnabled = true
            }
        })
    }

    private fun clearEdtText() {
        binding.idEdt.text = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Const.WRITE_EXTERNAL_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.size > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareQRcode()
            } else {
                Toast.makeText(this@MainActivity, R.string.ext_write_msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}