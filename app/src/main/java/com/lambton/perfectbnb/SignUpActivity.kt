package com.lambton.perfectbnb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.lambton.perfectbnb.admin.AdminMainActivity
import com.lambton.perfectbnb.user.MainActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //Initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance()
        //onClick of signup button calling signup function
        signup.setOnClickListener() {
            SignUpUser()
        }
        //Onclick of login button, we are going to login screen screen
        textView6.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        //Autologin function call
        autoLogin()
    }

    //auto login function with this we user will be stayed
    //login until unless he removed the app or click on signOut
    private fun autoLogin() {
        firebaseAuth.currentUser?.let {
            if (it.email == "admin@gmail.com") {
                val intent = Intent(this, AdminMainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
    //SignUp function, It will create new user email an password if email is not existing in firebase
    private fun SignUpUser() {
        val useremail = email.text.toString()
        val password = password.text.toString()
        val confirmPassword = confirm_password.text.toString()

        if (useremail.isBlank() || password.isBlank() || confirmPassword.isBlank()) {

            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password and ConfirmPassword do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }
        //Firebase in-build function to signup the user with email and password
        firebaseAuth.createUserWithEmailAndPassword(useremail, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Error while generating User", Toast.LENGTH_SHORT).show()
                }
            }
    }
}