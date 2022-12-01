package com.lambton.perfectbnb

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.lambton.perfectbnb.admin.AdminMainActivity
import com.lambton.perfectbnb.user.MainActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Initializing firebase authentication object
        firebaseAuth= FirebaseAuth.getInstance()

        //Onclick of login button
        button2.setOnClickListener(){
            loginUser()
        }
        //onClick of signup button, we are going to signup screen
        textView.setOnClickListener(){
            val intent= Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
    //Login function, It will compare email an password with firebase and login if everything matched
    private fun loginUser(){
        val email=editTextTextPersonName.text.toString()
        val password=editTextTextPersonName2.text.toString()
        if(email.isBlank()||password.isBlank()){
            Toast.makeText(this,"Email and Password can't be blank",Toast.LENGTH_SHORT).show()
            return
        }
        //Firebase in-build function to sign in the user with email and password
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    //If email is admin's, Move to admin screen
                    if(email == "admin@gmail.com"){
                        val intent = Intent(this, AdminMainActivity::class.java)
                        startActivity(intent)
                    // if its user's email, Move to user screen
                    }else {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                }else{
                    Toast.makeText(this,"Error while login",Toast.LENGTH_SHORT).show()

                }
            }
    }
}