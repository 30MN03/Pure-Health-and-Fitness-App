package com.example.purelogregui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.btn_signOut
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btnLogin
import kotlinx.android.synthetic.main.activity_review.*

class HomeActivity : AppCompatActivity() {
    //Authenticate Firebase
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Access database table named "Users"
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")

        home()
    }

    //Retrieve information
    private  fun home() {

        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        //Retrieve current user "Name" from the database
        userReference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                et_homeName.text = snapshot.child("Name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
            }
        })

        //Redirect user to Profile page when button pressed
        btnPrfHome.setOnClickListener {
            startActivity(Intent(this@HomeActivity, ProfileActivity::class.java))
            finish()
        }

//        //Redirect user to Record page when button pressed
//        btnRecHome.setOnClickListener {
//            startActivity(Intent(this@HomeActivity, RecordActivity::class.java))
//            finish()
//        }
//
//        //Redirect user to Progress page when button pressed
//        btnPrgHome.setOnClickListener {
//            startActivity(Intent(this@HomeActivity, ProgressActivity::class.java))
//            finish()
//        }

        //Sign out when button pressed
        btn_signOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }
    }
}