package com.example.camasyssign

import com.pspdfkit.signatures.EncryptionAlgorithm
import com.pspdfkit.signatures.HashAlgorithm
import com.pspdfkit.signatures.provider.SignatureProvider

class CustomSignatureProvider : SignatureProvider {
    override fun getEncryptionAlgorithm(): EncryptionAlgorithm {
        return EncryptionAlgorithm.RSA
    }

    override fun signData(data: ByteArray, hashAlgorithm: HashAlgorithm): ByteArray {
        val request = SignRequest(data, getAlgorithmString(hashAlgorithm))
        return SignatureServerApi.retrofitService.signStream(request).execute().body()?.bytes()!!
    }

    private fun getAlgorithmString(hashAlgorithm: HashAlgorithm): String {
        when (hashAlgorithm) {
            HashAlgorithm.MD5 -> return "MD5"
            HashAlgorithm.SHA160 -> return "SHA1"
            HashAlgorithm.SHA224 -> return "SHA224"
            HashAlgorithm.SHA256 -> return "SHA256"
            HashAlgorithm.SHA384 -> return "SHA384"
            HashAlgorithm.SHA512 -> return "SHA512"
            else -> throw IllegalStateException("No appropriate signing algorithm was found for hash algorithm: " + hashAlgorithm.name)
        }

    }

}
