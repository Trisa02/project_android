package com.example.project_android_trisa.Model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("full_name") val full_name : String,
    @SerializedName("email") val email : String,
    @SerializedName("username") val username:String
)
