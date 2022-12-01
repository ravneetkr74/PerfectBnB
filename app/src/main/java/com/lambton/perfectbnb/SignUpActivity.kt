package com.lambton.perfectbnb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.header_layout.*

class SignUpActivity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        firebaseAuth= FirebaseAuth.getInstance()
        signup.setOnClickListener(){
            SignUpUser()
        }

        textView6.setOnClickListener(){
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        autoLogin()


    }

    private fun autoLogin() {
         firebaseAuth.currentUser?.let {
          if (it.email == "admin@gmail.com"){
              val intent = Intent(this, AdminMainActivity::class.java)
              startActivity(intent)
          }else {
              val intent = Intent(this, MainActivity::class.java)
              startActivity(intent)
          }
         }
    }

    private fun SignUpUser(){
      val useremail=email.text.toString()
      val password=password.text.toString()
      val confirmPassword=confirm_password.text.toString()

      if(useremail.isBlank()||password.isBlank()||confirmPassword.isBlank()){

          Toast.makeText(this,"Email and Password can't be blank",Toast.LENGTH_SHORT).show()
          return
      }

      if(password!=confirmPassword){
          Toast.makeText(this,"Password and ConfirmPassword do not match",Toast.LENGTH_SHORT).show()
          return
      }

      firebaseAuth.createUserWithEmailAndPassword(useremail,password)
          .addOnCompleteListener(this){
              if(it.isSuccessful){
                val intent=Intent(this,LoginActivity::class.java)
                  startActivity(intent)

              }else{
                  Toast.makeText(this,"Error while generating User",Toast.LENGTH_SHORT).show()
              }
          }
  }
}