package com.example.thongjing

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScanActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private var scannerView: ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scannerView = ZXingScannerView(this)

        setContentView(scannerView)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    override fun handleResult(result: Result?) {
        scannerView?.resumeCameraPreview(this)
        scannerView?.stopCamera()

        goToProductActivity(productCode = result.toString())
    }

    override fun onResume() {
        super.onResume()

        scannerView?.setResultHandler(this)
        scannerView?.setAspectTolerance(0.2f)
        scannerView?.startCamera()
    }

    private fun goToProductActivity(productCode: String) {
        val intent: Intent = Intent(this, ProductActivity::class.java).apply {
            putExtra("PRODUCT_CODE", productCode)
        }

        startActivity(intent)
    }
}
