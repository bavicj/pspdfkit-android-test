package com.example.camasyssign

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pspdfkit.configuration.activity.PdfActivityConfiguration
import com.pspdfkit.configuration.signatures.SignatureCertificateSelectionMode
import com.pspdfkit.configuration.signatures.SignatureSavingStrategy
import com.pspdfkit.signatures.SignatureManager
import com.pspdfkit.ui.PdfActivityIntentBuilder
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import java.io.File.separator
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import java.io.File
import com.pspdfkit.framework.`in`
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var openButton: Button
    lateinit var openExistingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val certs = CertificateFactory.getInstance("X509").generateCertificates(assets.open("cachain.pem"))
        for (cert in certs) {
            SignatureManager.addTrustedCertificate(cert as X509Certificate)
        }
        openButton = findViewById(R.id.open_button)
        openButton.setOnClickListener {
            ExtractAssetTask.extract("pspdfkit_doc.pdf", "test", this, true) { documentFile ->
                val config = PdfActivityConfiguration.Builder(this)
                    .signatureCertificateSelectionMode(SignatureCertificateSelectionMode.IF_AVAILABLE)
                    .signatureSavingStrategy(SignatureSavingStrategy.NEVER_SAVE)
                    .build()
                val intent = PdfActivityIntentBuilder.fromUri(this, Uri.fromFile(documentFile))
                    .configuration(config)
                    .activityClass(CustomPdfActivity::class.java)
                    .build()
                intent.putExtra("NAME", "Avis_contract")
                startActivityForResult(intent, 99)
            }
        }
        openExistingButton = findViewById(R.id.open_existing_button)
        openExistingButton.setOnClickListener {
            ExtractAssetTask.extract("contract.pdf", "test", this, true) { documentFile ->
                val config = PdfActivityConfiguration.Builder(this)
                    .signatureCertificateSelectionMode(SignatureCertificateSelectionMode.IF_AVAILABLE)
                    .signatureSavingStrategy(SignatureSavingStrategy.NEVER_SAVE)
                    .build()
                val intent = PdfActivityIntentBuilder.fromUri(this, Uri.fromFile(documentFile))
                    .configuration(config)
                    .activityClass(CustomPdfActivity::class.java)
                    .build()
                intent.putExtra("NAME", "Avis_contract")
                startActivityForResult(intent, 99)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            99 -> {
                if (resultCode === Activity.RESULT_OK) {
                    val source = File(data?.getStringExtra("RESPONSE"))
                    if (source.exists()) {
                        Toast.makeText(this, "COPIED", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "NOT COPIED", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }
}
