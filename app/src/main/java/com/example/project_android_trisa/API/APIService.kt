package com.example.project_android_trisa.API

import com.example.project_android_trisa.Model.LoginRequest
import com.example.project_android_trisa.Model.LoginResponse
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST





interface APIService {
    ///Ini ditambahkan ketika data api nya dalam bentuk JSON
    data class RegisterRequest(
        val full_name: String,
        val email: String,
        val username: String,
        val password: String,
    )

    @POST("authentikasi/register")
    fun registerUser(
        @Body request: RegisterRequest
    ): Call<ResponseBody>

    @POST("authentikasi/login")
    fun loginUser(
        @Body request: LoginRequest
    ) : Call<LoginResponse>


}