package com.example.camasyssign


data class SignRequest (val data: ByteArray, val hashAlgorithm: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SignRequest

        if (!data.contentEquals(other.data)) return false
        if (hashAlgorithm != other.hashAlgorithm) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + hashAlgorithm.hashCode()
        return result
    }
}