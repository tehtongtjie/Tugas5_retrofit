package pember.qq.tugas5_retrofit.network

import pember.qq.tugas5_retrofit.model.LoginRequest
import pember.qq.tugas5_retrofit.model.LoginResponse
import pember.qq.tugas5_retrofit.model.PasienResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("pasien")
    fun getPasien(
        @Header("Authorization") token: String
    ): Call<PasienResponse>
}