package com.example.thongjing

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.net.NetworkInterface.getNetworkInterfaces
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (cannotAccessCamera()) {
            if (alreadyDenyAccessCamera()) {
                showPermissionRequired()
            } else {
                askCameraPermission()
            }
        } else {
            init()
        }
    }

    private fun init() {
        setMacAddress()
        setColumns()
        loadUser()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showPermissionRequired()
                } else {
                    init()
                }
            }
        }
    }

    fun onClickScanButton(view: View) {
        val intent = Intent(this, ScanActivity::class.java)

        startActivity(intent)
    }

    fun onClickSearchButton(view: View) {
        val intent = Intent(this, SearchActivity::class.java)

        startActivity(intent)
    }

    private fun alreadyDenyAccessCamera(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
    }

    private fun cannotAccessCamera(): Boolean {
        val cameraPermission: Int = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        return cameraPermission != PackageManager.PERMISSION_GRANTED
    }

    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA)
    }

    private fun showPermissionRequired() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setMessage("Permission to access the camera is required for this app.").setTitle("Permission required")

        builder.setPositiveButton("OK") { dialog, id ->
            finish()
        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    private fun setMacAddress() {
        macAddressTextView.text = getMacAddress()
    }

    private fun setColumns() {
    }

    private fun getMacAddress(): String {
        try {
            val all = Collections.list(getNetworkInterfaces())
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) {
                    continue
                }

                val macBytes = nif.hardwareAddress ?: return ""

                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:",b))
                }

                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (ex: Exception) {
        }

        return "02:00:00:00:00:00"
    }

    private fun loadUser() {
        val apiKey: String = "AIzaSyAMMokFaGeNXXU8IkfLm_wgEe7V82bxtjM"
        val sheetId: String = "1BdmiV5nI4mwzfshOjwck14oES0zcLgQqN3U7dOmcMxI"
        val url: String = "https://sheets.googleapis.com/v4/spreadsheets/$sheetId?key=$apiKey"




//        val client = OkHttpClient()
//        val request = Request.Builder().url(url).build()
//        val response = client.newCall(request).execute()




//        Log.i("THONGJINGTESTING", response.body()!!.string())
    }

    companion object {

        /**
         * Id to identify a camera permission request.
         */
        const val REQUEST_CAMERA = 0
    }

}
