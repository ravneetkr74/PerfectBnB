package com.lambton.perfectbnb

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.header_layout.*


class LoginActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        firebaseAuth= FirebaseAuth.getInstance()
        button2.setOnClickListener(){
            loginUser()
        }
        textView.setOnClickListener(){
            val intent= Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loginUser(){
        val email=editTextTextPersonName.text.toString()
        val password=editTextTextPersonName2.text.toString()

        if(email.isBlank()||password.isBlank()){

            Toast.makeText(this,"Email and Password can't be blank",Toast.LENGTH_SHORT).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)

                }else{
                    Toast.makeText(this,"Error while login",Toast.LENGTH_SHORT).show()

                }
            }



    }
}