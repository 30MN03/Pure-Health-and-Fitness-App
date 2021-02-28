package com.example.purelogregui

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_record.*
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.activity_review.btnSaveInfo
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class RecordActivity : AppCompatActivity() {
    //Authenticate Firebase
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        //Access database table named "Users"
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Users")

        record()
    }


    val date: TextView = findViewById(R.id.tv_recordDate)
    date.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())


    //Retrieve information
    private  fun record() {




        btnRecord.setOnClickListener {
            startActivity(Intent(this@RecordActivity, HomeActivity::class.java))
            finish()
        }
    }
}