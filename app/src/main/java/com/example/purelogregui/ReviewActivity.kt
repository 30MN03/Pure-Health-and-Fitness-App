package com.example.purelogregui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : AppCompatActivity() {
    //Authenticate Firebase
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)

        //Access database table named "Users"
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")

        review()
    }

    //Retrieve information
    private  fun review() {

        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        //Retrieve current user email
        et_reviewEmail.text = user?.email

        //Retrieve current user "Name" from the database
        userReference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                et_reviewName.text = snapshot.child("Name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                startActivity(Intent(this@ReviewActivity, HomeActivity::class.java))
                finish()
            }
        })

        btnSaveInfo.setOnClickListener {
            startActivity(Intent(this@ReviewActivity, HomeActivity::class.java))
            finish()
        }

        //Sign out when button pressed
        btn_signOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@ReviewActivity, LoginActivity::class.java))
            finish()
        }
    }
}