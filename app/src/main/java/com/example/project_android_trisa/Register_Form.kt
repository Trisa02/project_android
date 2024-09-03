package com.example.project_android_trisa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_android_trisa.API.APIService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.security.MessageDigest

class Register_Form : AppCompatActivity() {
    private lateinit var back: ImageView
    private  lateinit var username_register : EditText
    private  lateinit var email_register : EditText
    private  lateinit var fullname_register : EditText
    private  lateinit var password_register : EditText
    private lateinit var btnsave : Button



    private fun showAlertDialog(title: String, message: String, onPositiveClick: (() -> Unit)? = null) {
        val builder = AlertDialog.Builder(this@Register_Form, R.style.CustomAlertDialogTheme)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            onPositiveClick?.invoke()
        }
        val dialog = builder.create()
        dialog.show()
    }


    private fun registerUser(fullName: String,email: String, username: String, hashedPassword: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.16.2.107/Api-Project-Android/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)
        val request = APIService.RegisterRequest(fullName,email, username, hashedPassword)
        Log.d("Register_Form", "Sending request: $request")
        val call = service.registerUser(request)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("Register_Form", "Response: ${response.body()?.string()}")
                if (response.isSuccessful) {
                    showAlertDialog("Success", "Register berhasil") {
                        val intent = Intent(this@Register_Form, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Log.e("Register_Form", "Gagal Registrasi: ${response.errorBody()?.string()}")
                    showAlertDialog("Informasi", "Gagal Registrasi: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Register_Form", "Error: ${t.message}")
                showAlertDialog("Error", "Error: ${t.message}")
            }
        })
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_form)
        back = findViewById(R.id.back_button)

        //Insert to tb_register
        username_register = findViewById(R.id.username)
        fullname_register = findViewById(R.id.full_name)
        email_register = findViewById(R.id.email)
        password_register = findViewById(R.id.password)
        btnsave = findViewById(R.id.buttonregister)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        back.setOnClickListener {
            val intent = Intent(this@Register_Form, MainActivity::class.java)
            startActivity(intent)
        }

        btnsave.setOnClickListener{
            val fullName = fullname_register.text.toString()
            val username = username_register.text.toString()
            val password = password_register.text.toString()
            val email = email_register.text.toString()


            registerUser(fullName, email, username, password)
        }
    }
}