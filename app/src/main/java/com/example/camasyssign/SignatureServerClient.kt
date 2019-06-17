package com.example.camasyssign

import okhttp3.ResponseBody
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "http://192.168.100.124:8080/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface SignatureServerClient {
    @GET("certificate")
    fun getCertificate(): Call<ResponseBody>

    @POST("sign-stream")
    fun signStream(@Body request: SignRequest): Call<ResponseBody>
}

object SignatureServerApi {
    val retrofitService: SignatureServerClient by lazy {
        retrofit.create(SignatureServerClient::class.java)
    }
}