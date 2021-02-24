package com.example.purelogregui

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    //Authenticate Firebase
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //Create database table named "Users"
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")

        register()

        btnLogRegister.setOnClickListener {
            onBackPressed()
        }
    }

    //Animate page
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    //Register user when button pressed
    private fun register() {
        btnRegister.setOnClickListener {

            if (TextUtils.isEmpty(et_nameRegister.text.toString())) {
                et_nameRegister.error = "Please enter your name"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(et_emailRegister.text.toString())) {
                et_emailRegister.error = "Please enter an email address"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(et_passwordRegister.text.toString())) {
                et_passwordRegister.error = "Please enter a password"
                return@setOnClickListener
            }

            //Create user using information provided
            auth.createUserWithEmailAndPassword(
                    et_emailRegister.text.toString(),
                    et_passwordRegister.text.toString()
            )
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            //Save user in table previously created, under "Name"
                            val currentUser = auth.currentUser
                            val currentUserDb = databaseReference?.child((currentUser?.uid!!))
                            currentUserDb?.child("Name")?.setValue(et_nameRegister.text.toString())

                            et_emailRegister.setText(auth.currentUser?.email)
                            et_nameRegister.setText(auth.currentUser?.displayName)

                            Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration Successful",
                                    Toast.LENGTH_LONG
                            ).show()
                            finish()

                            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)

                        } else {
                            Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration Failed, please try again",
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    }
        }
    }
}

