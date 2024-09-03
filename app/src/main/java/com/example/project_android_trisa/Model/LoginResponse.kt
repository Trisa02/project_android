package com.example.project_android_trisa.Model

import com.google.gson.annotations.SerializedName

data class LoginResponse(@SerializedName("token") val accessToken: String?,
                         @SerializedName("refresh_token") val refreshToken: String?,val user :User)
