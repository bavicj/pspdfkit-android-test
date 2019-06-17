package com.example.camasyssign

import com.pspdfkit.signatures.SignatureManager
import com.pspdfkit.signatures.provider.SignatureProvider
import com.pspdfkit.signatures.signers.Signer
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

class ExternalSigner(displayName: String) : Signer(displayName) {

    lateinit var certificate: X509Certificate

    fun init() {
        SignatureServerApi.retrofitService.getCertificate().enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                System.out.println("failure " + t.localizedMessage)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val certFactory = CertificateFactory.getInstance("X.509")
                certificate = certFactory.generateCertificate(ByteArrayInputStream(response.body()?.bytes())) as X509Certificate
            }
        })

    }

    override fun prepareSigningParameters(callback: OnSigningParametersReadyCallback) {
        callback.onSigningParametersReady(CustomSignatureProvider(), certificate)
    }

}