package com.example.purelogregui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    //Authenticate Firebase
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Get current user
        auth = FirebaseAuth.getInstance()

        val currentuser = auth.currentUser
        if(currentuser != null) {
            startActivity(Intent(this@LoginActivity, ReviewActivity::class.java))
            finish()
        }

        login()
    }

    //Login user when button pressed
    private fun login() {
        btnLogin.setOnClickListener {

            if (TextUtils.isEmpty(et_emailLogin.text.toString())) {
                et_emailLogin.error = "Please enter an email address"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(et_passwordLogin.text.toString())) {
                et_passwordLogin.error = "Please enter a password"
                return@setOnClickListener
            }

            //Login user using information provided
            auth.signInWithEmailAndPassword(
                et_emailLogin.text.toString(),
                et_passwordLogin.text.toString()
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))

                        Toast.makeText(
                            this@LoginActivity,
                            "Login Successful",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login Failed, please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }

        //Animate page
        btnRegLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
    }
}
