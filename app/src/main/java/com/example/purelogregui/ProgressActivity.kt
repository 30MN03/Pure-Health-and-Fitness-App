package com.example.purelogregui

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_progress.*
import java.util.*

class ProgressActivity : AppCompatActivity() {

    //Authenticate Firebase
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_progress)

        //Access database table named "Users"
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")

        progress()
    }

    private fun progress() {
        val user = auth.currentUser
        val userReference = databaseReference?.child(user?.uid!!)

        //Retrieve current user "Name" from the database
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                et_homeName.text = snapshot.child("Name").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                startActivity(Intent(this@ProgressActivity, HomeActivity::class.java))
                finish()
            }
        })

        //return to home page when button pressed
        btn_back.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@ProgressActivity, HomeActivity::class.java))
            finish()
        }
    }
}