package com.example.project_android_trisa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.project_android_trisa.API.APIService
import com.example.project_android_trisa.Model.LoginRequest
import com.example.project_android_trisa.Model.LoginResponse
import com.example.project_android_trisa.Model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var register: TextView

    // Menampilkan alert dialog
    private fun showAlertDialog(title: String, message: String, onPositiveClick: (() -> Unit)? = null) {
        if (isFinishing || isDestroyed) return  // Cek jika aktivitas sudah dihancurkan
        val builder = AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialogTheme)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            onPositiveClick?.invoke()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cek apakah token masih ada di SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val token = sharedPreferences.getString("ACCESS_TOKEN", null)

        if (token != null) {
            // Jika token ada, langsung menuju Home
            val intent = Intent(this@MainActivity, Home::class.java)
            startActivity(intent)
            finish()
        } else {
            // Jika tidak ada token, tampilkan layar login
            setContentView(R.layout.activity_main)

            // Inisialisasi views dan login logic
            usernameInput = findViewById(R.id.username_input)
            passwordInput = findViewById(R.id.password_input)
            loginBtn = findViewById(R.id.buttonsave)
            register = findViewById(R.id.register_akun)

            register.setOnClickListener {
                val intent = Intent(this@MainActivity, Register_Form::class.java)
                startActivity(intent)
            }

            // Login user
            loginBtn.setOnClickListener {
                val username = usernameInput.text.toString()
                val password = passwordInput.text.toString()

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    perfomLogin(username, password)
                } else {
                    showAlertDialog("Informasi", "Silahkan Input Username dan Password dahulu!")
                }
            }
        }
    }

    private fun perfomLogin(username: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.16.2.107/Api-Project-Android/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)
        val loginRequest = LoginRequest(username, password, "local")

        service.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Log detail response untuk debugging
                Log.d("API Response", "Response Code: ${response.code()}")
                Log.d("API Response", "Response Message: ${response.message()}")
                Log.d("API Response", "Response Body: ${response.body()}")

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val accessToken = loginResponse.accessToken
                        val user = loginResponse.user

                        Log.d("API Response", "Access Token: $accessToken")
                        Log.d("API Response", "User: $user")

                        if (user != null) {
                            // Simpan token dan data user
                            saveUserData(accessToken, user)

                            // Navigasi ke layar berikutnya
                            showAlertDialog("Informasi", "Login Berhasil") {
                                val intent = Intent(this@MainActivity, Home::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            showAlertDialog("Informasi", "Username atau password salah")
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserData(accessToken: String?, user: User) {
        // Simpan access token dan informasi user di SharedPreferences atau penyimpanan lainnya
        val sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("FULL_NAME", user.full_name)
        editor.putString("EMAIL", user.email)
        editor.putString("USERNAME", user.username)
        editor.apply()
    }
}
